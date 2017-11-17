package com.chengxin.common;

import java.util.Map;

public class ValidationUtil {

	public static boolean isBlank(String str) {
		if(str == null) {
			return true;
		}

		if(str.equals("")) {
			return true;
		}

		return false;
	}

	public static boolean isBlank(String parameterName, Map<String, String> parameterMap) {
		if(!parameterMap.containsKey(parameterName)) {
			return true;
		}

		String value = (String)parameterMap.get(parameterName);

		return isBlank(value);
	}

	public static boolean isBlankDate(String str) {
		if(isBlank(str)) {
			return true; 
		}

		if(str.equals("--")) {
			return true;
		}

		return false;
	}

	public static boolean isBlankDate(String parameterName, Map<String, String> parameterMap) {
		if(!parameterMap.containsKey(parameterName)) {
			return true;
		}

		String value = (String)parameterMap.get(parameterName);

		return isBlankDate(value);
	}

	public static boolean isBlankArray(String str, String splitter) {
		if(isBlank(str)) {
			return true;
		}

		if(str.split(splitter).length == 0) {
			return true;
		}

		return false;
	}

	public static boolean isBlankArray(String parameterName, String splitter, Map<String, String> parameterMap) {
		if(!parameterMap.containsKey(parameterName)) {
			return true;
		}

		String value = (String)parameterMap.get(parameterName);

		return isBlankArray(value, splitter);
	}
}
