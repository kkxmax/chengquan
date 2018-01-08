package com.chengxin.bfip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.chengxin.bfip.model.Account;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.City;
import com.chengxin.bfip.model.CityDAO;
import com.chengxin.bfip.model.Elect;
import com.chengxin.bfip.model.ElectDAO;
import com.chengxin.bfip.model.ErrorDAO;
import com.chengxin.bfip.model.Estimate;
import com.chengxin.bfip.model.EstimateDAO;
import com.chengxin.bfip.model.Favourite;
import com.chengxin.bfip.model.FavouriteDAO;
import com.chengxin.bfip.model.Hot;
import com.chengxin.bfip.model.Interest;
import com.chengxin.bfip.model.InterestDAO;
import com.chengxin.bfip.model.ItemDAO;
import com.chengxin.bfip.model.MarkLog;
import com.chengxin.bfip.model.MarkLogDAO;
import com.chengxin.bfip.model.Notice;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.bfip.model.Pleixing;
import com.chengxin.bfip.model.PleixingDAO;
import com.chengxin.bfip.model.Product;
import com.chengxin.bfip.model.ProductDAO;
import com.chengxin.bfip.model.ServiceDAO;
import com.chengxin.bfip.model.XingyeWatch;
import com.chengxin.bfip.model.XingyeWatchDAO;
import com.chengxin.bfip.model.XingyeWatched;
import com.chengxin.bfip.model.XingyeWatchedDAO;
import com.chengxin.bfip.model.Xyleixing;
import com.chengxin.bfip.model.XyleixingDAO;

public class CommonUtil {

	public CommonUtil() {
	}

	public static String toStringDefault(Object obj) {
		if(obj == null) {
			return "";
		}
		else if(obj.toString().equals("null")) {
			return "null ";
		}
		else {
			return obj.toString();
		}
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
	
	public static String genRand6String() {
		return String.valueOf((new Random().nextInt(899999) + 100000));
	}
	
	public static String genVerifyCode() {
		return genRand6String();
	}
	
	public static String genReqCode() {
		return genRand6String();
	}
	
	public static void logGlobal(HttpServletRequest request, String content) {
		File file = new File(getRepositoryRealPath(request), Constants.LOG_FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream oFile = new FileOutputStream(file, true);
				oFile.write(content.getBytes("UTF-8"));
				oFile.write("\n".getBytes("UTF-8"));
		        oFile.flush();
		        oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void log(HttpServletRequest request, String logPath, String logFileName, String content) {
		
		String realPath = getRepositoryRealPath(request) + Constants.C_COMMON_FILE_PATH_SEP + "logs" + Constants.C_COMMON_FILE_PATH_SEP + logPath;
		File dir = new File(realPath); 
		try {
			if(!dir.exists()) {
				if(!dir.mkdirs()) {
					logGlobal(request, String.format("mkdirs() failed ---- logPath : %s, logFileName : %s", logPath, logFileName));
					return;
				}
			}
			File file = new File(realPath, logFileName);
			if(!file.exists()) {
				if(!file.createNewFile()) {
					logGlobal(request, String.format("createNewFile() failed ---- logPath : %s, logFileName : %s", logPath, logFileName));
					return;
				}
			}
			
			FileOutputStream oFile = new FileOutputStream(file, true);
			oFile.write(content.getBytes("UTF-8"));
			oFile.write("\n".getBytes("UTF-8"));
	        oFile.flush();
	        oFile.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			logGlobal(request, String.format("log() failed ---- logPath : %s, logFileName : %s", logPath, logFileName));
		}
		
	}
	
	public static String getIpAddr(HttpServletRequest request) {      
	   String ip = request.getHeader("x-forwarded-for");      
	   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	       ip = request.getHeader("Proxy-Client-IP");      
	   }      
	   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	       ip = request.getHeader("WL-Proxy-Client-IP");      
	   }      
	   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	       ip = request.getRemoteAddr();      
	   }      
	   return ip;      
	}
		
	public static void writeFile(String filePath, String fileName, String content) throws IOException {
		File file = new File(filePath, fileName);
		if(!file.exists()) {
			file.createNewFile();	
		}
		
		FileOutputStream oFile = new FileOutputStream(file, false);
		oFile.write(content.getBytes("UTF-8"));
        oFile.flush();
        oFile.close();
	}
	
