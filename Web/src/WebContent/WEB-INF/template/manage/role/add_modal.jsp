<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<modal_width val="35%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">添加角色</h4>
</div>
<form id="frm_edit" action="${cur_page}?pAct=addDo" method="post">
	<div class="modal-body font-gothic">
		<div class="row">
			<div class="form col-xs-12">
				<div class="form-horizontal">
					<div class="form-body col-xs-12">
						<div class="form-group">
							<label class="col-xs-3 control-label text-right">角色名称:</label>
							<div class="col-xs-8">
								<input type="text" class="form-control" name="title" id="title">
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
        	'title': {
                required: true,
                maxlength: 10
            }
        },
        messages: {
        	'title': {
                required: "请输入账号",
                maxlength: "不超过10个字符"
            }
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
