<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!--[if IE 8]>
<html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]>
<html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<%@ include file="/WEB-INF/template/manage/layout/head.jsp"%>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp"%>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
	<div class="form">
		<form class="form-horizontal" method="post" action="${cur_page}?pAct=changePwdDo" id="change_pwd">
			<div class="form-body">
				<div class="form-group">
					<label class="col-md-2 control-label">旧密码</label>
					<div class="col-md-3">
						<input type="password" class="form-control input-icon" name="oldpass">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">新密码</label>
					<div class="col-md-3">
						<input type="password" class="form-control input-icon" name="newpass">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">确认密码</label>
					<div class="col-md-3">
						<input type="password" class="form-control input-icon" name="confirmpass">
					</div>
				</div>
			</div>
			<div class="form-actions fluid">
				<div class="row">
					<div class="col-md-offset-2 col-md-9">
						<button type="submit" class="btn green">确定</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp"%>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>

	$(document).ready(function() {
		
		$('#change_pwd').validate({
	        errorElement: 'span', //default input error message container
	        errorClass: 'help-block', // default input error message class
	        focusInvalid: false, // do not focus the last invalid input
	        rules: {
	        	'oldpass': {
	                required: true,
	            },
	            'newpass': {
	                required: true,
	                minlength: 6,
	                maxlength: 20,
	                
	            },
	            'confirmpass': {
	                required: true,
	                minlength: 6,
	                maxlength: 20,
	            }
	        },

	        messages: {
	            'oldpass': {
	                required: "请输入旧密码",
	            },
	            'newpass': {
	                required: "请输入新密码",
	                minlength: "密码由6-20数字和字母组成",
	                maxlength: "密码由6-20数字和字母组成",
	            },
	            'confirmpass': {
	                required: "请输入旧密码",
	                minlength: "密码由6-20数字和字母组成",
	                maxlength: "密码由6-20数字和字母组成",
	            },
	        },

	        highlight: function (element) { // hightlight error inputs
	        	$(element).closest('.form-group').addClass('has-error'); // set error class to the control group
	        },
	        
	        success: function (label) {
	        	label.closest('.form-group').removeClass('has-error');
	        	label.remove();
	        },
	        
	        errorPlacement: function (error, element) {
	        	error.insertAfter(element.closest('.input-icon'));
	        },

	        submitHandler: function(form) {
	            $('#change_pwd').ajaxSubmit({
	                success: function(_response, _status, _xhr, $form) {
	                    if (_response.success == 200) {
	                        $('#change_pwd').resetForm();
	                        $('.form-control').closest('.form-group').removeClass('has-error').removeClass('has-success');
	                        toastr['success'](_response.msg);
	                    }else
	                        toastr['error'](_response.msg);
	                }
	            });
	        }
	    });
	});
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
