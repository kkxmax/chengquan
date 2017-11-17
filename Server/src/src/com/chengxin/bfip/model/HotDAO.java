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
public class HotDAO extends BaseDataAccessObject {

    private static String VIEW = "Hots_v";
    
    //张号类型
    public static final int HOTS_ST_UP = 1;
    public static final int HOTS_ST_DOWN = 0;
    
    
    public void insert(Hot member) {
        DBModelUtil.processSecure(Hot.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(member);
    }

    public void update(Hot member) {
        DBModelUtil.processSecure(Hot.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(member);
    }
    
    public void delete(Hot member) {
        DBModelUtil.processSecure(Hot.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(member);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Hot get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Hot ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Hot member = (Hot)list.get(0);
            
            DBModelUtil.processSecure(Hot.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return member;
        }

        return null;
    }
    
    public Hot getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Hot> resultList = this.search(filterParamObject);
        
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
            sql.appendSQL("SELECT id , title , xyleixing_id , visit_cnt, comment_cnt, elect_cnt , share_cnt , up_time , down_time "
            		+ ", content , status , write_time , xyleixing_name , status_name, xyleixing_level1_id, xyleixing_level1_name"
            		+ ", img_path1, img_path2, img_path3, img_path4, img_path5, img_path6, img_path7, img_path8, img_path9, img_path10, img_path11, img_path12, img_path13, img_path14, img_path15 ");
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
    
    public List<Hot> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<Hot> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Hot> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }
    
    public List<Hot> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Hot> resultList = new ArrayList<Hot>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Hot row = new Hot();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setTitle(CommonUtil.toStringDefault(objectArray[1]));
                row.setXyleixingId(CommonUtil.toIntDefault(objectArray[2]));
                row.setVisitCnt(CommonUtil.toIntDefault(objectArray[3]));
                row.setCommentCnt(CommonUtil.toIntDefault(objectArray[4]));
                row.setElectCnt(CommonUtil.toIntDefault(objectArray[5]));
                row.setShareCnt(CommonUtil.toIntDefault(objectArray[6]));
                row.setUpTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[7])));
                row.setDownTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[8])));
                row.setContent(CommonUtil.toStringDefault(objectArray[9]));
                row.setStatus(CommonUtil.toIntDefault(objectArray[10]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[11])));
                row.setWriteTimeString(DateTimeUtil.dateFormat(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[11]))));
                row.setXyleixingName(CommonUtil.toStringDefault(objectArray[12]));
                row.setStatusName(CommonUtil.toStringDefault(objectArray[13]));
                row.setXyleixingLevel1Id(CommonUtil.toIntDefault(objectArray[14]));
                row.setXyleixingLevel1Name(CommonUtil.toStringDefault(objectArray[15]));
                row.setImgPath1(CommonUtil.toStringDefault(objectArray[16]));
                row.setImgPath2(CommonUtil.toStringDefault(objectArray[17]));
                row.setImgPath3(CommonUtil.toStringDefault(objectArray[18]));
                row.setImgPath4(CommonUtil.toStringDefault(objectArray[19]));
                row.setImgPath5(CommonUtil.toStringDefault(objectArray[20]));
                row.setImgPath6(CommonUtil.toStringDefault(objectArray[21]));
                row.setImgPath7(CommonUtil.toStringDefault(objectArray[22]));
                row.setImgPath8(CommonUtil.toStringDefault(objectArray[23]));
                row.setImgPath9(CommonUtil.toStringDefault(objectArray[24]));
                row.setImgPath10(CommonUtil.toStringDefault(objectArray[25]));
                row.setImgPath11(CommonUtil.toStringDefault(objectArray[26]));
                row.setImgPath12(CommonUtil.toStringDefault(objectArray[27]));
                row.setImgPath13(CommonUtil.toStringDefault(objectArray[28]));
                row.setImgPath14(CommonUtil.toStringDefault(objectArray[29]));
                row.setImgPath15(CommonUtil.toStringDefault(objectArray[30]));
                row.setImgPaths(CommonUtil.getImgPathList(objectArray, 16, 30));
                
                DBModelUtil.processSecure(Hot.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }
    
}