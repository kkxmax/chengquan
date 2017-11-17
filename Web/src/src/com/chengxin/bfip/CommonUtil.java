package com.chengxin.bfip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.City;
import com.chengxin.bfip.model.CityDAO;
import com.chengxin.bfip.model.Interest;
import com.chengxin.bfip.model.InterestDAO;

public class CommonUtil {

	public CommonUtil() {
	}

	public static String toStringDefault(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static int toIntDefault(Object obj) {
		return obj == null ? 0 : Integer.valueOf(obj.toString());
	}

	public static Double toDoubleDefault(Object obj) {
		return obj == null ? 0 : Double.valueOf(obj.toString());
	}

	public static String getRepositoryRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("WEB-INF");
	}
	
	public static String toArrayStringDefault(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static int toArrayIntDefault(Object obj) {
		return obj == null ? 0 : Integer.valueOf(obj.toString());
	}
	
	public static String genCode(CityDAO cityDao, AccountDAO accountDao, int cityId) {
		
		City city = cityDao.getDetail(cityId);
		Calendar calendar = new GregorianCalendar();
		String strPrefix = "C" + String.valueOf(calendar.get(Calendar.YEAR)).substring(3, 4) 
				+ city.getProvinceCode() 
				+ (String.valueOf(calendar.get(Calendar.MONTH)).length() == 1 ? "0" + calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH))
				+ (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH));
		int nCode = accountDao.count(null, "code like '" + strPrefix + "%'");
		Logger.getLogger("bfip").log(Level.INFO, strPrefix);
		Logger.getLogger("bfip").log(Level.INFO, String.valueOf(nCode));
		
		return strPrefix + String.valueOf(nCode + 1);
	}

	public static List<Integer> getInterestIds(InterestDAO interestDao, int id) {
		
		List<Interest> myInterests = interestDao.search(null, "owner=" + id);
		List<Integer> result = new ArrayList<Integer>();
		for(Interest item : myInterests) {
			result.add(item.getTarget());
		}
		
		return result;
	}
}
