
package com.chengxin.common;

public class KeyValueString {
    private String key;
    private String value;

    public KeyValueString(){}
    public KeyValueString(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey(){return this.key;}
    public void setKey(String val){this.key = val;}

    public String getValue(){return this.value;}
    public void setValue(String val){this.value = val;}
}

