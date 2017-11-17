package com.chengxin.common;

import java.util.*;

public class SQLWithParameters {
    private StringBuffer sqlBuffer = new StringBuffer();
    private List<KeyValueObject> parameterList = new ArrayList<KeyValueObject>();
    
    public SQLWithParameters(String sql) {
        this.sqlBuffer.append(sql);
    }
    
    public void addParameter(KeyValueObject kvo) {
        this.parameterList.add(kvo);
    }
    
    public void appendSQL(String str) {
        sqlBuffer.append(str);
    }
    
    public String getSQL() {
        return sqlBuffer.toString();
    }
    
    public List<KeyValueObject> getParameterList() {
        return parameterList;
    }
}
