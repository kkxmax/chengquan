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
public class UserDAO extends BaseDataAccessObject {

    private static String VIEW = "Users_v";
    
    
    public void insert(User member) {
        DBModelUtil.processSecure(User.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(member);
    }

    public void update(User member) {
        DBModelUtil.processSecure(User.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(member);
    }
    
    public void delete(User member) {
        DBModelUtil.processSecure(User.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(member);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public User get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM User ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	User member = (User)list.get(0);
            
            DBModelUtil.processSecure(User.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return member;
        }

        return null;
    }
    
    public User getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<User> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public User getDetail(String where) {
        
        List<User> resultList = this.search(null, where);
        
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
            sql.appendSQL("SELECT id , username , realname , password , role_id , title , func_personal , func_enter "
            		+ ",func_product , func_item , func_carousel , func_video , func_hot , func_comment , func_error"
            		+ ", func_statistic , func_fenlei , func_chanpin , func_xingye , func_opinion , func_system , write_time ");
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
    
    public List<User> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<User> search(JSONObject filterParamObject, String extraWhere) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<User> resultList = new ArrayList<User>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                User row = new User();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setUsername(CommonUtil.toStringDefault(objectArray[1]));
                row.setRealname(CommonUtil.toStringDefault(objectArray[2]));
                row.setPassword(CommonUtil.toStringDefault(objectArray[3]));
                row.setRoleId(CommonUtil.toIntDefault(objectArray[4]));
                row.setTitle(CommonUtil.toStringDefault(objectArray[5]));
                row.setFuncPersonal(CommonUtil.toIntDefault(objectArray[6]));
                row.setFuncEnter(CommonUtil.toIntDefault(objectArray[7]));
                row.setFuncProduct(CommonUtil.toIntDefault(objectArray[8]));
                row.setFuncItem(CommonUtil.toIntDefault(objectArray[9]));
                row.setFuncCarousel(CommonUtil.toIntDefault(objectArray[10]));
                row.setFuncVideo(CommonUtil.toIntDefault(objectArray[11]));
                row.setFuncHot(CommonUtil.toIntDefault(objectArray[12]));
                row.setFuncComment(CommonUtil.toIntDefault(objectArray[13]));
                row.setFuncError(CommonUtil.toIntDefault(objectArray[14]));
                row.setFuncStatistic(CommonUtil.toIntDefault(objectArray[15]));
                row.setFuncFenlei(CommonUtil.toIntDefault(objectArray[16]));
                row.setFuncChanpin(CommonUtil.toIntDefault(objectArray[17]));
                row.setFuncXingye(CommonUtil.toIntDefault(objectArray[18]));
                row.setFuncOpinion(CommonUtil.toIntDefault(objectArray[19]));
                row.setFuncSystem(CommonUtil.toIntDefault(objectArray[20]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[21])));
                
                
                DBModelUtil.processSecure(User.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
    
    public List<User> getAllRoles() {
        
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = new SQLWithParameters("");

        sql.appendSQL("SELECT id , title ");
        sql.appendSQL(" FROM roles");
        sql.appendSQL(" WHERE 1 ");
        
        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<User> resultList = new ArrayList<User>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                User row = new User();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setTitle(CommonUtil.toStringDefault(objectArray[1]));
                
                DBModelUtil.processSecure(User.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
}