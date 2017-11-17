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
public class EstimatesDAO extends BaseDataAccessObject {

	private static String VIEW = "Estimates_v";

	//张号类型
	public static final int ESTIMATE_KIND_FORWORD = 1;
	public static final int ESTIMATE_KIND_BACKWORD = 2;

	public static final int ESTIMATE_METHOD_DETAIl = 1;
	public static final int ESTIMATE_METHOD_QUICK = 2;


	public void insert(Estimates member) {
		DBModelUtil.processSecure(Estimates.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().save(member);
	}

	public void update(Estimates member) {
		DBModelUtil.processSecure(Estimates.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().update(member);
	}

	public void delete(Estimates member) {
		DBModelUtil.processSecure(Estimates.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().delete(member);
	}

	public void delete(int id) {
		this.getHibernateTemplate().delete(get(id));
	}

	public Estimates get(int id) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("FROM Estimates ");
		stringBuffer.append("WHERE id = :id ");

		List list = (List)this.getHibernateTemplate().findByNamedParam(
				stringBuffer.toString(),
				new String[]{"id"},
				new Object[]{id});

		stringBuffer = null;

		if (list.size() > 0) {
			Estimates member = (Estimates)list.get(0);

			DBModelUtil.processSecure(Estimates.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);

			return member;
		}

		return null;
	}

	public Estimates getDetail(int id) {

		JSONObject filterParamObject = new JSONObject();
		JSONArray equalParamArray = new JSONArray();
		JSONObject equalParam = new JSONObject();

		equalParam.put("id", id);
		equalParamArray.add(equalParam);
		filterParamObject.put("equal_param", equalParamArray);

		List<Estimates> resultList = this.search(filterParamObject);

		if(resultList.size() > 0) {
			return resultList.get(0);
		}
		else {
			return null;	
		}
	}

	private SQLWithParameters _makeSearchQuery(boolean isCountSQL, JSONObject filterParamObject, String extraWhere) {

		SQLWithParameters sql = new SQLWithParameters("");
		JSONArray likeParamArray = new JSONArray();
		JSONArray equalParamArray = new JSONArray();
		int offset = -1;
		int limit = -1;
		String orderCol = "";
		String orderDir = "asc";

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

		if(isCountSQL) {
			sql.appendSQL("SELECT COUNT(*)");
		} else {
			sql.appendSQL("SELECT id , upper_id , account_id, hot_id, owner , content , kind , method , reason , update_time , write_time , kind_name , method_name ");
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

		if(!isCountSQL && !orderCol.isEmpty()) {
			sql.appendSQL(" ORDER BY " + orderCol + " " + orderDir);
		}

		if(!isCountSQL && offset != -1 && limit != -1) {
			sql.appendSQL(" LIMIT " + offset + "," + limit);	
		}

		return sql;
	}

	public int count(JSONObject filterParamObject) {
		return count(filterParamObject, "");
	}

	public int count(JSONObject filterParamObject, String extraWhere) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(true, filterParamObject, extraWhere);

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

		int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return result;
	}

	public List<Estimates> search(JSONObject filterParamObject) {
		return search(filterParamObject, "");
	}

	public List<Estimates> search(JSONObject filterParamObject, String extraWhere) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere);

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
		List<Estimates> resultList = new ArrayList<Estimates>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				Estimates row = new Estimates();

				row.setId(CommonUtil.toIntDefault(objectArray[0]));
				row.setUpperId(CommonUtil.toIntDefault(objectArray[1]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[2]));
				row.setHotId(CommonUtil.toIntDefault(objectArray[3]));
				row.setOwner(CommonUtil.toIntDefault(objectArray[4]));
				row.setContent(CommonUtil.toStringDefault(objectArray[5]));
				row.setKind(CommonUtil.toIntDefault(objectArray[6]));
				row.setMethod(CommonUtil.toIntDefault(objectArray[7]));
				row.setReason(CommonUtil.toStringDefault(objectArray[8]));
				row.setUpdateTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[9])));
				row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[10])));
				row.setKindName(CommonUtil.toStringDefault(objectArray[11]));
				row.setMethodName(CommonUtil.toStringDefault(objectArray[12]));


				DBModelUtil.processSecure(Estimates.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				resultList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return resultList;
	}

}