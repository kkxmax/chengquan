<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!--[if IE 8]>
<html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]>
<html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<%@ include file="/WEB-INF/template/manage/layout/head.jsp" %>
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/themes/default/css/ueditor.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/third-party/codemirror/codemirror.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
  <div class="portlet box blue-hoki">
    <div class="portlet-title">
      <div class="caption">
        <i class="fa fa-circle-o"></i>账户详情
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
      </div>
      <div class="actions">
      </div>
    </div>
    <div class="portlet-body" style="padding-top: 30px;">
      <div class="form-horizontal">
        <div class="form-body col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 text-right">账号:</label>
            <label class="col-xs-9 align-left">${record.mobile}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-3 text-right">诚信代码:</label>
            <label class="col-xs-9 align-left">${record.code}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-3 text-right">信用度:</label>
            <label class="col-xs-9 align-left">${record.credit}%</label>
          </div>
          <div class="form-group">
            <label class="col-xs-3 text-right">推荐人数:</label>
            <label class="col-xs-9 align-left">${record.electCnt}</label>
          </div>
          <c:if test="${record.testStatus == 2}">
	          <div class="form-group">
	            <label class="col-xs-3 text-right">头像:</label>
	            <div class="col-xs-9"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.logo}" alt="头像图片" style="width: 50px; max-height: 50px;"></div>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">真实姓名:</label>
	            <label class="col-xs-9 align-left">${record.realname}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">身份证:</label>
	            <div class="col-xs-9"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.certImage}" alt="身份证图像" style="height: 130px;"></div>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">公司:</label>
	            <label class="col-xs-9 align-left">${record.enterName}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">行业:</label>
	            <label class="col-xs-9 align-left">${record.xyName}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">所在地:</label>
	            <label class="col-xs-9 align-left">${record.provinceName} ${record.cityName} ${record.addr}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">职位:</label>
	            <label class="col-xs-9 align-left">${record.job}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">工作经验:</label>
	            <label class="col-xs-9 align-left">${record.experience}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">个人经历:</label>
	            <label class="col-xs-9 align-left">${record.history}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">特长或喜欢的行业:</label>
	            <label class="col-xs-9 align-left">${record.xyWatchedName}</label>
	          </div>
	          <div class="form-group">
	            <label class="col-xs-3 text-right">喜欢的资源或朋友:</label>
	            <label class="col-xs-9 align-left">${record.xyWatchName}</label>
	          </div>
	    	</c:if>
        </div>
        <div class="form-actions">
            <div class="row">
                <div class="col-md-offset-5 col-md-4">
                    <a href="${cur_page}" class="btn btn-default green"> 返回 </a>
                </div>
            </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp" %>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>
$(document).ready(function () {

});
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
