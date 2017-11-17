package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.common.BaseDataAccessObject;
import com.chengxin.common.DBModelUtil;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.SQLWithParameters;

/**
 *
 * @author Genesys Framework
 */
public class EstimateDAO extends BaseDataAccessObject {

	private static String VIEW = "Estimates_v";

	public static final int ESTIMATE_TYPE_PERSONAL_OR_ENTER = 1;
	public static final int ESTIMATE_TYPE_HOT = 2;
	
	public static final int ESTIMATE_KIND_FORWORD = 1;
	public static final int ESTIMATE_KIND_BACKWORD = 2;

	public static final int ESTIMATE_METHOD_DETAIl = 1;
	public static final int ESTIMATE_METHOD_QUICK = 2;


	public void insert(Estimate member) {
		DBModelUtil.processSecure(Estimate.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().save(member);
	}

	public void update(Estimate member) {
		DBModelUtil.processSecure(Estimate.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().update(member);
	}

	public void delete(Estimate member) {
		DBModelUtil.processSecure(Estimate.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().delete(member);
	}

	public void delete(int id) {
		this.getHibernateTemplate().delete(get(id));
	}

	public Estimate get(int id) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("FROM Estimate ");
		stringBuffer.append("WHERE id = :id ");

		List list = (List)this.getHibernateTemplate().findByNamedParam(
				stringBuffer.toString(),
				new String[]{"id"},
				new Object[]{id});

		stringBuffer = null;

		if (list.size() > 0) {
			Estimate member = (Estimate)list.get(0);

			DBModelUtil.processSecure(Estimate.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);

			return member;
		}

		return null;
	}

	public Estimate getDetail(int id) {

		JSONObject filterParamObject = new JSONObject();
		JSONArray equalParamArray = new JSONArray();
		JSONObject equalParam = new JSONObject();

		equalParam.put("id", id);
		equalParamArray.add(equalParam);
		filterParamObject.put("equal_param", equalParamArray);

		List<Estimate> resultList = this.search(filterParamObject);

		if(resultList.size() > 0) {
			return resultList.get(0);
		}
		else {
			return null;	
		}
	}

	private SQLWithParameters _makeSearchQuery(boolean isCountSQL, JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {

		SQLWithParameters sql = new SQLWithParameters("");
		JSONArray likeParamArray = new JSONArray();
		JSONArray equalParamArray = new JSONArray();
		int offset = -1;
		int limit = -1;
		String orderCol = "";
		String orderDir = "asc";

		if(filterParamObject != null) {
			if(filterParamObject.has("like_param")) {
				likeParamArray = filterParamObject.getJSONArray("like_param");	
			}
			if(filterParamObject.has("equal_param")) {
				equalParamArray = filterParamObject.getJSONArray("equal_param");	
			}
			if(filterParamObject.has("order_col")) {
				orderCol = filterParamObject.getString("order_col");	
			}
			if(filterParamObject.has("order_dir")) {
				orderDir = filterParamObject.getString("order_dir");	
			}
			if(filterParamObject.has("start")) {
				offset = filterParamObject.getInt("start");	
			}
			if(filterParamObject.has("length")) {
				limit = filterParamObject.getInt("length");	
			}
		}
		

		if(isCountSQL) {
			sql.appendSQL("SELECT COUNT(*)");
		} else {
			sql.appendSQL("SELECT id, type, upper_id, account_id, hot_id, owner, content, kind, method, reason"
					+ ", update_time, write_time, kind_name, method_name, elect_cnt"
					+ ", owner_akind, owner_realname, owner_enter_name"
					+ ", target_account_mobile, target_account_akind, target_account_realname, target_account_enter_name"
					+ ", target_account_elect_cnt, target_account_feedback_cnt"
					+ ", img_path1, img_path2, img_path3, img_path4, img_path5, target_account_logo, owner_logo, is_false, owner_mobile, owner_name, target_account_name ");
		}

		sql.appendSQL(" FROM " +  VIEW);
		sql.appendSQL(" WHERE 1 ");

		for(int i=0; i<likeParamArray.size(); i++) {
			JSONObject oneParam = likeParamArray.getJSONObject(i);
			Iterator<String> itr = oneParam.keys();
			while(itr.hasNext()) {
				String key = itr.next();
				sql.appendSQL(" AND " + key + " LIKE '%" + oneParam.get(key) + "%' ");
			}
		}

		for(int i=0; i<equalParamArray.size(); i++) {
			JSONObject oneParam = equalParamArray.getJSONObject(i);
			Iterator<String> itr = oneParam.keys();
			while(itr.hasNext()) {
				String key = itr.next();
				sql.appendSQL(" AND " + key + "=" + oneParam.get(key));
			}
		}

		if(!extraWhere.isEmpty()) {
			sql.appendSQL(" AND " + extraWhere);
		}

		if(!isCountSQL) {
        	if(!groupby.isEmpty()) {
        		sql.appendSQL(" GROUP BY " + groupby);
        	}
        	String orderby = "";
        	if(!orderCol.isEmpty()) {
            	orderby = orderCol + " " + orderDir;
            }
        	
        	if(!extraOrder.isEmpty()) {
        		if(orderby.isEmpty()) {
        			orderby += extraOrder;	
        		}
        		else {
        			orderby += " ," + extraOrder;
        		}
        		
        	}
        	
        	if(orderby.isEmpty()) {
        		orderby = "id asc";
        	}
        	sql.appendSQL(" ORDER BY " + orderby);
            
            if(offset != -1 && limit != -1) {
            	sql.appendSQL(" LIMIT " + offset + "," + limit);	
            }
        }

		return sql;
	}

	public int count(JSONObject filterParamObject) {
		return count(filterParamObject, "");
	}

	public int count(JSONObject filterParamObject, String extraWhere) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(true, filterParamObject, extraWhere, "", "");

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

		int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return result;
	}

	public List<Estimate> search(JSONObject filterParamObject) {
		return search(filterParamObject, "");
	}
	
	public List<Estimate> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Estimate> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }

	public List<Estimate> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
		List<Estimate> resultList = new ArrayList<Estimate>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				Estimate row = new Estimate();

				row.setId(CommonUtil.toIntDefault(objectArray[0]));
				row.setType(CommonUtil.toIntDefault(objectArray[1]));
				row.setUpperId(CommonUtil.toIntDefault(objectArray[2]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[3]));
				row.setHotId(CommonUtil.toIntDefault(objectArray[4]));
				row.setOwner(CommonUtil.toIntDefault(objectArray[5]));
				row.setContent(CommonUtil.toStringDefault(objectArray[6]));
				row.setKind(CommonUtil.toIntDefault(objectArray[7]));
				row.setMethod(CommonUtil.toIntDefault(objectArray[8]));
				row.setReason(CommonUtil.toStringDefault(objectArray[9]));
				row.setUpdateTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[10])));
				row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[11])));
				row.setWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[11]))));
				row.setKindName(CommonUtil.toStringDefault(objectArray[12]));
				row.setMethodName(CommonUtil.toStringDefault(objectArray[13]));
				row.setElectCnt(CommonUtil.toIntDefault(objectArray[14]));
				row.setOwnerAkind(CommonUtil.toIntDefault(objectArray[15]));
				row.setOwnerRealname(CommonUtil.toStringDefault(objectArray[16]));
				row.setOwnerEnterName(CommonUtil.toStringDefault(objectArray[17]));
				row.setTargetAccountMobile(CommonUtil.toStringDefault(objectArray[18]));
				row.setTargetAccountAkind(CommonUtil.toIntDefault(objectArray[19]));
				row.setTargetAccountRealname(CommonUtil.toStringDefault(objectArray[20]));
				row.setTargetAccountEnterName(CommonUtil.toStringDefault(objectArray[21]));
				row.setTargetAccountElectCnt(CommonUtil.toIntDefault(objectArray[22]));
				row.setTargetAccountFeedbackCnt(CommonUtil.toIntDefault(objectArray[23]));
				row.setImgPath1(CommonUtil.toStringDefault(objectArray[24]));
                row.setImgPath2(CommonUtil.toStringDefault(objectArray[25]));
                row.setImgPath3(CommonUtil.toStringDefault(objectArray[26]));
                row.setImgPath4(CommonUtil.toStringDefault(objectArray[27]));
                row.setImgPath5(CommonUtil.toStringDefault(objectArray[28]));
				row.setImgPaths(CommonUtil.getImgPathList(objectArray, 24, 28));
				row.setTargetAccountLogo(CommonUtil.toStringDefault(objectArray[29]));
				row.setOwnerLogo(CommonUtil.toStringDefault(objectArray[30]));
				row.setIsFalse(CommonUtil.toIntDefault(objectArray[31]));
				row.setOwnerMobile(CommonUtil.toStringDefault(objectArray[32]));
				row.setOwnerName(CommonUtil.toStringDefault(objectArray[33]));
				row.setTargetAccountName(CommonUtil.toStringDefault(objectArray[34]));

				DBModelUtil.processSecure(Estimate.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				resultList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return resultList;
	}

}