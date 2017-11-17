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
public class ElectDAO extends BaseDataAccessObject {

    private static String VIEW = "Elects_v";
    
    
    public void insert(Elect elect) {
        DBModelUtil.processSecure(Elect.class.getName(), elect, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(elect);
    }

    public void update(Elect elect) {
        DBModelUtil.processSecure(Elect.class.getName(), elect, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(elect);
    }
    
    public void delete(Elect elect) {
        DBModelUtil.processSecure(Elect.class.getName(), elect, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(elect);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Elect get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Elect ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Elect elect = (Elect)list.get(0);
            
            DBModelUtil.processSecure(Elect.class.getName(), elect, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return elect;
        }

        return null;
    }
    
    public Elect getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Elect> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public Elect getDetail(String where) {
        
        List<Elect> resultList = this.search(null, where);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }

    private SQLWithParameters _makeSearchQuery(boolean isCountSQL, JSONObject filterParamObject, String extraWhere, String extraOrder) {
        
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
            sql.appendSQL("SELECT id, kind, account_id, hot_id, estimate_id, owner, write_time ");
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
        
        SQLWithParameters sql = _makeSearchQuery(true, filterParamObject, extraWhere, "");

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public List<Elect> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<Elect> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Elect> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Elect> resultList = new ArrayList<Elect>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Elect row = new Elect();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
                row.setHotId(CommonUtil.toIntDefault(objectArray[2]));
                row.setEstimateId(CommonUtil.toIntDefault(objectArray[3]));
                row.setOwner(CommonUtil.toIntDefault(objectArray[4]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[5])));
                
                DBModelUtil.processSecure(Elect.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }

    
}