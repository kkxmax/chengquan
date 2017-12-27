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
public class XyleixingDAO extends BaseDataAccessObject {

	private static String TABLE = "xyleixings";
    private static String VIEW = "xyleixings_v";
    
    //张号类型
    public static final int Xyleixing_ST_UP = 0;
    public static final int Xyleixing_ST_DOWN = 2;
    
    
    public void insert(Xyleixing member) {
        DBModelUtil.processSecure(Xyleixing.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(member);
    }

    public void update(Xyleixing member) {
        DBModelUtil.processSecure(Xyleixing.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(member);
    }
    
    public void delete(Xyleixing member) {
        DBModelUtil.processSecure(Xyleixing.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(member);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public int delete(String where) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	Transaction transaction = session.beginTransaction();
    	
    	String delQuery = "delete from " + TABLE + " where " + where;
    	
    	Query query = session.createSQLQuery(delQuery);
    	
    	int success = query.executeUpdate();
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return success;
    }
    
    public Xyleixing get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Xyleixing ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Xyleixing member = (Xyleixing)list.get(0);
            
            DBModelUtil.processSecure(Xyleixing.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return member;
        }

        return null;
    }
    
    public Xyleixing get(String where) {
    	 List<Xyleixing> resultList = this.search(null, where, "");
         
         if(resultList.size() > 0) {
         	return resultList.get(0);
         }
         else {
         	return null;	
         }
    }
    
    public Xyleixing getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Xyleixing> resultList = this.search(filterParamObject);
        
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
            sql.appendSQL("SELECT id , title , upper_id , write_time ");
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
        
        SQLWithParameters sql = _makeSearchQuery(true, filterParamObject, extraWhere, null);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public List<Xyleixing> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "", "");
    }
    
    public List<Xyleixing> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Xyleixing> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Xyleixing> resultList = new ArrayList<Xyleixing>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Xyleixing row = new Xyleixing();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setTitle(CommonUtil.toStringDefault(objectArray[1]));
                row.setUpperId(CommonUtil.toIntDefault(objectArray[2]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[3])));
                
                DBModelUtil.processSecure(Xyleixing.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
    public int countAccountByLevel1XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from accounts where xyleixing_id in (select id from xyleixings where upper_id=" + xyleixingId + ")";
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countAccountByLevel2XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from accounts where xyleixing_id=" + xyleixingId;
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countHotByLevel1XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from hots where xyleixing_id in (select id from xyleixings where upper_id=" + xyleixingId + ")";
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countHotByLevel2XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from hots where xyleixing_id=" + xyleixingId;
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countXingyesWatchByLevel1XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from xingyes_watch where xyleixing_id in (select id from xyleixings where upper_id=" + xyleixingId + ")";
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countXingyesWatchByLevel2XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from xingyes_watch where xyleixing_id=" + xyleixingId;
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countXingyesWatchedByLevel1XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from xingyes_watched where xyleixing_id in (select id from xyleixings where upper_id=" + xyleixingId + ")";
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
    
    public int countXingyesWatchedByLevel2XyleixingId(int xyleixingId) {
    	Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        
    	String sql = "select count(*) from xingyes_watched where xyleixing_id=" + xyleixingId;
    	
        Query query = session.createSQLQuery(sql);
        
        List list = null; try { list = query.list(); } catch(Exception e) {e.printStackTrace();}

        int result = list ==  null ? 0 : Integer.parseInt("" + list.get(0));
        
        if(session.isOpen()) {session.close();}
        
        return result;
    }
}