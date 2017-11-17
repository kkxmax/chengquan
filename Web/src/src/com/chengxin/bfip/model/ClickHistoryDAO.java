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
public class ClickHistoryDAO extends BaseDataAccessObject {

	private static String VIEW = "ClickHistorys_v";

	//分类
	private int HISTORY_TYPE_CONNECTION = 1;		//联系我
	private int HISTORY_TYPE_SHOPPING = 2;			//产品分享
	private int HISTORY_TYPE_ITEM = 3;				//项目分享
	private int HISTORY_TYPE_SERVICE = 4;			//服务分享
	private int HISTORY_TYPE_ETC = 5;				//其他分享
	private int HISTORY_TYPE_BUY = 6;				//立即购买
	private int HISTORY_TYPE_REQUEST = 7;			//邀请好友
	private int HISTORY_TYPE_PERSONAL_DETAIL = 8;			//邀请好友
	private int HISTORY_TYPE_ENTERPRISE_DETAIL = 9;			//邀请好友
	private int HISTORY_TYPE_REPORT = 10;			//邀请好友
	
	//账户类型
	private int ACCOUNT_AKIND_PERSONAL = 1;
	private int ACCOUNT_AKIND_ENTERPRISE = 2;


	public List<ClickHistory> link_statis(int type , String from , String to){
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters dateSql = new SQLWithParameters("");
		String dateWhere = "";
		if(from == "" && to == ""){
			dateWhere = " ";
		} else if(from != "" && to== ""){
			dateWhere = " and click_date >= '"+ from + "' ";
		} else if(from == "" && to!= ""){
			dateWhere = " and click_date <= '"+ to + "' ";
		} else if(from != "" && to!= ""){
			dateWhere = " and click_date >= '"+ from +"' and click_date <= '"+ to +"'";
		}

		dateSql.appendSQL("select click_date , account_id from click_history_v where type = " + String.valueOf(type) + dateWhere + " group BY click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(CommonUtil.toStringDefault(objectArray[0]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
				
				SQLWithParameters personalSql = new SQLWithParameters("");

				personalSql.appendSQL("select count(id) as personal_count , account_id from click_history_v where type = " + String.valueOf(type) + " and akind = " + String.valueOf(ACCOUNT_AKIND_PERSONAL) + " and click_date = '" + row.getClick_date() + "'");

				Query personalQuery = session.createSQLQuery(personalSql.getSQL());

				DBModelUtil.fillParameter(personalSql, personalQuery);

				List personalList = null; 
				try { personalList = personalQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] personal = (Object[])personalList.get(0);
				
				row.setPersonal_cnt(CommonUtil.toIntDefault(personal[0]));
				
				
				SQLWithParameters enterpriseSql = new SQLWithParameters("");

				enterpriseSql.appendSQL("select count(id) as personal_count , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_CONNECTION) + " and akind = " + String.valueOf(ACCOUNT_AKIND_ENTERPRISE) + " and click_date = '" + row.getClick_date() + "'");

				Query enterpriseQuery = session.createSQLQuery(enterpriseSql.getSQL());

				DBModelUtil.fillParameter(enterpriseSql, enterpriseQuery);

				List enterpriseList = null; 
				try { enterpriseList = enterpriseQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] enterprise = (Object[])enterpriseList.get(0);
				
				row.setEnterprise_cnt(CommonUtil.toIntDefault(enterprise[0]));

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
	public List<ClickHistory> item_statis(int type , String from , String to){
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters dateSql = new SQLWithParameters("");
		String dateWhere = "";
		if(from == "" && to == ""){
			dateWhere = " ";
		} else if(from != "" && to== ""){
			dateWhere = " and click_date >= '"+ from + "' ";
		} else if(from == "" && to!= ""){
			dateWhere = " and click_date <= '"+ to + "' ";
		} else if(from != "" && to!= ""){
			dateWhere = " and click_date >= '"+ from +"' and click_date <= '"+ to +"'";
		}

		dateSql.appendSQL("select click_date , account_id from click_history_v where (type = 2 or type = 3 or type = 4)"  + dateWhere + " group BY click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(CommonUtil.toStringDefault(objectArray[0]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
				
				SQLWithParameters shopSql = new SQLWithParameters("");

				shopSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_SHOPPING) + " and click_date =  '" + row.getClick_date() + "'");

				Query shopQuery = session.createSQLQuery(shopSql.getSQL());

				DBModelUtil.fillParameter(shopSql, shopQuery);

				List shopList = null; 
				try { shopList = shopQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] shop = (Object[])shopList.get(0);
				
				row.setShop_cnt(CommonUtil.toIntDefault(shop[0]));
				
				
				SQLWithParameters itemSql = new SQLWithParameters("");

				itemSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_ITEM) + " and click_date =  '" + row.getClick_date() + "'");

				Query itemQuery = session.createSQLQuery(itemSql.getSQL());

				DBModelUtil.fillParameter(itemSql, itemQuery);

				List itemList = null; 
				try { itemList = itemQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] item = (Object[])itemList.get(0);
				
				row.setItem_cnt(CommonUtil.toIntDefault(item[0]));
				
				
				SQLWithParameters serviceSql = new SQLWithParameters("");

				serviceSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_SERVICE) + " and click_date =  '" + row.getClick_date() + "'");

				Query serviceQuery = session.createSQLQuery(serviceSql.getSQL());

				DBModelUtil.fillParameter(serviceSql, serviceQuery);

				List serviceList = null; 
				try { serviceList = serviceQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] service = (Object[])serviceList.get(0);
				
				row.setService_cnt(CommonUtil.toIntDefault(service[0]));

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
	public List<ClickHistory> etc_statis(int type , String from , String to){
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters dateSql = new SQLWithParameters("");
		String dateWhere = "";
		if(from == "" && to == ""){
			dateWhere = " ";
		} else if(from != "" && to== ""){
			dateWhere = " and click_date >= '"+ from + "' ";
		} else if(from == "" && to!= ""){
			dateWhere = " and click_date <= '"+ to + "' ";
		} else if(from != "" && to!= ""){
			dateWhere = " and click_date >= '"+ from +"' and click_date <= '"+ to +"'";
		}

		dateSql.appendSQL("select click_date , account_id from click_history_v where (type = 8 or type = 9 or type = 10)"  + dateWhere + " group BY click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(CommonUtil.toStringDefault(objectArray[0]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
				
				SQLWithParameters shopSql = new SQLWithParameters("");

				shopSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_PERSONAL_DETAIL) + " and click_date =  '" + row.getClick_date() + "'");

				Query shopQuery = session.createSQLQuery(shopSql.getSQL());

				DBModelUtil.fillParameter(shopSql, shopQuery);

				List shopList = null; 
				try { shopList = shopQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] shop = (Object[])shopList.get(0);
				
				row.setPersonal_detail_cnt(CommonUtil.toIntDefault(shop[0]));
				
				
				SQLWithParameters itemSql = new SQLWithParameters("");

				itemSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_ENTERPRISE_DETAIL) + " and click_date =  '" + row.getClick_date() + "'");

				Query itemQuery = session.createSQLQuery(itemSql.getSQL());

				DBModelUtil.fillParameter(itemSql, itemQuery);

				List itemList = null; 
				try { itemList = itemQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] item = (Object[])itemList.get(0);
				
				row.setEnterprise_detail_cnt(CommonUtil.toIntDefault(item[0]));
				
				
				SQLWithParameters serviceSql = new SQLWithParameters("");

				serviceSql.appendSQL("select count(id) as shop_cnt , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_REPORT) + " and click_date =  '" + row.getClick_date() + "'");

				Query serviceQuery = session.createSQLQuery(serviceSql.getSQL());

				DBModelUtil.fillParameter(serviceSql, serviceQuery);

				List serviceList = null; 
				try { serviceList = serviceQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] service = (Object[])serviceList.get(0);
				
				row.setReport_cnt(CommonUtil.toIntDefault(service[0]));

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
	public List<ClickHistory> buy_statis(int type , String from , String to){
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters dateSql = new SQLWithParameters("");
		String dateWhere = "";
		if(from == "" && to == ""){
			dateWhere = " ";
		} else if(from != "" && to== ""){
			dateWhere = " and click_date >= '"+ from + "' ";
		} else if(from == "" && to!= ""){
			dateWhere = " and click_date <= '"+ to + "' ";
		} else if(from != "" && to!= ""){
			dateWhere = " and click_date >= '"+ from +"' and click_date <= '"+ to +"'";
		}

		dateSql.appendSQL("select click_date , account_id from click_history_v where type = " + String.valueOf(type) + dateWhere + " group BY click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(CommonUtil.toStringDefault(objectArray[0]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
				
				SQLWithParameters buySql = new SQLWithParameters("");

				buySql.appendSQL("select count(id) as buy_count , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_BUY) + " and click_date = '" + row.getClick_date() + "'");

				Query buyQuery = session.createSQLQuery(buySql.getSQL());

				DBModelUtil.fillParameter(buySql, buyQuery);

				List buyList = null; 
				try { buyList = buyQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] buy = (Object[])buyList.get(0);
				
				row.setBuy_cnt(CommonUtil.toIntDefault(buy[0]));
				

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
	public List<ClickHistory> request_statis(int type , String from , String to){
		Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
		Transaction transaction = session.beginTransaction();

		SQLWithParameters dateSql = new SQLWithParameters("");
		String dateWhere = "";
		if(from == "" && to == ""){
			dateWhere = " ";
		} else if(from != "" && to== ""){
			dateWhere = " and click_date >= '"+ from + "' ";
		} else if(from == "" && to!= ""){
			dateWhere = " and click_date <= '"+ to + "' ";
		} else if(from != "" && to!= ""){
			dateWhere = " and click_date >= '"+ from +"' and click_date <= '"+ to +"'";
		}

		dateSql.appendSQL("select click_date , account_id from click_history_v where type = " + String.valueOf(type) + dateWhere + " group BY click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object[] objectArray = (Object[])queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(CommonUtil.toStringDefault(objectArray[0]));
				row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
				
				SQLWithParameters request_sql = new SQLWithParameters("");

				request_sql.appendSQL("select count(id) as buy_count , account_id from click_history_v where type = " + String.valueOf(HISTORY_TYPE_REQUEST) + " and click_date = '" + row.getClick_date() + "'");

				Query requestQuery = session.createSQLQuery(request_sql.getSQL());

				DBModelUtil.fillParameter(request_sql, requestQuery);

				List requestList = null; 
				try { requestList = requestQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				Object[] request = (Object[])requestList.get(0);
				
				row.setRequest_cnt(CommonUtil.toIntDefault(request[0]));
				

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
}

