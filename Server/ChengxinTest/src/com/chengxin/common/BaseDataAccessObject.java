/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chengxin.common;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.*;
import org.hibernate.*;
import org.springframework.orm.hibernate3.*;
import org.springframework.orm.hibernate3.support.*;


public class BaseDataAccessObject extends HibernateDaoSupport {
    protected CachedObjectService cachedObjectService = null;

    public void setCachedObjectService(CachedObjectService value) {this.cachedObjectService = value;}

    protected String getSetting(String settingName) throws Exception {
        return cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, settingName);
    }

    protected String getBlankValue(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value.length() == 0) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
