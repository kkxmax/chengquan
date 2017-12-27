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
<style>
	.sub-title {background-color: #f2f2f2; padding-top: 8px}
</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
  <div class="portlet box blue-hoki">
    <div class="portlet-title">
      <div class="caption">
        <i class="fa fa-circle-o"></i>纠错详情
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
				<label class="col-xs-3 text-right">账号:</label> <label
					class="col-xs-9 text-left">${record.ownerMobile}</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">纠错人:</label> <label
					class="col-xs-9 text-left">
					<c:if test="${record.ownerAkind == 1}">${record.ownerRealname}</c:if>
					<c:if test="${record.ownerAkind == 2}">${record.ownerEnterName}</c:if>
				</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">被评价人:</label> <label
					class="col-xs-9 text-left">
					<c:if test="${record.estimateeAkind == 1}">${record.estimateeRealname}</c:if>
					<c:if test="${record.estimateeAkind == 2}">${record.estimateeEnterName}</c:if>
				</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">评价人:</label> <label
					class="col-xs-9 text-left">
					<c:if test="${record.estimaterAkind == 1}">${record.estimaterRealname}</c:if>
					<c:if test="${record.estimaterAkind == 2}">${record.estimaterEnterName}</c:if>
				</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">评价内容:</label><label
					class="col-xs-9 text-left">${record.estimateContent}</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">纠错原因:</label> <label
					class="col-xs-9 text-left">${record.reason}</label>
			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">纠错依据:</label> <label
					class="col-xs-9 text-left">${record.whyis}</label>

			</div>
			<div class="form-group">
				<label class="col-xs-3 text-right">图片:</label>
				<div class="col-xs-9 text-left">
					<c:forEach items="${record.imgPaths}" var="imgPath">
						<img class="avatar-small" src="${C_UPLOAD_PATH}${imgPath}" alt="图像" style="width:18%">
					</c:forEach>
				</div>
			</div>
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
