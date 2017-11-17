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
public class AccountDAO extends BaseDataAccessObject {

    private static String VIEW = "Accounts_v";
    
    // 账号类型
    public static final int ACCOUNT_TYPE_PERSONAL = 1;    // 个人账户
    public static final int ACCOUNT_TYPE_ENTERPRISE = 2;  // 企业账户

    // 企业类型
    public static final int ENTER_TYPE_PERSONAL = 1;      // 企业
    public static final int ENTER_TYPE_ENTERPRISE = 2;    // 个体户

    // 审核状态
    public static final int TEST_ST_READY = 1;            // 审核中
    public static final int TEST_ST_PASSED = 2;           // 审核通过
    public static final int TEST_ST_UNPASSED = 3;         // 审核未通过

    // 禁用状态
    public static final int BAN_ST_UNBANED = 1;             // 未禁用
    public static final int BAN_ST_BANED = 2;           // 已禁用
    
    
    public void insert(Account member) {
        DBModelUtil.processSecure(Account.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(member);
    }

    public void update(Account member) {
        DBModelUtil.processSecure(Account.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(member);
    }
    
    public void delete(Account member) {
        DBModelUtil.processSecure(Account.class.getName(), member, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(member);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Account get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Account ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Account member = (Account)list.get(0);
            
            DBModelUtil.processSecure(Account.class.getName(), member, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return member;
        }

        return null;
    }
    
    public Account getDetail(int id) {
        
        JSONObject filterParamObject = new JSONObject();
        JSONArray equalParamArray = new JSONArray();
        JSONObject equalParam = new JSONObject();
        
        equalParam.put("id", id);
        equalParamArray.add(equalParam);
        filterParamObject.put("equal_param", equalParamArray);
        
        List<Account> resultList = this.search(filterParamObject);
        
        if(resultList.size() > 0) {
        	return resultList.get(0);
        }
        else {
        	return null;	
        }
    }
    
    public Account getDetail(String where) {
        
        List<Account> resultList = this.search(null, where);
        
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
            sql.appendSQL("SELECT id, mobile, password, akind, code, view_cnt, elect_cnt, logo, enter_kind, enter_name"
            		+ ", xyleixing_id, addr, weburl, weixin, main_job, cert_image "
            		+ ", comment, recommend, boss_name, boss_job, boss_mobile, boss_weixin"
            		+ ", test_status, ban_status, write_time, test_status_name, ban_status_name, enter_kind_name"
            		+ ", realname, token, reqcode_id, friend1, friend2, friend3, city_id, city_name, province_id, province_name"
            		+ ", job, experience, history, cert_num, enter_cert_num, enter_cert_image, xy_name"
            		+ ", req_code_sender_id, req_code_sender_realname, req_code_sender_enter_name ");
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
    
    public List<Account> search(JSONObject filterParamObject) {
    	return search(filterParamObject, "");
    }
    
    public List<Account> search(JSONObject filterParamObject, String extraWhere) {
    	return search(filterParamObject, extraWhere, "");
    }
    
    public List<Account> search(JSONObject filterParamObject, String extraWhere, String extraOrder) {
    	return search(filterParamObject, extraWhere, extraOrder, "");
    }
    
    public List<Account> search(JSONObject filterParamObject, String extraWhere, String extraOrder, String groupby) {
    
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        SQLWithParameters sql = _makeSearchQuery(false, filterParamObject, extraWhere, extraOrder, groupby);

        Query query = session.createSQLQuery(sql.getSQL());
        
        DBModelUtil.fillParameter(sql, query);

        List queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        List<Account> resultList = new ArrayList<Account>();
        
        if (queryList != null) {
            int listSize = queryList.size();
            
            for(int i = 0; i < listSize; i++) {
                Object[] objectArray = (Object[])queryList.get(i);
                
                Account row = new Account();
                
                row.setId(CommonUtil.toIntDefault(objectArray[0]));
                row.setMobile(CommonUtil.toStringDefault(objectArray[1]));
                row.setPassword(CommonUtil.toStringDefault(objectArray[2]));
                row.setAkind(CommonUtil.toIntDefault(objectArray[3]));
                row.setCode(CommonUtil.toStringDefault(objectArray[4]));
                row.setViewCnt(CommonUtil.toIntDefault(objectArray[5]));
                row.setElectCnt(CommonUtil.toIntDefault(objectArray[6]));
                row.setLogo(CommonUtil.toStringDefault(objectArray[7]));
                row.setEnterKind(CommonUtil.toIntDefault(objectArray[8]));
                row.setEnterName(CommonUtil.toStringDefault(objectArray[9]));
                row.setXyleixingId(CommonUtil.toIntDefault(objectArray[10]));
                row.setAddr(CommonUtil.toStringDefault(objectArray[11]));
                row.setWeburl(CommonUtil.toStringDefault(objectArray[12]));
                row.setWeixin(CommonUtil.toStringDefault(objectArray[13]));
                row.setMainJob(CommonUtil.toStringDefault(objectArray[14]));
                row.setCertImage(CommonUtil.toStringDefault(objectArray[15]));
                row.setComment(CommonUtil.toStringDefault(objectArray[16]));
                row.setRecommend(CommonUtil.toStringDefault(objectArray[17]));
                row.setBossName(CommonUtil.toStringDefault(objectArray[18]));
                row.setBossJob(CommonUtil.toStringDefault(objectArray[19]));
                row.setBossMobile(CommonUtil.toStringDefault(objectArray[20]));
                row.setBossWeixin(CommonUtil.toStringDefault(objectArray[21]));
                row.setTestStatus(CommonUtil.toIntDefault(objectArray[22]));
                row.setBanStatus(CommonUtil.toIntDefault(objectArray[23]));
                row.setWriteTime(DateTimeUtil.stringToDate(CommonUtil.toStringDefault(objectArray[24])));
                row.setTestStatusName(CommonUtil.toStringDefault(objectArray[25]));
                row.setBanStatusName(CommonUtil.toStringDefault(objectArray[26]));
                row.setEnterKindName(CommonUtil.toStringDefault(objectArray[27]));
                row.setRealname(CommonUtil.toStringDefault(objectArray[28]));
                row.setToken(CommonUtil.toStringDefault(objectArray[29]));
                row.setReqCodeId(CommonUtil.toIntDefault(objectArray[30]));
                row.setFriend1(CommonUtil.toStringDefault(objectArray[31]));
                row.setFriend2(CommonUtil.toStringDefault(objectArray[32]));
                row.setFriend3(CommonUtil.toStringDefault(objectArray[33]));
                row.setCityId(CommonUtil.toIntDefault(objectArray[34]));
                row.setCityName(CommonUtil.toStringDefault(objectArray[35]));
                row.setProvinceId(CommonUtil.toIntDefault(objectArray[36]));
                row.setProvinceName(CommonUtil.toStringDefault(objectArray[37]));
                row.setJob(CommonUtil.toStringDefault(objectArray[38]));
                row.setExperience(CommonUtil.toStringDefault(objectArray[39]));
                row.setHistory(CommonUtil.toStringDefault(objectArray[40]));
                row.setCertNum(CommonUtil.toStringDefault(objectArray[41]));
                row.setEnterCertNum(CommonUtil.toStringDefault(objectArray[42]));
                row.setEnterCertImage(CommonUtil.toStringDefault(objectArray[43]));
                row.setXyName(CommonUtil.toStringDefault(objectArray[44]));
                row.setReqCodeSenderId(CommonUtil.toIntDefault(objectArray[45]));
                row.setReqCodeSenderRealname(CommonUtil.toStringDefault(objectArray[46]));
                row.setReqCodeSenderEnterName(CommonUtil.toStringDefault(objectArray[47]));
                
                DBModelUtil.processSecure(Account.class.getName(), row, DBModelUtil.C_SECURE_TYPE_DECRYPT);

                resultList.add(row);
            }
        }
        
        transaction.commit();
        
        if(session.isOpen()) {session.close();}
        
        return resultList;
    }

    
}