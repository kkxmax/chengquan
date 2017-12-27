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
	.horizontal-splitter {border: none; border-bottom: solid 1px #f0f0f0}
	.border-none {border: none;}
	.width-full {width: 100%}
	.label-title {color: #b8b8b8; font-size: 18px; float: left; padding-right: 10px}
	.frm-signup input {font-size: 18px; max-width: 150px !important}
	input:focus {outline: none;}
	::-webkit-input-placeholder {color: #b8b8b8;}
	::-moz-placeholder { /* Firefox 19+ */color: #b8b8b8;}
	:-ms-input-placeholder {color: #b8b8b8;}
	:-moz-placeholder { /* Firefox 18- */color: #b8b8b8;}
</style>
<head>
<%@ include file="/WEB-INF/template/front/layout/head.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="row init-div">
  <div class="col-xs-12 init-div-horizontal" style="padding: 40px 35px 35px 35px;">
  	<form class="frm-signup" method="post">
  		<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<input type="text" class="border-none" id="mobile" placeholder="手机号" value="">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal horizontal-splitter">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<input type="text" class="border-none" id="reqCode" placeholder="邀请码" value="${reqCode}">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal horizontal-splitter">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-8 init-div-horizontal">
	  			<input type="text" class="border-none" style="width: 100px" id="verifyCode" placeholder="验证码">
	  		</div>
	  		<div class="col-xs-4 init-div-horizontal">
	  			<div style="border: solid 1px #368fe7; border-radius: 5px !important; padding: 5px; text-align: center;">
	  				<a href="#" onclick="getVerifyCode();" id="btn_send_verifycode" style="color: #368fe7; text-decoration: none">获取验证码</a>
	  			</div>
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal horizontal-splitter">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<input type="password" class="border-none" id="password" placeholder="密码">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal horizontal-splitter">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<input type="password" class="border-none" id="confirmpassword" placeholder="确认密码">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal horizontal-splitter">
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<label><input type="checkbox" id="agree"/></label><label><span style="color: #b8b8b8">我已阅读并同意接受</span><a href="agree.html" style="text-decoration: none !important"><span style="color: #368fe7">《诚乎服务条款》</span></a></label>
	  		</div>
	  	</div>
	  	<div class="row init-div margin-bottom-15">
	  		<div class="col-xs-12 init-div-horizontal">
	  			<button type="button" onclick="onSubmit();" class="btn btn-primary blue width-full" style="border-radius: 4px !important">注册</button>
	  		</div>
	  	</div>
  	</form>
  </div>
  <div id="bottombar" style="border-top: solid 1px #e5e5e5; position: absolute; top: 1000px; left: 0; right: 0; padding-top: 7px">
  	<div class="row init-div">
  		<div class="col-xs-8">
  			<img src="assets/custom/img/login_logo.png" style="width: 50px; float: left">
  			<div style="padding-left: 56px; padding-top: 6px">
  				<p style="font-size: 18px; font-weight: bold; color: #1793e5; margin-bottom: 0">诚乎</p>
  				<p style="color: #1793e5">你的话语权主场</p>
  			</div>
  		</div>
  		<div class="col-xs-4" style="padding-top: 8px">
  			<div style="border: solid 1px #368fe7; border-radius: 5px !important; padding: 5px; text-align: center;">
  				<a href="#" id="btn_send_verifycode" style="color: #368fe7; text-decoration: none; font-size: 14px">下载客户端</a>
  			</div>
  		</div>
  	</div>
  </div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp" %>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>

var g_verify_active = true;

jQuery(document).ready(function () {
	$('#bottombar').css('top', $(window).outerHeight(true) - $('#bottombar').height() - 7);
});

function onSubmit() {
	if($('#password').val() != $('#confirmpassword').val()) {
		toastr['error']('密码和确认密码不一致!');
		return;
	}
	if(!$('#agree').prop('checked')) {
		toastr['error']('您要同意接受《诚乎服务条款》!');
		return;
	}
	$.ajax({
        type: "POST",
        url: "api.html?pAct=register",
       	data: {
       		mobile: $('#mobile').val(),
       		reqCode: $('#reqCode').val(),
       		verifyCode: $('#verifyCode').val(),
       		password: $('#password').val(),
       	},
        success: function(res) {
            if (res.retCode == 200) {
            	location.href = "sign_success.html";
                //toastr['success']('注册成功');
            } else {
                toastr['error'](res.msg);
            }
        },
        error: function(xhr, ajaxOptions, thrownError) {
            toastr['error']('网路错误！');
        }
    });
}

function getVerifyCode() {
	
	if(!g_verify_active) {
		return;
	}
	
	g_verify_active = false;
	
	down_count();
	
	$.ajax({
        type: "POST",
        url: "api.html?pAct=getVerifyCode",
       	data: {
       		mobile: $('#mobile').val(),
       	},
        success: function(res) {
        	
            if (res.retCode == 200) {
            	toastr['success']('验证码发送成功了，请您确认短信。');
            } else {
                toastr['error'](res.msg);
            }
        },
        error: function(xhr, ajaxOptions, thrownError) {
            toastr['error']('网路错误！');
        }
    });
}

function down_count() {
	setTimeout(function() {
		if(!g_verify_active) {
			var text_send_verifycode = $('#btn_send_verifycode').text();
			if(text_send_verifycode == "获取验证码") {
				$('#btn_send_verifycode').text("59秒可重发");
			}
			else {
				var val = parseInt(text_send_verifycode);
				if(val > 1) {
					$('#btn_send_verifycode').text((val-1) + "秒可重发");
				}
				else {
					$('#btn_send_verifycode').text("获取验证码");
					g_verify_active = true;	
				}	
			}
			
			down_count();	
		}

	}, 1000);
}

</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
