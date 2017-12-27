/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chengxin.bfip.manage;

/**
 *
 * @author Administrator
 */
public class ServiceResult {
    private boolean isError;
    private String errorMessage;
    
    public ServiceResult(boolean isError, String errorMessage) {
        this.isError = isError;
        this.errorMessage = errorMessage;
    }
    
    public boolean IsError() { return isError; }
    public String getErrorMessage() { return errorMessage; }
}
