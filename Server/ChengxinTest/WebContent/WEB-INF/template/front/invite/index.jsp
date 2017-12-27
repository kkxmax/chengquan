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
<style>
	
</style>
<head>
<%@ include file="/WEB-INF/template/front/layout/head.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="row init-div">
  <div class="col-xs-12 init-div-horizontal">
  	<div style="width:300px; margin: auto; margin-top: 30px">
  		<div style="width: 300px; height: 114px; background: url('assets/custom/img/invite_top.png');"></div>
  		<div style="margin-top: -1px; width: 300px; height: 100px; background: url('assets/custom/img/invite_blue_bg.png');">
  			<div class="row init-div text-center" style="color: white; font-size: 16px; border-bottom: dashed 1px white; margin: 0px 50px; padding: 10px">邀请码</div>
  			<div class="row init-div text-center" style="color: white; font-size: 24px; padding-top: 10px">
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[1]}</span>
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[2]}</span>
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[3]}</span>
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[4]}</span>
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[5]}</span>
  				<span style="background: #56b0ea; padding: 5px">${reqCodeArr[6]}</span>
  			</div>
  		</div>
  		<div class="text-center" style="margin-top: -1px; width: 300px; height: 31px; background: url('assets/custom/img/invite_middle.png');">
  			<div style="width: 80px; height: 80px; margin: auto;"><img id="img_logo" src="assets/custom/img/no_image.png" style="width: 100%; height: 100%; border-radius: 50% !important; border: solid 4px white"></div>
  		</div>
  		<div style="margin-top: -1px; width: 300px; height: 200px; background: url('assets/custom/img/invite_gray_bg.png');">
  			<div class="row init-div text-center" style="padding-top: 60px; font-size: 14px; color: #666666">您的好友邀请您</div>
  			<div class="row init-div text-center" style="margin-top: 5px; font-size: 20px; font-weight: bold">共建商业诚信生态系统</div>
  			<div class="row init-div text-center">
  				<button type="button" onclick="location.href='signup.html?reqcode=${reqCode}'" class="btn btn-primary" style="margin-top: 25px; width: 90%; border-radius: 4px !important; background: #fe9f10; font-size: 20px">立即注册</button>
  			</div>
  		</div>
  		<div style="margin-top: -1px; width: 300px; height: 10px; background: url('assets/custom/img/invite_bottom.png') no-repeat;"></div>
  	</div>
  	<div class="row init-div text-center" style="margin-top: 20px;"><img src="assets/custom/img/yaoqing_down.png" style="width: 15px"></div>
  	<div class="row init-div text-center" style="margin-top: 5px; font-size: 16px"><a href="" style="color: #d4d4d6; text-decoration: none">下载客户端</a></div>
  </div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp" %>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>

jQuery(document).ready(function () {
	$.backstretch(["assets/custom/img/invite_bg.png"], {});
	
	var shareUserId = getParameterByName("shareUserId");
	
	$.ajax({
		type : "POST",
		url : "api.html?share=1&pAct=getAccountInfo",
		data:{
			accountId: shareUserId
		},
		success : function(res) {
			if (res.retCode == 200) {
				
				$('#img_logo').attr('src', uploadPath + res.account.logo);
			} else {
				toastr['error'](res.msg);
			}
		},
		error : function(xhr, ajaxOptions,
				thrownError) {
			toastr['error']('网路错误！');
		}
	});
});

</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
