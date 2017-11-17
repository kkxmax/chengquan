package com.chengxin.common;

public class BinaryFormParameter {
    private String name = "";
    private boolean isTextField = true;
    private Object value = null;

    public BinaryFormParameter() {}

    public BinaryFormParameter(String name, boolean isTextField, Object value) {
        this.name = name;
        this.isTextField = isTextField;
        this.value = value;
    }

    public String getName() {return this.name;}
    public void setName(String val) {this.name = val;}

    public boolean getIsTextField() {return this.isTextField;}
    public void setIsTextField(boolean val) {this.isTextField = val;}

    public Object getValue() {return this.value;}
    public void setValue(Object val) {this.value = val;}
}
