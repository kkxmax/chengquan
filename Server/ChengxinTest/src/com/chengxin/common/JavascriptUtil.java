
package com.chengxin.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
public class JavascriptUtil {
    private static String TEMPLATE = "javascript";

    public static String RELOAD = "opener.location.reload(true);\n";
    public static String CLOSE = "if(navigator.appVersion.indexOf('MSIE 6') >= 0 ) {window.opener = self;self.close();} else {window.open('about:blank','_self').close();}";

    public static ModelAndView Write(HttpServletRequest request, HttpServletResponse response, String scripts) {
        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView Close(HttpServletRequest request, HttpServletResponse response, String message, String url) {

        String scripts = CommonType.C_STRING_NULL;

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }

        scripts += CLOSE;

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView Close(HttpServletRequest request, HttpServletResponse response, String localeCode,
            String message_EN, String message_KR, String message_CN, String url) {

        String scripts = CommonType.C_STRING_NULL;

        String message = "";
        
        message = message_EN;
        
        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }

        scripts += CLOSE;

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView CloseRefresh(HttpServletRequest request, HttpServletResponse response, String localeCode,
            String message_EN, String message_KR, String message_CN) {

        String scripts = CommonType.C_STRING_NULL;

        String message = "";
        
        message = message_EN;

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        scripts += RELOAD;
        scripts += CLOSE;

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView Message(HttpServletRequest request, HttpServletResponse response, String prefix,
        String suffix, String message) {

        String scripts = CommonType.C_STRING_NULL;

        scripts += prefix + "\n";

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        scripts += suffix + "\n";

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView MessageMove(HttpServletRequest request, HttpServletResponse response, String prefix,
        String suffix, String message, String url) {

        String scripts = CommonType.C_STRING_NULL;

        scripts += prefix + "\n";

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }

        scripts += suffix + "\n";

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView MessageMove(HttpServletRequest request, HttpServletResponse response, String message,
            String url) {

        String scripts = CommonType.C_STRING_NULL;

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "toastr['error']('" + message + "')\n";
        }

        scripts += "setTimeout(function() {";
        
        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }
        
        scripts += "}, 2000);";
        
        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }
    
    public static ModelAndView MessageMove(HttpServletRequest request, HttpServletResponse response, String localeCode,
            String message_EN, String message_KR, String message_CN, String url) {

        String scripts = CommonType.C_STRING_NULL;
        
        String message = "";
        
        message = message_EN;

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }
        
        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView MessageMove(HttpServletRequest request, HttpServletResponse response, String message,
            String url, String close) {

        String scripts = CommonType.C_STRING_NULL;

        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
            scripts += close;
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "location.href='" + url + "'\n";
        }

        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }

    public static ModelAndView MessageMovePost(HttpServletRequest request, HttpServletResponse response, String message,
            String url) {

        String scripts = CommonType.C_STRING_NULL;
        String fromInputArray = CommonType.C_STRING_NULL;
        
        if (!message.equals(CommonType.C_STRING_NULL)) {
            scripts += "alert('" + message + "')\n";
        }

        if (url.equals(CommonType.C_STRING_NULL)) {
            scripts += "history.back();\n";
        } else {
            scripts += "function init() {\n";
            scripts += "    with(document.frmMove) {\n";
            scripts += "        submit();\n";
            scripts += "    }\n";
            scripts += "}\n";
            scripts += "window.onload = init;";
            
            String[] urlArray = url.split("\\?");
            String[] inputArray = urlArray[1].split("\\&");

            fromInputArray += "<form name=\"frmMove\" method=\"post\" action=\"" + urlArray[0] + "\" >\n";
                    
            for(int i = 0; i < inputArray.length; i++) {
                String[] input = inputArray[i].split("=");
                
                fromInputArray += "<input type=\"hidden\" name=\"" + input[0] + "\" value=\"" + input[1] + "\" />\n";
            }

            fromInputArray += "</form>";
            
            request.setAttribute("INPUT_HIDDEN_ARRAY", fromInputArray);
        }
        
        request.setAttribute("SCRIPTS", scripts);

        return new ModelAndView(TEMPLATE);
    }
}
