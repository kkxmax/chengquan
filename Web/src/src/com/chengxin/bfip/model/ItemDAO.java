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
public class ItemDAO extends BaseDataAccessObject {

    private static String VIEW = "Items_v";
    
    
    public void insert(Item item) {
        DBModelUtil.processSecure(Item.class.getName(), item, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(item);
    }

    public void update(Item item) {
        DBModelUtil.processSecure(Item.class.getName(), item, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(item);
    }
    
    public void delete(Item item) {
        DBModelUtil.processSecure(Item.class.getName(), item, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(item);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Item get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Item ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Item item = (Item)list.get(0);
            
            DBModelUtil.processSecure(Item.class.getName(), item, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return item;
        }

        return null;
    }
    
    public Item getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Item> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public Item getDetail(String where) {
        
        List<Item> resultList = this.search(null, where);
        
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
            sql.appendSQL("SELECT id, code, account_id, name, fenlei_id, fenlei_name, city_id, city_name, province_id, province_name"
            		+ ", comment, need, weburl, is_show, status, status_name, up_time, down_time, contact_name, contact_mobile, contact_weixin "
            		+ ", logo, img_path1, img_path2, img_path3, img_path4, img_path5, write_time"
            		+ ", akind, akind_name, enter_kind, enter_kind_name, realname, enter_name, account_mobile ");
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
    
    public List<Item> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<Item> search(JSONObject filterParamObject, String extraWhere) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Item> resultList = new ArrayList<Item>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Item row = new Item();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setCode(CommonUtil.toStringDefault(objectArray[1]));
                row.setAccountId(CommonUtil.toIntDefault(objectArray[2]));
                row.setName(CommonUtil.toStringDefault(objectArray[3]));
                row.setFenleiId(CommonUtil.toIntDefault(objectArray[4]));
                row.setFenleiName(CommonUtil.toStringDefault(objectArray[5]));
                row.setCityId(CommonUtil.toIntDefault(objectArray[6]));
                row.setCityName(CommonUtil.toStringDefault(objectArray[7]));
                row.setProvinceId(CommonUtil.toIntDefault(objectArray[8]));
                row.setProvinceName(CommonUtil.toStringDefault(objectArray[9]));
                row.setComment(CommonUtil.toStringDefault(objectArray[10]));
                row.setNeed(CommonUtil.toStringDefault(objectArray[11]));
                row.setWeburl(CommonUtil.toStringDefault(objectArray[12]));
                row.setIsShow(CommonUtil.toIntDefault(objectArray[13]));
                row.setStatus(CommonUtil.toIntDefault(objectArray[14]));
                row.setStatusName(CommonUtil.toStringDefault(objectArray[15]));
                row.setUpTime(CommonUtil.toStringDefault(objectArray[16]));
                row.setDownTime(CommonUtil.toStringDefault(objectArray[17]));
                row.setContactName(CommonUtil.toStringDefault(objectArray[18]));
                row.setContactMobile(CommonUtil.toStringDefault(objectArray[19]));
                row.setContactWeixin(CommonUtil.toStringDefault(objectArray[20]));
                row.setLogo(CommonUtil.toStringDefault(objectArray[21]));
                row.setImgPath1(CommonUtil.toStringDefault(objectArray[22]));
                row.setImgPath2(CommonUtil.toStringDefault(objectArray[23]));
                row.setImgPath3(CommonUtil.toStringDefault(objectArray[24]));
                row.setImgPath4(CommonUtil.toStringDefault(objectArray[25]));
                row.setImgPath5(CommonUtil.toStringDefault(objectArray[26]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[27])));
                row.setAkind(CommonUtil.toIntDefault(objectArray[28]));
                row.setAkindName(CommonUtil.toStringDefault(objectArray[29]));
                row.setEnterKind(CommonUtil.toIntDefault(objectArray[30]));
                row.setEnterKindName(CommonUtil.toStringDefault(objectArray[31]));
                row.setRealname(CommonUtil.toStringDefault(objectArray[32]));
                row.setEnterName(CommonUtil.toStringDefault(objectArray[33]));
                row.setAccountMobile(CommonUtil.toStringDefault(objectArray[34]));
                
                DBModelUtil.processSecure(Item.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
}