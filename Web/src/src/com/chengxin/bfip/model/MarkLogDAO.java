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
public class MarkLogDAO extends BaseDataAccessObject {

    private static String VIEW = "Mark_logs_v";
    
    public static final int LOG_KIND_INVITE = 1;
    public static final int LOG_KIND_ESTIMATE_GIVE = 2;
    public static final int LOG_KIND_ESTIMATE_RECEIVE = 3;
    public static final int LOG_KIND_CORRECT_GIVE = 4;
    public static final int LOG_KIND_CORRECT_RECEIVE = 5;
    
    public static final int PMARK_INVITE = 2;
    public static final int PMARK_ESTIMATE_P_GIVE_DETAIL = 5;
    public static final int PMARK_ESTIMATE_P_GIVE_QUICK = 3;
    public static final int PMARK_ESTIMATE_P_RECEIVE = 4;
    public static final int PMARK_ESTIMATE_N_GIVE = 5;
    public static final int NMARK_ESTIMATE_N_RECEIVE = 4;
    public static final int PMARK_CORRECT_GIVE = 6;
    public static final int NMARK_CORRECT_RECEIVE_OVER = 4;
    public static final int NMARK_CORRECT_RECEIVE_FALSE = 7;

    
    public void insert(MarkLog markLog) {
        DBModelUtil.processSecure(MarkLog.class.getName(), markLog, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(markLog);
    }

    public void update(MarkLog markLog) {
        DBModelUtil.processSecure(MarkLog.class.getName(), markLog, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(markLog);
    }
    
    public void delete(MarkLog markLog) {
        DBModelUtil.processSecure(MarkLog.class.getName(), markLog, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(markLog);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public MarkLog get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM MarkLog ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	MarkLog markLog = (MarkLog)list.get(0);
            
            DBModelUtil.processSecure(MarkLog.class.getName(), markLog, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return markLog;
        }

        return null;
    }
    
    public MarkLog getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<MarkLog> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public MarkLog getDetail(String where) {
        
        List<MarkLog> resultList = this.search(null, where);
        
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
        JSONArray dateParamArray = new JSONArray();
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
            if(filterParamObject.has("date_search_param")) {
            	dateParamArray = filterParamObject.getJSONArray("date_search_param");	
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
            sql.appendSQL("SELECT id, account_id, kind, estimate_kind, estimate_method, error_kind, pmark, nmark, sender_id, write_time"
            		+ ", estimate_kind_name, estimate_method_name, error_kind_name"
            		+ ", account_akind, account_realname, account_enter_name, sender_akind, sender_realname, sender_enter_name ");
        }
        
        sql.appendSQL(" FROM " + VIEW);
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
        
        for(int i=0; i<dateParamArray.size(); i++) {
        	JSONObject oneParam = dateParamArray.getJSONObject(i);
        	Iterator<String> itr = oneParam.keys();
        	while(itr.hasNext()) {
        		String key = itr.next();
        		JSONObject val = oneParam.getJSONObject(key);
        		if(val.has("from")) {
    				sql.appendSQL(" AND " + key + ">='" + val.get("from") + "'");
    			}
    			if(val.has("to")) {
    				sql.appendSQL(" AND " + key + "<='" + val.get("to") + "'");
    			}
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
    
    public List<MarkLog> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<MarkLog> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<MarkLog> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }
    
    public List<MarkLog> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<MarkLog> resultList = new ArrayList<MarkLog>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                MarkLog row = new MarkLog();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setAccountId(CommonUtil.toIntDefault(objectArray[1]));
                row.setKind(CommonUtil.toIntDefault(objectArray[2]));
                row.setEstimateKind(CommonUtil.toIntDefault(objectArray[3]));
                row.setEstimateMethod(CommonUtil.toIntDefault(objectArray[4]));
                row.setErrorKind(CommonUtil.toIntDefault(objectArray[5]));
                row.setPmark(CommonUtil.toIntDefault(objectArray[6]));
                row.setNmark(CommonUtil.toIntDefault(objectArray[7]));
                row.setSenderId(CommonUtil.toIntDefault(objectArray[8]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[9])));
                row.setWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[9]))));
                row.setEstimateKindName(CommonUtil.toStringDefault(objectArray[10]));
                row.setEstimateMethodName(CommonUtil.toStringDefault(objectArray[11]));
                row.setErrorKindName(CommonUtil.toStringDefault(objectArray[12]));
                row.setAccountAkind(CommonUtil.toIntDefault(objectArray[13]));
                row.setAccountRealname(CommonUtil.toStringDefault(objectArray[14]));
                row.setAccountEnterName(CommonUtil.toStringDefault(objectArray[15]));
                row.setSenderAkind(CommonUtil.toIntDefault(objectArray[16]));
                row.setSenderRealname(CommonUtil.toStringDefault(objectArray[17]));
                row.setSenderEnterName(CommonUtil.toStringDefault(objectArray[18]));
                row.setMsg(CommonUtil.makeMarkLogMsg(row));
                
                DBModelUtil.processSecure(MarkLog.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
}