	public static String readFile(String filePath) {
		String result = "";
		File file = new File(filePath);
		byte[] buf = new byte[1024];
		if(!file.exists()) {
			return "";
		}
		
		FileInputStream fis = null;
        
		try {
            fis = new FileInputStream(file);
            fis.read(buf);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		try {
			result = new String(buf, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return result;
	}
	
	public static String genAccountCode(CityDAO cityDao, AccountDAO accountDao, int cityId) {
		
		City city = cityDao.getDetail(cityId);
		Calendar calendar = new GregorianCalendar();
		String strPrefix = "C" + String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4) 
				+ city.getCode();
		String strDate = (String.valueOf(calendar.get(Calendar.MONTH)).length() == 1 ? "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1) : String.valueOf(calendar.get(Calendar.MONTH) + 1))
				+ (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH));
		
		String strCode = "001";
		List<Account> lastMatchCodes = accountDao.search(null, "substr(code from 4 for 6) = '" + city.getCode() + strDate + "'", "substr(code from 10) desc");
		if(lastMatchCodes.size() > 0) {
			strCode = String.valueOf(Integer.valueOf(lastMatchCodes.get(0).getCode().substring(9)) + 1);
		}
		
		if(strCode.length() < 3) {
			int len = strCode.length();
			for(int i=0; i<3-len; i++) {
				strCode = "0" + strCode;
			}
		}
		return strPrefix + strDate + strCode;
	}
	
	public static String genProductCode(ProductDAO productDao) {
		
		int nCode = productDao.count(null);
		
		return "CP05918" + String.valueOf(nCode + 1);
	}
	
	public static String genItemCode(ItemDAO itemDao) {
		
		int nCode = itemDao.count(null);
		
		return "XM05918" + String.valueOf(nCode + 1);
	}
	
	public static String genServiceCode(ServiceDAO serviceDao) {
		
		int nCode = serviceDao.count(null);
		
		return "FW05918" + String.valueOf(nCode + 1);
	}

	public static List<Integer> getInterestIds(InterestDAO interestDao, int id) {
		
		List<Interest> myInterests = interestDao.search(null, "owner=" + id);
		List<Integer> result = new ArrayList<Integer>();
		for(Interest item : myInterests) {
			result.add(item.getTarget());
		}
		
		return result;
	}
	
	public static String getInterestIdsStr(InterestDAO interestDao, int id) {
		
		List<Interest> myInterests = interestDao.search(null, "owner=" + id);
		String result = "";
		for(Interest item : myInterests) {
			if(!result.isEmpty()) {
				result += ",";
			}
			result += item.getTarget();
		}
		
		return result;
	}
	
	public static List<Integer> getStarInterestIds(InterestDAO interestDao, int id) {
		
		List<Interest> myInterests = interestDao.search(null, "owner=" + id + " and is_star=1");
		List<Integer> result = new ArrayList<Integer>();
		for(Interest item : myInterests) {
			result.add(item.getTarget());
		}
		
		return result;
	}
	
	public static List<Integer> getFavouriteIds(FavouriteDAO favouriteDao, int id, int kind) {
		
		List<Favourite> myFavourites = favouriteDao.search(null, "owner=" + id + " and kind=" + kind);
		List<Integer> result = new ArrayList<Integer>();
		for(Favourite item : myFavourites) {
			if(kind == FavouriteDAO.FAVOURITE_KIND_PRODUCT) {
				result.add(item.getProductId());	
			}
			else if(kind == FavouriteDAO.FAVOURITE_KIND_HOT) {
				result.add(item.getHotId());
			}
		}
		
		return result;
	}
	
	// returns both level 1 and level 2
	public static String getSubXyIds(XyleixingDAO xyleixingDao, int xyleixingId) {
		
		String result = "";
		
		if(xyleixingId != 0) {
			result = String.valueOf(xyleixingId);
		}
		List<Xyleixing> xys = xyleixingDao.search(null, "upper_id=" + xyleixingId);
		
		for(Xyleixing item : xys) {
			if(xyleixingId == 0) {
				result += "," + getSubXyIds(xyleixingDao, item.getId());
			}
			else {
				result += "," + item.getId();	
			}
		}
		
		if(xyleixingId == 0) {
			result = result.substring(1);
		}
		return result;
	}
	
	public static String getSubXyIds(XyleixingDAO xyleixingDao, String[] xyleixingIds) {
		
		String result = "";
		
		for(String item : xyleixingIds) {
			if(item.isEmpty()) {
				continue;
			}
			if(!item.equals("0")) {
				if(!result.isEmpty()) {
					result += ",";
				}
				result += item;	
			}
			
			List<Xyleixing> xys = xyleixingDao.search(null, "upper_id=" + item);
			
			for(Xyleixing xy : xys) {
				if(item.equals("0")) {
					if(!result.isEmpty()) {
						result += ",";
					}
					result += getSubXyIds(xyleixingDao, xy.getId());
				}
				else {
					if(!result.isEmpty()) {
						result += ",";
					}
					result += xy.getId();	
				}
			}
		}
		return result;
	}
	
	// returns both level 1 and level 2
	public static String getSubPIds(PleixingDAO pleixingDao, int pleixingId) {
			
		String result = "";
		
		if(pleixingId != 0) {
			result = String.valueOf(pleixingId);
		}
		List<Pleixing> ps = pleixingDao.search(null, "upper_id=" + pleixingId);
		
		for(Pleixing item : ps) {
			if(pleixingId == 0) {
				result += "," + getSubPIds(pleixingDao, item.getId());
			}
			else {
				result += "," + item.getId();	
			}
		}
		
		if(pleixingId == 0) {
			result = result.substring(1);
		}
		return result;
	}
	
	public static String getSubPIds(PleixingDAO pleixingDao, String[] pleixingIds) {
		
		String result = "";
		
		for(String item : pleixingIds) {
			if(item.isEmpty()) {
				continue;
			}
			if(!item.equals("0")) {
				if(!result.isEmpty()) {
					result += ",";
				}
				result += item;	
			}
			
			List<Pleixing> ps = pleixingDao.search(null, "upper_id=" + item);
			
			for(Pleixing p : ps) {
				if(item.equals("0")) {
					if(!result.isEmpty()) {
						result += ",";
					}
					result += getSubPIds(pleixingDao, p.getId());
				}
				else {
					if(!result.isEmpty()) {
						result += ",";
					}
					result += p.getId();	
				}
			}
		}
		
		return result;
	}

	public static List<Integer> getXingyeWatchArray(XingyeWatchDAO xingyeWatchDao, int accountId) {
		
		List<Integer> result = new ArrayList<Integer>();
		
		List<XingyeWatch> watchList = xingyeWatchDao.search(null, "account_id=" + accountId);
		
		for(XingyeWatch item : watchList) {
			
			result.add(item.getXyleixingId());
		}
		
		return result;
	}

	public static String getXingyeWatchs(XingyeWatchDAO xingyeWatchDao, int accountId) {
		
		String result = "";
		
		List<XingyeWatch> watchList = xingyeWatchDao.search(null, "account_id=" + accountId);
		
		for(XingyeWatch item : watchList) {
			if(!result.isEmpty()) {
				result += ",";
			}
			result += item.getXyleixingId();
		}
		
		return result;
	}
	
	public static List<Integer> getXingyeWatchedArray(XingyeWatchedDAO xingyeWatchedDao, int accountId) {
		
		List<Integer> result = new ArrayList<Integer>();
		
		List<XingyeWatched> watchedList = xingyeWatchedDao.search(null, "account_id=" + accountId);
		
		for(XingyeWatched item : watchedList) {
			
			result.add(item.getXyleixingId());
		}
		
		return result;
	}

	public static String getXingyeWatcheds(XingyeWatchedDAO xingyeWatchedDao, String xyleixingIds) {
		
		String result = "";
		
		if(xyleixingIds.isEmpty()) {
			return result;
		}
		
		List<XingyeWatched> watchedList = xingyeWatchedDao.search(null, "xyleixing_id in (" + xyleixingIds + ")");
		
		for(XingyeWatched item : watchedList) {
			if(!result.isEmpty()) {
				result += ",";
			}
			result += item.getAccountId();
		}
		
		return result;
	}
	
	public static void setInterestStatus(InterestDAO interestDao, int accountId, Account account) {
		List<Integer> myInterestArray = CommonUtil.getInterestIds(interestDao, accountId);
    	List<Integer> myStarInterestArray = CommonUtil.getStarInterestIds(interestDao, accountId);
    	
    	if(myInterestArray.contains(account.getId())) {
    		account.setInterested(1);
		}
		if(myStarInterestArray.contains(account.getId())) {
			account.setIsStarInterested(1);
		}
	}
	
	public static void setInterestStatus(InterestDAO interestDao, int accountId, List<Account> accountList) {
		List<Integer> myInterestArray = CommonUtil.getInterestIds(interestDao, accountId);
    	List<Integer> myStarInterestArray = CommonUtil.getStarInterestIds(interestDao, accountId);
    	
    	for(Account item : accountList) {
    		if(myInterestArray.contains(item.getId())) {
    			item.setInterested(1);
    		}
    		if(myStarInterestArray.contains(item.getId())) {
    			item.setIsStarInterested(1);
    		}
    	}
	}
	
	public static void setProductFavouriteStatus(FavouriteDAO favouriteDao, int accountId, Product product) {
		List<Integer> myFavouriteProductArray = CommonUtil.getFavouriteIds(favouriteDao, accountId, FavouriteDAO.FAVOURITE_KIND_PRODUCT);
    	
		if(myFavouriteProductArray.contains(product.getId())) {
			product.setIsFavourite(1);
		}
	}
	
	public static void setProductFavouriteStatus(FavouriteDAO favouriteDao, int accountId, List<Product> productList) {
		List<Integer> myFavouriteProductArray = CommonUtil.getFavouriteIds(favouriteDao, accountId, FavouriteDAO.FAVOURITE_KIND_PRODUCT);
    	
    	for(Product item : productList) {
    		if(myFavouriteProductArray.contains(item.getId())) {
    			item.setIsFavourite(1);
    		}
    	}
	}
	
	public static List<Integer> getElectIds(ElectDAO electDao, int accountId, int kind) {
		List<Elect> myElects = electDao.search(null, "owner=" + accountId + " and kind=" + kind);
		List<Integer> result = new ArrayList<Integer>();
		for(Elect item : myElects) {
			if(kind == ElectDAO.ELECT_KIND_PERSONAL_OR_ENTER) {
				result.add(item.getAccountId());	
			}
			else if(kind == ElectDAO.ELECT_KIND_HOT) {
				result.add(item.getHotId());
			}
			else if(kind == ElectDAO.ELECT_KIND_ESTIMATE) {
				result.add(item.getEstimateId());
			}
		}
		return result;
	}
	
	public static void setHotElectStatus(ElectDAO electDao, int accountId, List<Hot> hotList) {
		
		List<Integer> myElectHotArray = CommonUtil.getElectIds(electDao, accountId, ElectDAO.ELECT_KIND_HOT);
    	
    	for(Hot item : hotList) {
    		if(myElectHotArray.contains(item.getId())) {
    			item.setIsElectedByMe(1);
    		}
    	}
	}
	
	public static void setHotElectStatus(ElectDAO electDao, int accountId, Hot hot) {
		
		List<Integer> myElectHotArray = CommonUtil.getElectIds(electDao, accountId, ElectDAO.ELECT_KIND_HOT);
    	
		if(myElectHotArray.contains(hot.getId())) {
			hot.setIsElectedByMe(1);
		}
	}
	
	public static void setEstimateElectStatus(ElectDAO electDao, int accountId, List<Estimate> estimateList) {
		
		List<Integer> myElectEstimateArray = CommonUtil.getElectIds(electDao, accountId, ElectDAO.ELECT_KIND_ESTIMATE);
    	
    	for(Estimate item : estimateList) {
    		if(myElectEstimateArray.contains(item.getId())) {
    			item.setIsElectedByMe(1);
    		}
    	}
	}
	
	public static void setHotFavouriteStatus(FavouriteDAO favouriteDao, int accountId, Hot hot) {
		List<Integer> myFavouriteHotArray = CommonUtil.getFavouriteIds(favouriteDao, accountId, FavouriteDAO.FAVOURITE_KIND_HOT);
    	
		if(myFavouriteHotArray.contains(hot.getId())) {
			hot.setIsFavourite(1);
		}
	}
	
	public static void setHotFavouriteStatus(FavouriteDAO favouriteDao, int accountId, List<Hot> hotList) {
		List<Integer> myFavouriteHotArray = CommonUtil.getFavouriteIds(favouriteDao, accountId, FavouriteDAO.FAVOURITE_KIND_HOT);
    	
    	for(Hot item : hotList) {
    		if(myFavouriteHotArray.contains(item.getId())) {
    			item.setIsFavourite(1);
    		}
    	}
	}
	
	public static List<String> getImgPathList(Object[] objectArray, int start, int end) {
		
		List<String> imgPathList = new ArrayList<String>();
		
		for(int i=start; i<=end; i++) {
			String strPath = CommonUtil.toStringDefault(objectArray[i]); 
			if(!strPath.isEmpty()) {
				imgPathList.add(strPath);
			}
		}
		
		return imgPathList;
	}
	
	public static void insertMarkLog(MarkLogDAO markLogDao, int receiverId, int kind, Integer estimateKind, Integer estimateMethod, Integer errorKind, Integer pmark, Integer nmark, Integer senderId) {
		 
		MarkLog markLog = new MarkLog();
		
		markLog.setAccountId(receiverId);
		markLog.setKind(kind);
		
		if(estimateKind != null) {
			markLog.setEstimateKind(estimateKind);	
		}
		if(estimateMethod != null) {
			markLog.setEstimateMethod(estimateMethod);	
		}
		if(errorKind != null) {
			markLog.setErrorKind(errorKind);	
		}
		if(pmark != null) {
			markLog.setPmark(pmark);	
		}
		if(nmark != null) {
			markLog.setNmark(nmark);	
		}
		if(senderId != null) {
			markLog.setSenderId(senderId);	
		}
		
		markLogDao.insert(markLog);
	}
	
	public static void insertNotice(NoticeDAO noticeDao, int type, Integer receiverId, int kind, Integer subKind, String msgTitle, String msgContent
			, Integer inviteeId, Integer estimateId, Integer errorId) {
		 
		Notice notice = new Notice();
		
		notice.setType(type);
		if(receiverId != null) {
			notice.setAccountId(receiverId);	
		}
		notice.setKind(kind);
		if(subKind != null) {
			notice.setSubKind(subKind);	
		}
		if(msgTitle != null) {
			notice.setMsgTitle(msgTitle);	
		}
		notice.setMsgContent(msgContent);
		if(inviteeId != null) {
			notice.setInviteeId(inviteeId);	
		}
		if(estimateId != null) {
			notice.setEstimateId(estimateId);	
		}
		if(errorId != null) {
			notice.setErrorId(errorId);
		}
		
		noticeDao.insert(notice);
	}
	
	public static String getNoticeMsgTitle(int kind) {
		String result = "";
		
		switch(kind) {
		case NoticeDAO.NOTICE_KIND_INVITE:
			result = "邀请好友注册成功";
			break;
		case NoticeDAO.NOTICE_KIND_AUTH:
			result = "实名认证";
			break;
		case NoticeDAO.NOTICE_KIND_ESTIMATE:
			result = "对我的评价";
			break;
		case NoticeDAO.NOTICE_KIND_CORRECTION:
			result = "纠错消息";
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String getNoticeMsgContent(int kind, Integer subKind, Object param1, Object param2) {
		String result = "";
		
		if(kind == NoticeDAO.NOTICE_KIND_INVITE) {
			result = "您有一名好友注册成功，为感谢您对诚信系统做出的贡献，奖励您" + String.valueOf(param1) + "个好评，请再接再厉。";
		}
		else if(kind == NoticeDAO.NOTICE_KIND_AUTH) {
			if(subKind == NoticeDAO.NOTICE_SUBKIND_AUTH_PASS) {
				result = "您好，您的认证已审核通过，点击查看详情！";	
			}
			else if(subKind == NoticeDAO.NOTICE_SUBKIND_AUTH_DENY) {
				result = "您好，您的认证审核未通过，点击查看详情！";
			}
		}
		else if(kind == NoticeDAO.NOTICE_KIND_ESTIMATE) {
			if(subKind == EstimateDAO.ESTIMATE_KIND_FORWORD) {
				result = "您好， 有人对您进行了正面评价，奖励您" + String.valueOf(param1) + "个好评，点击查看详情！";
			}
			else if(subKind == EstimateDAO.ESTIMATE_KIND_BACKWORD) {
				result = "您好， 有人对您进行了负面评价，惩罚" + String.valueOf(param1) + "个差评，点击查看详情！";
			}
			
		}
		else if(kind == NoticeDAO.NOTICE_KIND_CORRECTION) {
			if(subKind == NoticeDAO.NOTICE_SUBKIND_CORRECTION_GIVE) {
				if(Integer.valueOf(String.valueOf(param1)) == ErrorDAO.ERROR_ST_PASS) {
					result = "您好， 您的纠错审核已通过，奖励您" + String.valueOf(param2) + "个好评，请再接再厉。点击查看详情！";
				}
				else if(Integer.valueOf(String.valueOf(param1)) == ErrorDAO.ERROR_ST_NOPASS) {
					result = "您好， 您的纠错审核未通过，点击查看详情！";
				}
			}
			else if(subKind == NoticeDAO.NOTICE_SUBKIND_CORRECTION_RECEIVE) {
				if(Integer.valueOf(String.valueOf(param1)) == ErrorDAO.ERROR_KIND_OVER) {
					result = "您好，你有一条评价被别人进行了纠错，审核结果为“夸大评价”，惩罚" + MarkLogDAO.NMARK_CORRECT_RECEIVE_OVER + "个差评，请如实进行评价，点击查看详情！";	
				}
				else if(Integer.valueOf(String.valueOf(param1)) == ErrorDAO.ERROR_KIND_OVER) {
					result = "您好，你有一条评价被别人进行了纠错，审核结果为“虚假评价”，惩罚" + MarkLogDAO.NMARK_CORRECT_RECEIVE_FALSE + "个差评，请如实进行评价，点击查看详情！";
				}
			}
		}
		return result;
	}
	
	public static String makeMarkLogMsg(MarkLog markLog) {
		
		String msg = "";
		
		if(markLog.getKind() == MarkLogDAO.LOG_KIND_INVITE) {
			msg = "推荐好友注册";
		}
		else if(markLog.getKind() == MarkLogDAO.LOG_KIND_ESTIMATE_RECEIVE) {
			msg = (markLog.getSenderAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? markLog.getSenderRealname() : markLog.getSenderEnterName())
					+ "对您做了" + markLog.getEstimateKindName();
		}
		else if(markLog.getKind() == MarkLogDAO.LOG_KIND_ESTIMATE_GIVE) {
			msg = "对" + (markLog.getSenderAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? markLog.getSenderRealname() : markLog.getSenderEnterName()) 
					+ "做了" + markLog.getEstimateMethodName();
		}
		else if(markLog.getKind() == MarkLogDAO.LOG_KIND_CORRECT_RECEIVE) {
			if(markLog.getErrorKind() == ErrorDAO.ERROR_KIND_OVER) {
				msg = (markLog.getSenderAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? markLog.getSenderRealname() : markLog.getSenderEnterName())
						+ "对您做了纠错，审核结果为“夸大评价”";	
			}
			else if(markLog.getErrorKind() == ErrorDAO.ERROR_KIND_OVER) {
				
			}
			
		}
		else if(markLog.getKind() == MarkLogDAO.LOG_KIND_CORRECT_GIVE) {
			msg = "您纠错成功";
		}
		
		return msg;
	}

	public static Integer getFriendLevel(int id, Account loginAccount) {
		ArrayList<String> friends;
		String strFriends = loginAccount.getFriend1();
		if(strFriends != null && !strFriends.isEmpty()) {
			String[] arrFriend = strFriends.split(",");
			friends = new ArrayList<>(Arrays.asList(arrFriend));
			if(friends.contains(String.valueOf(id))) {
				return 1;
			}
		}
		
		strFriends = loginAccount.getFriend2();
		if(strFriends != null &&!strFriends.isEmpty()) {
			String[] arrFriend = strFriends.split(",");
			friends = new ArrayList<>(Arrays.asList(arrFriend));
			if(friends.contains(String.valueOf(id))) {
				return 2;
			}
		}
		
		strFriends = loginAccount.getFriend3();
		if(strFriends != null &&!strFriends.isEmpty()) {
			String[] arrFriend = loginAccount.getFriend3().split(",");
			friends = new ArrayList<>(Arrays.asList(arrFriend));
			if(friends.contains(String.valueOf(id))) {
				return 3;
			}
		}
		
		return null;
	}
}
