
package com.chengxin.common;

public class KeyValueObject {
    public static final String C_DATA_TYPE_ARRAY = "C_DATA_TYPE_ARRAY";
    public static final String C_DATA_TYPE_STRING = "C_DATA_TYPE_STRING";
    
    private String key;
    private Object value;
    private String dataType = C_DATA_TYPE_STRING;

    public KeyValueObject(){}
    public KeyValueObject(String key, Object value) {
        this.key = key;
        this.value = value;
    }
    
    public KeyValueObject(String key, Object value, String dataType) {
        this.key = key;
        this.value = value;
        this.dataType = dataType;
    }

    public String getKey(){return this.key;}
    public void setKey(String val){this.key = val;}

    public Object getValue(){return this.value;}
    public void setValue(Object val){this.value = val;}
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
}

