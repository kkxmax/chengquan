package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Hot;
import com.chengxin.bfip.model.HotDAO;
import com.chengxin.bfip.model.Xyleixing;
import com.chengxin.bfip.model.XyleixingDAO;
import com.chengxin.common.AppSettings;
import com.chengxin.common.BaseController;
import com.chengxin.common.BinaryFormUtil;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.File;
import com.chengxin.common.ImageCropper;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class HotController extends BaseController {

	private HotDAO hotDao = null;
	private XyleixingDAO xyleixingDao = null;

	public void setXyleixingDao(XyleixingDAO xyleixingDao) {
		this.xyleixingDao = xyleixingDao;
	}

	public void sethotDao(HotDAO value) {
		this.hotDao = value;
	}

	public HotController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "", "login.html");
		}
		if(!this.checkManagePermission(request, "hots.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "hots.html");
		request.setAttribute("title", new String[] { "热点管理", "热点管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("热点管理",
						"hots.html") });
		request.setAttribute("C_CROP_BG_IMAGE", Constants.C_HOT_CROP_BG_IMAGE);

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("viewDetail")) {
			return this.viewDetail(request, response, session);
		} else if (action.equals("delete")) {
			return this.delete_record(request, response, session);
		} else if (action.equals("up")) {
			return this.up(request, response, session);
		} else if (action.equals("down")) {
			return this.down(request, response, session);
		} else if (action.equals("edit")) {
			return this.edit(request, response, session);
		} else if (action.equals("editDo")) {
			return this.editDo(request, response, session);
		} else if (action.equals("getLevel2Xyleixings")) {
			return this.getLevel2Xyleixings(request, response, session);
		} else if (action.equals("uploadEditorImage")) {
			return this.uploadEditorImage(request, response, session);
		} 

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		List<Xyleixing> leixings = xyleixingDao.search(null, "upper_id=0");
		
		request.setAttribute("leixings", leixings);
		request.setAttribute("C_HOTS_ST_NAME", Constants.C_HOTS_ST_NAME);

		return new ModelAndView("manage/hots/index");
	}

	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");
		
		Hot record = null;
		if(!id.isEmpty()) {
			record = hotDao.getDetail(request, Integer.valueOf(id));
		}
		
		List<Xyleixing> leixings = xyleixingDao.search(null, "upper_id=0");
		request.setAttribute("leixings", leixings);
		request.setAttribute("record", record);		
		
		return new ModelAndView("manage/hots/edit");
	}

	public ModelAndView editDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				Constants.C_COMMON_FILE_PATH_SEP);
		
		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));
		
		String id = formUtil.getString("id", "");
		String title = formUtil.getString("title", "");
		String xyleixingId = formUtil.getString("xyleixingId", "");
		String content = formUtil.getString("content", "");
		String summary = formUtil.getString("summary", "");
		
		double sw = Double.parseDouble(formUtil.getString("sw", "0"));
		double sh = Double.parseDouble(formUtil.getString("sh", "0"));
		double x = Double.parseDouble(formUtil.getString("x", "0"));
		double y = Double.parseDouble(formUtil.getString("y", "0"));
		double w = Double.parseDouble(formUtil.getString("w", "0"));
		double h = Double.parseDouble(formUtil.getString("h", "0"));
		
		double sw2 = Double.parseDouble(formUtil.getString("sw2", "0"));
		double sh2 = Double.parseDouble(formUtil.getString("sh2", "0"));
		double x2 = Double.parseDouble(formUtil.getString("x2", "0"));
		double y2 = Double.parseDouble(formUtil.getString("y2", "0"));
		double w2 = Double.parseDouble(formUtil.getString("w2", "0"));
		double h2 = Double.parseDouble(formUtil.getString("h2", "0"));
		
		double sw3 = Double.parseDouble(formUtil.getString("sw3", "0"));
		double sh3 = Double.parseDouble(formUtil.getString("sh3", "0"));
		double x3 = Double.parseDouble(formUtil.getString("x3", "0"));
		double y3 = Double.parseDouble(formUtil.getString("y3", "0"));
		double w3 = Double.parseDouble(formUtil.getString("w3", "0"));
		double h3 = Double.parseDouble(formUtil.getString("h3", "0"));
		
		Hot hot;
		if(id.isEmpty()) {
			hot = new Hot();	
		}
		else {
			hot = hotDao.get(Integer.valueOf(id));
		}
		
		hot.setTitle(title);
		hot.setXyleixingId(Integer.valueOf(xyleixingId));
		hot.setSummary(summary);
		
		File imgFile;
		String strImageFile;
		
		imgFile = formUtil.getFile("imgPath1");
    	if(imgFile != null) {
    		if(w > 0 && h > 0) {
    			String absoluteDir = CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH;
    			String savefileName = ImageCropper.ImageCrop(
    					absoluteDir + Constants.C_COMMON_FILE_PATH_SEP + imgFile.getPhysicalPath(), imgFile.getPhysicalName(),
    					sw, sh, x, y, w, h);
        		strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + savefileName;
    		}
    		else {
    			strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
    		}
    		
    		hot.setImgPath1(strImageFile);
    	}

    	imgFile = formUtil.getFile("imgPath2");
    	if(imgFile != null) {
    		if(w2 > 0 && h2 > 0) {
    			String absoluteDir = CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH;
    			String savefileName = ImageCropper.ImageCrop(
    					absoluteDir + Constants.C_COMMON_FILE_PATH_SEP + imgFile.getPhysicalPath(), imgFile.getPhysicalName(),
    					sw2, sh2, x2, y2, w2, h2);
        		strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + savefileName;
    		}
    		else {
    			strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
    		}
    		hot.setImgPath2(strImageFile);
    	}

    	imgFile = formUtil.getFile("imgPath3");
    	if(imgFile != null) {
    		if(w3 > 0 && h3 > 0) {
    			String absoluteDir = CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH;
    			String savefileName = ImageCropper.ImageCrop(
    					absoluteDir + Constants.C_COMMON_FILE_PATH_SEP + imgFile.getPhysicalPath(), imgFile.getPhysicalName(),
    					sw3, sh3, x3, y3, w3, h3);
        		strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + savefileName;
    		}
    		else {
    			strImageFile = Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
    		}
    		hot.setImgPath3(strImageFile);
    	}
    	
    	String contentFilePath = CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH + Constants.C_COMMON_FILE_PATH_SEP + "html";
		String contentFileName = DateTimeUtil.getUniqueTime() + ".htm";
		
		CommonUtil.writeFile(contentFilePath, contentFileName, content);
		
		hot.setContent(Constants.HOT_IMAGE_URL + "/html/" + contentFileName);
		
		if(id.isEmpty()) {
			hotDao.insert(hot);
			result.put("retcode", 200);
			result.put("msg", "新增成功");
		}
		else {
			hotDao.update(hot);
			result.put("retcode", 200);
			result.put("msg", "更新成功");
		}

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject filterParamObject = new JSONObject();

		String filterParam = this.getBlankParameter(request,
				"filter[filter_param]", "");
		String start = this.getBlankParameter(request, "start", "");
		String length = this.getBlankParameter(request, "length", "");
		String orderColIndex = this.getBlankParameter(request,
				"order[0][column]", "");
		String orderColName = this.getBlankParameter(request, "columns["
				+ orderColIndex + "][name]", "");
		String orderDir = this.getBlankParameter(request, "order[0][dir]",
				"asc");

		if (!filterParam.isEmpty()) {
			filterParam = HtmlUtils.htmlUnescape(filterParam);
			filterParamObject = JSONObject.fromObject(filterParam);
		}
		filterParamObject.put("start", start);
		filterParamObject.put("length", length);
		filterParamObject.put("order_col", orderColName);
		filterParamObject.put("order_dir", orderDir);

		String extraWhere = "";
		List<Hot> HotList = hotDao.search(request, filterParamObject, extraWhere);
		int count = hotDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < HotList.size(); i++) {
			Hot row = HotList.get(i);

			String opHtml = "<a href='hots.html?pAct=viewDetail&id="
					+ row.getId()
					+ "' class='btn btn-xs purple'><i class='fa fa-edit'></i> 查看</a>";
			if(row.getStatus() == HotDAO.HOTS_ST_DOWN) {
				opHtml += "<a href='hots.html?pAct=edit&id=" + row.getId() + "' class='btn btn-xs purple'><i class='fa fa-edit'></i> 编辑</a>";	
			}
			if(row.getStatus() == HotDAO.HOTS_ST_UP) {
				opHtml += "<a href='javascript:;down("+ row.getId() +");' class='btn btn-xs default'><i class='fa fa-check'></i> 下架</a>";	
			}
			else if(row.getStatus() == HotDAO.HOTS_ST_DOWN) {
				opHtml += "<a href='javascript:;up("+ row.getId() +");' class='btn btn-xs default'><i class='fa fa-check'></i> 上架</a>";
				opHtml += "<a href='javascript:;delete_record("+ row.getId() +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i> 删除</a>";
			}
			
			String[] dataItem = new String[] {
					row.getTitle(), 
					row.getXyleixingName(),
					String.valueOf(row.getVisitCnt()),
					String.valueOf(row.getCommentCnt()),
					String.valueOf(row.getElectCnt()),
					String.valueOf(row.getShareCnt()),
					DateTimeUtil.dateFormat(row.getStatusName()),
					DateTimeUtil.dateFormat(row.getWriteTime()), 
					opHtml 
			};
			data.add(dataItem);
		}
		result.put("recordsTotal", count);
		result.put("recordsFiltered", count);
		result.put("data", data);

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView viewDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");

		Hot record = hotDao.getDetail(request, Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/hots/detail");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Hot record = hotDao.get(Integer.valueOf(id));

		hotDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView up(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Hot record = hotDao.get(Integer.valueOf(id));

		if(record == null) {
			result.put("retcode", 201);
			result.put("msg", "该热点不存在。");

			request.setAttribute("JSON", result);
		}
		
		record.setStatus(HotDAO.HOTS_ST_UP);
		
		hotDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView down(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Hot record = hotDao.get(Integer.valueOf(id));

		if(record == null) {
			result.put("retcode", 201);
			result.put("msg", "该热点不存在。");

			request.setAttribute("JSON", result);
		}
		
		record.setStatus(HotDAO.HOTS_ST_DOWN);
		
		hotDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView getLevel2Xyleixings(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String level1leixing = this.getBlankParameter(request, "level1leixing", "");

		String where = "";
		if(level1leixing.isEmpty()) {
			where = "upper_id != 0";
		}
		else {
			where = "upper_id=" + level1leixing;
		}
		List<Xyleixing> records = xyleixingDao.search(null, where);

		result.put("retcode", 200);
		result.put("records", records);

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView uploadEditorImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.HOT_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				Constants.C_COMMON_FILE_PATH_SEP);
		
		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));
		
		String strCKEditorFuncNum = formUtil.getString("CKEditorFuncNum", "1");
		String strImageFile = "";
		
		List<File> imgFileList = formUtil.getFileList();
		
		if(imgFileList.size() > 0) {
			File imgFile = imgFileList.get(0);
			strImageFile = Constants.C_UPLOAD_PATH + Constants.HOT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
		}
		
		
		String result = String.format("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(%s, '%s', '');</script>", strCKEditorFuncNum, strImageFile);
		
		request.setAttribute("TEXT", result);

		return new ModelAndView("print");
	}
}