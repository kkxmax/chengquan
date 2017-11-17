package com.chengxin.common;

import java.util.*;
import java.text.*;

public class HtmlUtil {

    public HtmlUtil() {
    }

    public static String fillHtmlTag(String html) {
        String replacedHtml = html;

        if(replacedHtml.indexOf("<html>") == -1) {
            replacedHtml = "<html>\r\n" + replacedHtml;
        }

        if(replacedHtml.indexOf("</html>") == -1) {
            replacedHtml = replacedHtml + "\r\n</html>";
        }

        return replacedHtml;
    }
    
    public static String replaceSpace(String source)
    {
        if(source == null) {return "";}

    	StringBuffer sb = new StringBuffer(source);
    	StringBuffer result = new StringBuffer();
    	String ch = null;
    	for(int i=0; i<source.length(); i++) {

    		if (Character.isSpaceChar(sb.charAt(i)))
    			ch = "&nbsp;";
    		else
    			ch = String.valueOf(sb.charAt(i));

    		result.append(ch);
    	}
    	return result.toString();
    }

    public static String replace(String line, String oldString, String newString){
        int index=0;
        while((index = line.indexOf(oldString, index)) >= 0){
        	line = line.substring(0, index) + newString + line.substring(index + oldString.length());
        	index += newString.length();
        }
        return line;
    }

    public static String parseDecimal(double unFormat, String foramt){
        DecimalFormat df = new DecimalFormat(foramt);
        String format = df.format(unFormat);
        return format;
    }
    
    public static String getPagingHTML (int currentPage, int rowCount, int pageRowCount, int pageCount){
        
        String html = "";
        
        int totalPage = 0;
        int activePage = currentPage;
        int nextPage = 0;
        
        if (rowCount <= pageRowCount) {
            totalPage = 1;
        } else if(pageRowCount == 1 || (rowCount % pageRowCount)==0) {
            totalPage = (rowCount / pageRowCount);
        } else if(rowCount != 0) {
            totalPage = (rowCount / pageRowCount) + 1;
        }
        
        currentPage = ((currentPage - 1) / pageCount) * pageCount + 1;
        nextPage = currentPage;
        String sep = "<td width=\"5px\"></td>";
        
        if(currentPage < pageCount) {
            html += "";
        } else {
            html += "<td><a href=\"javascript:movePage('1');\"><<</a></td>" + sep;
            html += "<td><a href=\"javascript:movePage('"+Integer.toString( currentPage-pageCount )+"');\"><</a></td>" + sep;
        }

        for( int i = 0; i < pageCount; i++) {
            
            if( currentPage < 1 || currentPage > totalPage ) {
                break;
            }
            
            if( activePage == currentPage) {
                html += "<td class=\"selectPage\"><a href=\"#\">" + Integer.toString(currentPage) + "</a></td>" + sep;
                
            } else {
                html += "<td><a href=\"javascript:movePage('"+Integer.toString( currentPage )+"');\">" + Integer.toString(currentPage) + "</a></td>" + sep;
            }
            
            currentPage++;
        }
        
        if(currentPage > totalPage) {
            html += "";
        } else {
            html += "<td><a href=\"javascript:movePage('"+Integer.toString( nextPage+pageCount )+"');\">></a></td>" + sep;
            html += "<td><a href=\"javascript:movePage('"+Integer.toString( totalPage )+"');\">>></a></td>";
        }

        return html;
    }

    public static String createInputHiddenString(Map<String, String> map) {
        String inputString = new String();
        Iterator<String> iterator = map.keySet().iterator();

        while(iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            inputString += "<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />\n";
        }

        return inputString;
    }

    public static String createHiddenString(Map<String, String> map) {
        String inputString = new String();
        Iterator<String> iterator = map.keySet().iterator();

        while(iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            inputString += "&" + key + "=" + value;
        }

        return inputString;
    }
}
