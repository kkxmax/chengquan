
package com.chengxin.common;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.multiaction.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ControllerAdvice extends HandlerInterceptorAdapter {

    private CachedObjectService cachedObjectService = null;

    public void setCachedObjectService(CachedObjectService val) {this.cachedObjectService = val;}

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        
        request.getSession();
        
        boolean next = true;

        request.setAttribute(AppSettings.C_SYSTEM_TITLE, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_SYSTEM_TITLE));
        request.setAttribute(AppSettings.C_SYSTEM_URL, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_SYSTEM_URL));
        
        request.setAttribute(AppSettings.C_ASSETS_PATH, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_ASSETS_PATH));
        request.setAttribute(AppSettings.C_UPLOAD_PATH, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_UPLOAD_PATH));
        request.setAttribute(AppSettings.C_EMPTY_HOT_IMAGE, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_EMPTY_HOT_IMAGE));
        
        request.setAttribute(AppSettings.C_COMMON_FILE_SAVE_PATH, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_COMMON_FILE_SAVE_PATH));
        request.setAttribute(AppSettings.C_COMMON_FILE_PATH_SEP, cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, AppSettings.C_COMMON_FILE_PATH_SEP));
                
        
        return next;
    }
}
