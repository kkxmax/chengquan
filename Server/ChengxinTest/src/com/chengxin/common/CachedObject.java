/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chengxin.common;

public class CachedObject {

    private String typeName;
    private Object object;
    private String cachedDateTime;
    
    public CachedObject(String typeName, Object object, String cachedDateTime) {
        this.typeName = typeName;
        this.object = object;
        this.cachedDateTime = cachedDateTime;
    }    
    
    public String getTypeName() { return typeName; }
    public Object getObject() { return object; }
    public String getCachedDateTime() { return cachedDateTime; }
}
