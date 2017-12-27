package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Carousel;
import com.chengxin.bfip.model.CarouselDAO;
import com.chengxin.bfip.model.Video;
import com.chengxin.bfip.model.VideoDAO;
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
public class CarouselController extends BaseController {

	private CarouselDAO memberDao = null;
	private VideoDAO videoDao = null;

	public void setMemberDao(CarouselDAO value) {this.memberDao = value;}
	public void setVideoDao(VideoDAO value) {this.videoDao = value;}

	public CarouselController() throws Exception {
	}

	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		if(!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "", "login.html");
		}
		if(!this.checkManagePermission(request, "carousel.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "carousel.html");
		request.setAttribute("title", new String[] {"轮播图管理", "轮播图管理"});
		request.setAttribute("breadcrumbs", new KeyValueString[] {
				new KeyValueString("轮播图管理", "carousel.html")
		});

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("add")) {
			return this.add(request, response, session);
		} else if (action.equals("upload")) {
			BinaryFormUtil formUtil = new BinaryFormUtil(
					CommonUtil.getRepositoryRealPath(request) + Constants.CAROUSEL_MEDIA_PATH,
					CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
					Constants.C_COMMON_FILE_PATH_SEP);

			formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

			return this.upload(formUtil, request, response);
		} else if (action.equals("changeStatus")) {
			return this.changeStatus(request, response, session);
		} else if (action.equals("delete")) {
			return this.delete(request, response, session);
		}

		return null;
	}

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		return new ModelAndView("manage/carousel/index");
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject filterParamObject = new JSONObject();
		filterParamObject.put("order_col", "write_time");
		filterParamObject.put("order_dir", "desc");

		List<Video> videosList = videoDao.search(filterParamObject);

		request.setAttribute("videosList", videosList);

		return new ModelAndView("manage/carousel/add");
	}

	public ModelAndView upload(BinaryFormUtil formUtil, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int kind = Integer.parseInt(formUtil.getString("kind", "0"));
		int ord = Integer.parseInt(formUtil.getString("ord", "0"));

		double sw = Double.parseDouble(formUtil.getString("sw", "0"));
		double sh = Double.parseDouble(formUtil.getString("sh", "0"));
		double x = Double.parseDouble(formUtil.getString("x", "0"));
		double y = Double.parseDouble(formUtil.getString("y", "0"));
		double w = Double.parseDouble(formUtil.getString("w", "0"));
		double h = Double.parseDouble(formUtil.getString("h", "0"));

		int videoId = Integer.parseInt(formUtil.getString("video_id", "0"));

		File imgFile = formUtil.getFile("img_file_url");
		
		//if(video_title.equals("")) {
		//	return JavascriptUtil.MessageMove(request, response, "video_title", "");
		//}
		Carousel carousel = new Carousel();
		carousel.setKind(kind);
		carousel.setOrd(ord);
		carousel.setStatus(CarouselDAO.ST_OFF);
		carousel.setVideoId(videoId);
		
		if (imgFile == null) {
			carousel.setImgName("");
			carousel.setImgSize(0);
			carousel.setImgUrl("");
		} else {
			if(w > 0 && h > 0) {
				String absoluteDir = CommonUtil.getRepositoryRealPath(request) + Constants.CAROUSEL_MEDIA_PATH;
				String savefileName = ImageCropper.ImageCrop(
						absoluteDir + Constants.C_COMMON_FILE_PATH_SEP + imgFile.getPhysicalPath(), imgFile.getPhysicalName(),
						sw, sh, x, y, w, h);
				carousel.setImgName(savefileName);	
				carousel.setImgUrl(Constants.CAROUSEL_MEDIA_URL + "/" + imgFile.getPhysicalPath() + "/" + savefileName);
			}
			else {
				carousel.setImgName(imgFile.getPhysicalName());
				carousel.setImgUrl(Constants.CAROUSEL_MEDIA_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName());
			}
			carousel.setImgSize(imgFile.getSize());
			
		}

		memberDao.insert(carousel);
		
		this.cachedObjectService.deleteProgram();

		//return JavascriptUtil.MessageMove(request, response, "", "program.html?pAct=search");
		JSONObject result = new JSONObject();
		result.put("flag", true);
		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject filterParamObject = new JSONObject();

		String start = this.getBlankParameter(request, "start", "");
		String length = this.getBlankParameter(request, "length", "");
		String orderColIndex = this.getBlankParameter(request, "order[0][column]", "");
		String orderColName = this.getBlankParameter(request, "columns[" + orderColIndex + "][name]", "");
		String orderDir = this.getBlankParameter(request, "order[0][dir]", "asc");

		filterParamObject.put("start", start);
		filterParamObject.put("length", length);
		filterParamObject.put("order_col", orderColName);
		filterParamObject.put("order_dir", orderDir);

		List<Carousel> carouselList = memberDao.search(filterParamObject);
		int count = memberDao.count(filterParamObject);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for(int i=0; i<carouselList.size(); i++) {
			Carousel row = carouselList.get(i); 

			String opHtml = "";
			if(row.getStatus() == CarouselDAO.ST_ON) {
				opHtml += "<a href='javascript:changeStatus(" + String.valueOf(row.getId()) + "," + CarouselDAO.ST_OFF + ")' "
						+ "class='btn btn-xs green'><i class='fa fa-arrow-down'></i> 下架</a>";
			} else {
				opHtml += "<a href='javascript:changeStatus(" + String.valueOf(row.getId()) + "," + CarouselDAO.ST_ON + ")' "
						+ "class='btn btn-xs green'><i class='fa fa-arrow-up'></i> 上架</a>";
				opHtml += "<a href='javascript:deleteRecord(" + String.valueOf(row.getId()) + ")' "
						+ "class='btn btn-xs red'><i class='fa fa-trash'></i> 删除</a>";
			}

			String[] dataItem = new String[] {
					String.valueOf(i+1),
					"<img src='" + Constants.C_UPLOAD_PATH + row.getImgUrl() + "' alt='头像图片' width='250px' >",
					row.getKind() == CarouselDAO.MEDIA_KIND_IMAGE ? "图片" : "视频",
					"" + row.getOrd(),
					DateTimeUtil.dateFormat(row.getWriteTime(), "yyyy-MM-dd HH:mm"),
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

	public ModelAndView changeStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String targetStatus = this.getBlankParameter(request, "targetStatus", "");

		if (targetStatus.equals("" + CarouselDAO.ST_ON)) {
			int count = memberDao.count(null, " status = 1 ");
			if (count >= Constants.MAXSIZE_ACTIVE_CAROUSEL) {
				result.put("retcode", 300);
				result.put("msg", "上架数量最多" + Constants.MAXSIZE_ACTIVE_CAROUSEL + "张。");

				request.setAttribute("JSON", result);

				return new ModelAndView("json_result");
			}
		}

		Carousel record = memberDao.get(Integer.valueOf(id));

		record.setStatus(Integer.valueOf(targetStatus));

		memberDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功。");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Carousel record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);
		String imgPath = CommonUtil.getRepositoryRealPath(request) + Constants.C_COMMON_FILE_PATH_SEP + ".." + record.getImgUrl().replace("/", Constants.C_COMMON_FILE_PATH_SEP);
		java.io.File imgFile = new java.io.File(imgPath);
		if (imgFile.exists()) {
			imgFile.delete();
		}

		result.put("retcode", 200);
		result.put("msg", "操作成功。");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

}