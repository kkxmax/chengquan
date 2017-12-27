<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<modal_width val="35%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">分类编辑</h4>
</div>
<form id="frm_edit" action="${cur_page}?pAct=addDo" method="post">
	<div class="modal-body font-gothic">
		<div class="row">
			<div class="form col-xs-12">
				<div class="form-horizontal">
					<div class="form-body col-xs-12">
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">账号:</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" name="username">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">姓名:</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" name="realname">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">密码:</label>
							<div class="col-xs-8">
								<input type="password" class="form-control" name="password" id="password">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">确认密码:</label>
							<div class="col-xs-8">
								<input type="password" class="form-control" name="repassword" id="repassword">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">用户角色:</label>
							<div class="col-xs-8">
								<select class="form-control select2me input-small"
									name="role_id">
									<c:forEach items="${role_ids}" var="item">
										<option value="${item.id}">${item.title}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer" style="text-align: center">
		<button type="submit" class="btn btn-primary">确认</button>
		<button type="button" class="btn default" data-dismiss="modal">取消</button>
	</div>
</form>

<script>
$(document).ready(function () {
	
	$('#frm_edit').validate({
        errorElement: 'span',
        errorClass: 'help-block-error',
        focusInvalid: false,
        ignore: '',
        rules: {
        	'username': {
                required: true,
                maxlength: 20
            },
        	'realname': {
                required: true,
                maxlength: 20
            },
            'password': {
                required: true,
                minlength: 6,
                maxlength: 20,
            },
            'repassword': {
                required: true,
                minlength: 6,
                maxlength: 20,
            },
            'role_id': {
                required: true,
            },
        },
        messages: {
        	'username': {
                required: "请输入账号",
                maxlength: "不超过20个字符",
            },
            'realname': {
                required: "请输入姓名",
                maxlength: "不超过20个字符",
            },
            'password': {
                required: "请密码",
                minlength: "密码由6-20数字和字母组成",
                maxlength: "密码由6-20数字和字母组成",
            },
            'repassword': {
                required: "请确认密码",
                minlength: "密码由6-20数字和字母组成",
                maxlength: "密码由6-20数字和字母组成",
            },
            'role_id': {
                required: "请用户角色",
            },
        },
        highlight: function(e) {
            $(e).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(e) {
            $(e).closest('.form-group').removeClass('has-error');
        },
        submitHandler: function(form) {
            $(form).ajaxSubmit({
                beforeSubmit: function() {
                    Metronic.blockUI({target: '#content-div',animate: true});
                },
                success: function(resp) {
                    Metronic.unblockUI('#content-div');
                    if (resp.retcode == 200) {
                        toastr['success'](resp.msg);
                        $('#global-modal').modal('hide');
                        loadTable();
                    } else {
                        toastr['error'](resp.msg);
                    }
                }
            });
        }
	});
	
});

</script>
