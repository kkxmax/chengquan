package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
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
public class ClickHistoryDAO extends BaseDataAccessObject {

	private static String VIEW = "click_history_v";

	//分类
	public static final int HISTORY_TYPE_CONTACT = 1;		//联系我
	public static final int HISTORY_TYPE_SHARE = 2;			//分享
	public static final int HISTORY_TYPE_BUY = 3;				//立即购买
	public static final int HISTORY_TYPE_REQUEST = 4;			//邀请好友
	
	public static final int HISTORY_SHARE_KIND_PRODUCT = 1;			//产品分享
	public static final int HISTORY_SHARE_KIND_ITEM = 2;			//项目分享
	public static final int HISTORY_SHARE_KIND_SERVICE = 3;			//服务分享
	public static final int HISTORY_SHARE_KIND_PERSONAL = 4;		//熟人详情分享,
	public static final int HISTORY_SHARE_KIND_ENTER = 5;			//企业详情分享
	public static final int HISTORY_SHARE_KIND_REPOET = 6;			//诚信报告分享
	public static final int HISTORY_SHARE_KIND_HOT_DETAIL = 7;		//热点详情


	public void insert(ClickHistory clickHistory) {
        DBModelUtil.processSecure(ClickHistory.class.getName(), clickHistory, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(clickHistory);
    }

    public void update(ClickHistory clickHistory) {
        DBModelUtil.processSecure(ClickHistory.class.getName(), clickHistory, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(clickHistory);
    }
    
    public void delete(ClickHistory clickHistory) {
        DBModelUtil.processSecure(ClickHistory.class.getName(), clickHistory, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(clickHistory);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public ClickHistory get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM ClickHistory ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	ClickHistory clickHistory = (ClickHistory)list.get(0);
            
            DBModelUtil.processSecure(ClickHistory.class.getName(), clickHistory, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return clickHistory;
        }

        return null;
    }
    
    public ClickHistory getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<ClickHistory> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public ClickHistory getDetail(String where) {
        
        List<ClickHistory> resultList = this.search(null, where);
        
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
            sql.appendSQL("SELECT id, owner_id, type, share_kind, account_id, product_id, item_id, service_id, click_date, contact_akind ");
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
    
    public List<ClickHistory> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<ClickHistory> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<ClickHistory> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }
    
    public List<ClickHistory> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<ClickHistory> resultList = new ArrayList<ClickHistory>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                ClickHistory row = new ClickHistory();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setOwnerId(CommonUtil.toIntDefault(objectArray[1]));
                row.setType(CommonUtil.toIntDefault(objectArray[2]));
                row.setShareKind(CommonUtil.toIntDefault(objectArray[3]));
                row.setAccountId(CommonUtil.toIntDefault(objectArray[4]));
                row.setProductId(CommonUtil.toIntDefault(objectArray[5]));
                row.setItemId(CommonUtil.toIntDefault(objectArray[6]));
                row.setServiceId(CommonUtil.toIntDefault(objectArray[7]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[8])));
                row.setClick_date(CommonUtil.toStringDefault(objectArray[9]));
                row.setContactAkind(CommonUtil.toIntDefault(objectArray[10]));
                
                DBModelUtil.processSecure(Account.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
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

		dateSql.appendSQL("select click_date from " + VIEW + " where type = " + type + dateWhere + " group by click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object object = queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(DateTimeUtil.dateOnlyFormat((Date) object));
				
				SQLWithParameters personalSql = new SQLWithParameters("");

				personalSql.appendSQL("select count(id) as personal_count from " + VIEW 
						+ " where type = " + type + " and contact_akind = " + AccountDAO.ACCOUNT_TYPE_PERSONAL 
						+ " and click_date = '" + row.getClick_date() + "' group by click_date ");

				Query personalQuery = session.createSQLQuery(personalSql.getSQL());

				DBModelUtil.fillParameter(personalSql, personalQuery);

				List personalList = null; 
				try { personalList = personalQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if (personalList.size() > 0) {
					Object personal = personalList.get(0);
					
					row.setPersonal_cnt(CommonUtil.toIntDefault(personal));
				}
				else {
					row.setPersonal_cnt(0);
				}
				
				
				SQLWithParameters enterpriseSql = new SQLWithParameters("");

				enterpriseSql.appendSQL("select count(id) as personal_count from " + VIEW
						+ " where type = " + HISTORY_TYPE_CONTACT + " and contact_akind = " + AccountDAO.ACCOUNT_TYPE_ENTERPRISE 
						+ " and click_date = '" + row.getClick_date() + "' group by click_date ");

				Query enterpriseQuery = session.createSQLQuery(enterpriseSql.getSQL());

				DBModelUtil.fillParameter(enterpriseSql, enterpriseQuery);

				List enterpriseList = null; 
				try { enterpriseList = enterpriseQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(enterpriseList.size() > 0) {
					Object enterprise = enterpriseList.get(0);
					
					row.setEnterprise_cnt(CommonUtil.toIntDefault(enterprise));
				}
				else {
					row.setEnterprise_cnt(0);
				}
				
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

		dateSql.appendSQL("select click_date from " + VIEW + " where type=" + HISTORY_TYPE_SHARE + " and (share_kind = " + HISTORY_SHARE_KIND_PRODUCT 
				+ " or share_kind = " + HISTORY_SHARE_KIND_ITEM + " or share_kind = " + HISTORY_SHARE_KIND_SERVICE + ")"  + dateWhere + " group by click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object object = queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(DateTimeUtil.dateOnlyFormat((Date) object));
				
				SQLWithParameters shopSql = new SQLWithParameters("");

				shopSql.appendSQL("select count(id) as shop_cnt from " + VIEW 
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_PRODUCT + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query shopQuery = session.createSQLQuery(shopSql.getSQL());

				DBModelUtil.fillParameter(shopSql, shopQuery);

				List shopList = null; 
				try { shopList = shopQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(shopList.size() > 0) {
					Object shop = shopList.get(0);
					
					row.setShop_cnt(CommonUtil.toIntDefault(shop));	
				}
				else {
					row.setShop_cnt(0);
				}
				
				
				SQLWithParameters itemSql = new SQLWithParameters("");

				itemSql.appendSQL("select count(id) as shop_cnt from " + VIEW 
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_ITEM + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query itemQuery = session.createSQLQuery(itemSql.getSQL());

				DBModelUtil.fillParameter(itemSql, itemQuery);

				List itemList = null; 
				try { itemList = itemQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(itemList.size() > 0) {
					Object item = itemList.get(0);
					
					row.setItem_cnt(CommonUtil.toIntDefault(item));	
				}
				else {
					row.setItem_cnt(0);
				}
				
				
				SQLWithParameters serviceSql = new SQLWithParameters("");

				serviceSql.appendSQL("select count(id) as shop_cnt from " + VIEW
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_SERVICE + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query serviceQuery = session.createSQLQuery(serviceSql.getSQL());

				DBModelUtil.fillParameter(serviceSql, serviceQuery);

				List serviceList = null; 
				try { serviceList = serviceQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(serviceList.size() > 0) {
					Object service = serviceList.get(0);
					
					row.setService_cnt(CommonUtil.toIntDefault(service));	
				}
				else {
					row.setService_cnt(0);
				}
				

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

		dateSql.appendSQL(String.format("select click_date from %s where type=%d and (share_kind = %d or share_kind = %d or share_kind = %d or share_kind = %d) group by click_date"
				, VIEW, HISTORY_TYPE_SHARE, HISTORY_SHARE_KIND_PERSONAL, HISTORY_SHARE_KIND_ENTER, HISTORY_SHARE_KIND_REPOET, HISTORY_SHARE_KIND_HOT_DETAIL));

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object object = queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(DateTimeUtil.dateOnlyFormat((Date) object));
				
				SQLWithParameters shopSql = new SQLWithParameters("");

				shopSql.appendSQL("select count(id) as shop_cnt from " + VIEW
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_PERSONAL + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query shopQuery = session.createSQLQuery(shopSql.getSQL());

				DBModelUtil.fillParameter(shopSql, shopQuery);

				List shopList = null; 
				try { shopList = shopQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(shopList.size() > 0) {
					Object shop = shopList.get(0);
					
					row.setPersonal_detail_cnt(CommonUtil.toIntDefault(shop));
				}
				else {
					row.setPersonal_detail_cnt(0);
				}
				
				
				SQLWithParameters itemSql = new SQLWithParameters("");

				itemSql.appendSQL("select count(id) as shop_cnt from " + VIEW
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_ENTER + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query itemQuery = session.createSQLQuery(itemSql.getSQL());

				DBModelUtil.fillParameter(itemSql, itemQuery);

				List itemList = null; 
				try { itemList = itemQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(itemList.size() > 0) {
					Object item = itemList.get(0);
					
					row.setEnterprise_detail_cnt(CommonUtil.toIntDefault(item));
				}
				else {
					row.setEnterprise_detail_cnt(0);
				}
				
				
				
				SQLWithParameters serviceSql = new SQLWithParameters("");

				serviceSql.appendSQL("select count(id) as shop_cnt from " + VIEW
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_REPOET + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query serviceQuery = session.createSQLQuery(serviceSql.getSQL());

				DBModelUtil.fillParameter(serviceSql, serviceQuery);

				List serviceList = null; 
				try { serviceList = serviceQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(serviceList.size() > 0) {
					Object service = serviceList.get(0);
					
					row.setReport_cnt(CommonUtil.toIntDefault(service));
				}
				else {
					row.setReport_cnt(0);
				}
				
				
				SQLWithParameters hotSql = new SQLWithParameters("");

				hotSql.appendSQL("select count(id) as cnt from " + VIEW
						+ " where type = " + HISTORY_TYPE_SHARE + " and share_kind=" + HISTORY_SHARE_KIND_HOT_DETAIL + " and click_date =  '" + row.getClick_date() + "' group by click_date ");

				Query hotQuery = session.createSQLQuery(hotSql.getSQL());

				DBModelUtil.fillParameter(hotSql, hotQuery);

				List hotList = null; 
				try { hotList = hotQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(hotList.size() > 0) {
					Object hot = hotList.get(0);
					
					row.setHotCnt(CommonUtil.toIntDefault(hot));
				}
				else {
					row.setHotCnt(0);
				}
				
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

		dateSql.appendSQL("select click_date from " + VIEW + " where type = " + String.valueOf(type) + dateWhere + " group by click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object object = queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(DateTimeUtil.dateOnlyFormat((Date) object));
				
				SQLWithParameters buySql = new SQLWithParameters("");

				buySql.appendSQL("select count(id) as buy_count from " + VIEW + " where type = " + HISTORY_TYPE_BUY + " and click_date = '" + row.getClick_date() + "' group by click_date ");

				Query buyQuery = session.createSQLQuery(buySql.getSQL());

				DBModelUtil.fillParameter(buySql, buyQuery);

				List buyList = null; 
				try { buyList = buyQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(buyList.size() > 0) {
					Object buy = buyList.get(0);
					
					row.setBuy_cnt(CommonUtil.toIntDefault(buy));
				}
				else {
					row.setBuy_cnt(0);
				}
				

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

		dateSql.appendSQL("select click_date from " + VIEW + " where type = " + String.valueOf(type) + dateWhere + " group by click_date;");

		Query query = session.createSQLQuery(dateSql.getSQL());

		DBModelUtil.fillParameter(dateSql, query);

		List queryList = null; 
		try { queryList = query.list(); }
		catch(Exception e) {e.printStackTrace();}
		List<ClickHistory> dateList = new ArrayList<ClickHistory>();

		if (queryList != null) {
			int listSize = queryList.size();

			for(int i = 0; i < listSize; i++) {
				Object object = queryList.get(i);

				ClickHistory row = new ClickHistory();

				row.setClick_date(DateTimeUtil.dateOnlyFormat((Date) object));
				
				SQLWithParameters request_sql = new SQLWithParameters("");

				request_sql.appendSQL("select count(id) as buy_count from " + VIEW + " where type = " + HISTORY_TYPE_REQUEST + " and click_date = '" + row.getClick_date() + "' group by click_date ");

				Query requestQuery = session.createSQLQuery(request_sql.getSQL());

				DBModelUtil.fillParameter(request_sql, requestQuery);

				List requestList = null; 
				try { requestList = requestQuery.list(); }
				catch(Exception e) {e.printStackTrace();}
				
				if(requestList.size() > 0) {
					Object request = requestList.get(0);
					
					row.setRequest_cnt(CommonUtil.toIntDefault(request));
				}
				else {
					row.setRequest_cnt(0);
				}
				

				DBModelUtil.processSecure(ClickHistory.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

				dateList.add(row);
			}
		}

		transaction.commit();

		if(session.isOpen()) {session.close();}

		return dateList;

	}
	
}

