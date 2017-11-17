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
public class ProvinceDAO extends BaseDataAccessObject {
    
    public void insert(Province province) {
        DBModelUtil.processSecure(Province.class.getName(), province, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().save(province);
    }

    public void update(Province province) {
        DBModelUtil.processSecure(Province.class.getName(), province, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().update(province);
    }
    
    public void delete(Province province) {
        DBModelUtil.processSecure(Province.class.getName(), province, DBModelUtil.C_SECURE_TYPE_ENCRYPT);
        
        this.getHibernateTemplate().delete(province);
    }
    
    public void delete(int id) {
        this.getHibernateTemplate().delete(get(id));
    }
    
    public Province get(int id) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("FROM Province ");
        stringBuffer.append("WHERE id = :id ");

        List list = (List)this.getHibernateTemplate().findByNamedParam(
                    stringBuffer.toString(),
                    new String[]{"id"},
                    new Object[]{id});

        stringBuffer = null;

        if (list.size() > 0) {
        	Province province = (Province)list.get(0);
            
            DBModelUtil.processSecure(Province.class.getName(), province, DBModelUtil.C_SECURE_TYPE_DECRYPT);
            
            return province;
        }

        return null;
    }
    
    public List<Province> search() {
        
        Session session = SessionFactoryUtils.getNewSession(this.getHibernateTemplate().getSessionFactory());
        Transaction transaction = session.beginTransaction();
        
        Query query = session.createQuery("from Province");
        
        List<Province> queryList = null; try { queryList = query.list(); } catch(Exception e) {e.printStackTrace();}
        
        transaction.commit();
        if(session.isOpen()) {session.close();}
        
        return queryList;
    }
    
}