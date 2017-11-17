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
public class FavouriteDAO extends BaseDataAccessObject {

    private static String VIEW = "Favourites_v";
    
    public static final int FAVOURITE_KIND_PRODUCT = 1;    // 产品
    public static final int FAVOURITE_KIND_HOT = 2;  		// 热点

    public void insert(Favourite favourite) {
        DBModelUtil.processSecure(Favourite.class.getName(), favourite, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(favourite);
    }

    public void update(Favourite favourite) {
        DBModelUtil.processSecure(Favourite.class.getName(), favourite, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(favourite);
    }
    
    public void delete(Favourite favourite) {
        DBModelUtil.processSecure(Favourite.class.getName(), favourite, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(favourite);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Favourite get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Favourite ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Favourite favourite = (Favourite)list.get(0);
            
            DBModelUtil.processSecure(Favourite.class.getName(), favourite, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return favourite;
        }

        return null;
    }
    
    public Favourite getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Favourite> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public Favourite getDetail(String where) {
        
        List<Favourite> resultList = this.search(null, where);
        
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
            sql.appendSQL("SELECT id, kind, owner, product_id, hot_id, write_time"
            		+ ", product_name, product_price, product_img_path1"
            		+ ", hot_title, hot_content, hot_visit_cnt, hot_comment_cnt, hot_write_time, hot_img_path1, hot_img_path2, hot_img_path3, hot_img_path4, hot_img_path5"
            		+ ", hot_img_path6, hot_img_path7, hot_img_path8, hot_img_path9, hot_img_path10, hot_img_path11, hot_img_path12, hot_img_path13, hot_img_path14, hot_img_path15 ");
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
    
    public List<Favourite> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<Favourite> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Favourite> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }
    
    public List<Favourite> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Favourite> resultList = new ArrayList<Favourite>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Favourite row = new Favourite();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setKind(CommonUtil.toIntDefault(objectArray[1]));
                row.setOwner(CommonUtil.toIntDefault(objectArray[2]));
                row.setProductId(CommonUtil.toIntDefault(objectArray[3]));
                row.setHotId(CommonUtil.toIntDefault(objectArray[4]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[5])));
                row.setWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[5]))));
                row.setProductName(CommonUtil.toStringDefault(objectArray[6]));
                row.setProductPrice(CommonUtil.toDoubleDefault(objectArray[7]));
                row.setProductImgPath1(CommonUtil.toStringDefault(objectArray[8]));
                row.setHotTitle(CommonUtil.toStringDefault(objectArray[9]));
                row.setHotContent(CommonUtil.toStringDefault(objectArray[10]));
                row.setHotVisitCnt(CommonUtil.toIntDefault(objectArray[11]));
                row.setHotCommentCnt(CommonUtil.toIntDefault(objectArray[12]));
                row.setHotWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[13])));
                row.setHotWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[13]))));
                row.setHotImgPath1(CommonUtil.toStringDefault(objectArray[14]));
                row.setHotImgPath2(CommonUtil.toStringDefault(objectArray[15]));
                row.setHotImgPath3(CommonUtil.toStringDefault(objectArray[16]));
                row.setHotImgPath4(CommonUtil.toStringDefault(objectArray[17]));
                row.setHotImgPath5(CommonUtil.toStringDefault(objectArray[18]));
                row.setHotImgPath6(CommonUtil.toStringDefault(objectArray[19]));
                row.setHotImgPath7(CommonUtil.toStringDefault(objectArray[20]));
                row.setHotImgPath8(CommonUtil.toStringDefault(objectArray[21]));
                row.setHotImgPath9(CommonUtil.toStringDefault(objectArray[22]));
                row.setHotImgPath10(CommonUtil.toStringDefault(objectArray[23]));
                row.setHotImgPath11(CommonUtil.toStringDefault(objectArray[24]));
                row.setHotImgPath12(CommonUtil.toStringDefault(objectArray[25]));
                row.setHotImgPath13(CommonUtil.toStringDefault(objectArray[26]));
                row.setHotImgPath14(CommonUtil.toStringDefault(objectArray[27]));
                row.setHotImgPath15(CommonUtil.toStringDefault(objectArray[28]));
                row.setHotImgPaths(CommonUtil.getImgPathList(objectArray, 14, 28));
                
                DBModelUtil.processSecure(Favourite.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }

    
}