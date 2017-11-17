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
public class ErrorDAO extends BaseDataAccessObject {

	private static String VIEW = "Errors_v";

	//状态
	public static final int ERROR_ST_PROCESSING = 1;
	public static final int ERROR_ST_PASS = 2;
	public static final int ERROR_ST_NOPASS = 3;

	//纠错类型
	public static final int ERROR_KIND_OVER = 1;
	public static final int ERROR_KIND_FALSE = 2;

	public void insert(Errors member) {
		DBModelUtil.processSecure(Errors.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().save(member);
	}

	public void update(Errors member) {
		DBModelUtil.processSecure(Errors.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().update(member);
	}

	public void delete(Errors member) {
		DBModelUtil.processSecure(Errors.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().delete(member);
	}

	public void delete(int id) {
		this.getHibernateTemplate().delete(get(id));
	}

	public Errors get(int id) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("FROM Errors ");
		stringBuffer.append("WHERE id = :id ");

		List list = (List)this.getHibernateTemplate().findByNamedParam(
				stringBuffer.toString(),
				new String[]{"id"},
				new Object[]{id});

		stringBuffer = null;

		if (list.size() > 0) {
			Errors member = (Errors)list.get(0);

			DBModelUtil.processSecure(Errors.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);

			return member;
		}

		return null;
	}

	public Errors getDetail(int id) {

		JSONObject filterParamObject = new JSONObject();
		JSONArray equalParamArray = new JSONArray();
		JSONObject equalParam = new JSONObject();

		equalParam.put("id", id);
		equalParamArray.add(equalParam);
		filterParamObject.put("equal_param", equalParamArray);

		List<Errors> resultList = this.search(filterParamObject);

		if(resultList.size() > 0) {
			return resultList.get(0);
		}
		else {
			return null;	
		}
	}

	public Errors getDetail(String where) {
        
        List<Errors> resultList = this.search(null, where);
        
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
			sql.appendSQL("SELECT id, estimate_id, owner_id, kind, reason, whyis, status"
					+ ", img_path1, img_path2, img_path3, img_path4, img_path5, img_path6, write_time, kind_name, status_name"
					+ ", owner_akind, owner_mobile, owner_realname, owner_enter_name"
					+ ", estimatee_id, estimatee_akind, estimatee_realname, estimatee_enter_name"
					+ ", estimater_id, estimater_akind, estimater_realname, estimater_enter_name"
					+ ", estimate_content, owner_name, estimatee_name, estimater_name, estimater_logo"
					+ ", estimate_img_path1, estimate_img_path2, estimate_img_path3, estimate_img_path4, estimate_img_path5");
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

	public List<Errors> search(JSONObject filterParamObject) {
		return search(filterParamObject, "");
	}
	
	public List<Errors> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Errors> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }

	public List<Errors> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
		List<Errors> resultList = new ArrayList<Errors>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				Errors row = new Errors();

				row.setId(CommonUtil.toIntDefault(objectArray[0]));
				row.setEstimateId(CommonUtil.toIntDefault(objectArray[1]));
				row.setOwnerId(CommonUtil.toIntDefault(objectArray[2]));
				row.setKind(CommonUtil.toIntDefault(objectArray[3]));
				row.setReason(CommonUtil.toStringDefault(objectArray[4]));
				row.setWhyis(CommonUtil.toStringDefault(objectArray[5]));
				row.setStatus(CommonUtil.toIntDefault(objectArray[6]));
				row.setImgPath1(CommonUtil.toStringDefault(objectArray[7]));
				row.setImgPath2(CommonUtil.toStringDefault(objectArray[8]));
				row.setImgPath3(CommonUtil.toStringDefault(objectArray[9]));
				row.setImgPath4(CommonUtil.toStringDefault(objectArray[10]));
				row.setImgPath5(CommonUtil.toStringDefault(objectArray[11]));
				row.setImgPath6(CommonUtil.toStringDefault(objectArray[12]));
				row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[13])));
				row.setWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[13]))));
				row.setKindName(CommonUtil.toStringDefault(objectArray[14]));
				row.setStatusName(CommonUtil.toStringDefault(objectArray[15]));
				row.setOwnerAkind(CommonUtil.toIntDefault(objectArray[16]));
				row.setOwnerMobile(CommonUtil.toStringDefault(objectArray[17]));
				row.setOwnerRealname(CommonUtil.toStringDefault(objectArray[18]));
				row.setOwnerEnterName(CommonUtil.toStringDefault(objectArray[19]));
				row.setEstimateeId(CommonUtil.toIntDefault(objectArray[20]));
				row.setEstimateeAkind(CommonUtil.toIntDefault(objectArray[21]));
				row.setEstimateeRealname(CommonUtil.toStringDefault(objectArray[22]));
				row.setEstimateeEnterName(CommonUtil.toStringDefault(objectArray[23]));
				row.setEstimaterId(CommonUtil.toIntDefault(objectArray[24]));
				row.setEstimaterAkind(CommonUtil.toIntDefault(objectArray[25]));
				row.setEstimaterRealname(CommonUtil.toStringDefault(objectArray[26]));
				row.setEstimaterEnterName(CommonUtil.toStringDefault(objectArray[27]));
				row.setEstimateContent(CommonUtil.toStringDefault(objectArray[28]));
				row.setOwnerName(CommonUtil.toStringDefault(objectArray[29]));
				row.setEstimateeName(CommonUtil.toStringDefault(objectArray[30]));
				row.setEstimaterName(CommonUtil.toStringDefault(objectArray[31]));
				row.setEstimaterLogo(CommonUtil.toStringDefault(objectArray[32]));
				row.setEstimateImgPath1(CommonUtil.toStringDefault(objectArray[33]));
				row.setEstimateImgPath2(CommonUtil.toStringDefault(objectArray[34]));
				row.setEstimateImgPath3(CommonUtil.toStringDefault(objectArray[35]));
				row.setEstimateImgPath4(CommonUtil.toStringDefault(objectArray[36]));
				row.setEstimateImgPath5(CommonUtil.toStringDefault(objectArray[37]));
				row.setImgPaths(CommonUtil.getImgPathList(objectArray, 7, 12));
				row.setEstimateImgPaths(CommonUtil.getImgPathList(objectArray, 33, 37));


				DBModelUtil.processSecure(Errors.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				resultList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return resultList;
	}

}