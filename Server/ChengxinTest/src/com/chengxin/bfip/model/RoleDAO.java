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
import com.chengxin.common.SQLWithParameters;

/**
 *
 * @author Genesys Framework
 */
public class RoleDAO extends BaseDataAccessObject {

	private static String VIEW = "roles_v";


	public void insert(Role member) {
		DBModelUtil.processSecure(Role.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().save(member);
	}

	public void update(Role member) {
		DBModelUtil.processSecure(Role.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().update(member);
	}

	public void delete(Role member) {
		DBModelUtil.processSecure(Role.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);

		this.getHibernateTemplate().delete(member);
	}

	public void delete(int id) {
		this.getHibernateTemplate().delete(get(id));
	}

	public Role get(int id) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("FROM Role ");
		stringBuffer.append("WHERE id = :id ");

		List list = (List)this.getHibernateTemplate().findByNamedParam(
				stringBuffer.toString(),
				new String[]{"id"},
				new Object[]{id});

		stringBuffer = null;

		if (list.size() > 0) {
			Role member = (Role)list.get(0);

			DBModelUtil.processSecure(Role.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);

			return member;
		}

		return null;
	}

	public Role getDetail(int id) {

		JSONObject filterParamObject = new JSONObject();
		JSONArray equalParamArray = new JSONArray();
		JSONObject equalParam = new JSONObject();

		equalParam.put("id", id);
		equalParamArray.add(equalParam);
		filterParamObject.put("equal_param", equalParamArray);

		List<Role> resultList = this.search(filterParamObject);

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

		if(filterParamObject != null){
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
			sql.appendSQL("SELECT id ,title,func_personal,func_enter,func_fenlei,func_xingye,func_carousel,func_video,func_product,func_comment,func_statistic,func_item,func_hot,func_error,func_chanpin,func_opinion,func_system ");
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

	public List<Role> search(JSONObject filterParamObject) {
		return search(filterParamObject, "");
	}

	public List<Role> search(JSONObject filterParamObject, String extraWhere) {

		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere);

		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
		List<Role> resultList = new ArrayList<Role>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				Role row = new Role();

				row.setId(CommonUtil.toIntDefault(objectArray[0]));
				row.setTitle(CommonUtil.toStringDefault(objectArray[1]));
				row.setFuncPersonal(CommonUtil.toIntDefault(objectArray[2]));
				row.setFuncEnter(CommonUtil.toIntDefault(objectArray[3]));
				row.setFuncFenlei(CommonUtil.toIntDefault(objectArray[4]));
				row.setFuncXingye(CommonUtil.toIntDefault(objectArray[5]));
				row.setFuncCarousel(CommonUtil.toIntDefault(objectArray[6]));
				row.setFuncVideo(CommonUtil.toIntDefault(objectArray[7]));
				row.setFuncProduct(CommonUtil.toIntDefault(objectArray[8]));
				row.setFuncComment(CommonUtil.toIntDefault(objectArray[9]));
				row.setFuncStatistic(CommonUtil.toIntDefault(objectArray[10]));
				row.setFuncItem(CommonUtil.toIntDefault(objectArray[11]));
				row.setFuncHot(CommonUtil.toIntDefault(objectArray[12]));
				row.setFuncError(CommonUtil.toIntDefault(objectArray[13]));
				row.setFuncChanpin(CommonUtil.toIntDefault(objectArray[14]));
				row.setFuncOpinion(CommonUtil.toIntDefault(objectArray[15]));
				row.setFuncSystem(CommonUtil.toIntDefault(objectArray[16]));


				DBModelUtil.processSecure(Role.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				resultList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return resultList;
	}

	public boolean find(String extraWhere) {
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters sql = new SQLWithParameters("");
		sql.appendSQL("select id from roles where 1 " + extraWhere);    
		Query query = session.createSQLQuery(sql.getSQL());

		DBModelUtil.fillParameter(sql, query);

		List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

		transaction.commit();

		if(session.isOpen()) {session.close();}
		
		if(list.size() == 0){
			return true;
		}else{
			return false;
		}
		

	}

}