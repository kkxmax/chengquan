package com.chengxin.bfip.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.common.AppSettings;
import com.chengxin.common.BaseController;
import com.chengxin.common.BinaryFormUtil;
import com.chengxin.common.File;

/**
 *
 * @author Administrator
 */
public class TestApiController extends BaseController {

    public TestApiController() throws Exception {
    	
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
        String action = this.getBlankParameter(request, "pAct", "");
                
        if (action.equals("") || action.equals("index")) {
            return this.index(request, response, session);
        } else if(action.equals("upload")) {
        	return this.upload(request, response, session);
        }
        
        return null;
    }
    
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    
    	return new ModelAndView("testApi/index");
    }
    
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        
    	Logger.getLogger("bfip").log(Level.INFO, CommonUtil.getRepositoryRealPath(request));
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.CAROUSEL_MEDIA_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));
		
//		File imgFile = formUtil.getFile("cert_image");
//		
//		if(imgFile != null) {
//			Logger.getLogger("bfip").log(Level.INFO, Constants.CAROUSEL_MEDIA_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName());
//		}
		
		List<File> imgFileList = formUtil.getFileList();
		
		for(File imgFile : imgFileList) {

			Logger.getLogger("bfip").log(Level.INFO, Constants.CAROUSEL_MEDIA_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName());
		}
		
    	return new ModelAndView("testApi/index");
    }
}