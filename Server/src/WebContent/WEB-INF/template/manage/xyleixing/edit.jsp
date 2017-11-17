<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="portlet box purple">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-edit"></i>类型编辑
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse"></a>
            <a href="javascript:;" class="reload"></a>
        </div>
    </div>
    <div class="portlet-body form">
        <form id="frm_edit" class="form-horizontal" action="${cur_page}?pAct=editDo" method="post">
            <input type="hidden" name="id" value="${record.id}">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-3 control-label">名称</label>
                    <div class="col-md-9">
                    	<input type="text" class="form-control" name="title" value="${record.title}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">父母</label>
                    <div class="col-md-9">
                        <select class="form-control select2me input-small" name="upperId">
		                	<option value="0">一级类型</option>
		                	<c:forEach items="${root_leixings}" var="item">
		                		<option value="${item.id}" <c:if test="${record.upperId == item.id}">selected</c:if>>${item.title}</option>
		                	</c:forEach>
		                </select>
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <div class="row">
                    <div class="col-md-offset-3 col-md-9">
                        <button type="submit" class="btn purple"><i class="fa fa-save"></i> 确定</button>
                        <button type="button" class="btn red" onclick="javascript:delCategory('${record.id}');"><i class="fa fa-save"></i> 删除</button>
                        <button type="button" class="btn default" onclick="javascript:hideCategory();">取消</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
$(document).ready(function() {
    $('#frm_edit').validate({
        errorElement: 'span',
        errorClass: 'help-block help-block-error',
        focusInvalid: false,
        ignore: '',
        rules: {
        	title: {
                required: true,
                maxlength: 10,
                remote: {
                    url: '${cur_page}?pAct=validate',
                    type: 'post',
                    data: {
                        rule: 'unique',
                        id: '${record.id}',
                    }
                }
            },
        },
        messages: {
        	title: {
                required: '不能为空',
                maxlength: '不超过10个字符',
                remote: '不能相同'
            },
        },
        highlight: function (e) {
            $(e).closest('.form-group').removeClass('has-success').addClass('has-error');
        },
        unhighlight: function (e) {
            $(e).closest('.form-group').removeClass('has-error').addClass('has-success');
        },
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                success: function(resp) {
                    if (resp.retcode == 200) {
                        toastr['success'](resp.msg);
                        loadCategories();
                        $('#action_category').empty();
                    } else {
                        toastr['error'](resp.msg);
                    }
                }
            });
        }
    });

});	

function delCategory(id) {
	
	bootbox.confirm("是否删除？", function (result) {
		if (result) {
			Metronic.blockUI({target: '#content-div', animate: true});
			$.ajax({
				type: "POST",
				url: "${cur_page}?pAct=remove",
						data: {'id': id},
						success: function (resp) {
							Metronic.unblockUI('#content-div');
							if (resp.retcode == 200) {
								toastr['success'](resp.msg);
								loadCategories();
								$('#action_category').empty();
							} else {
								toastr['error'](resp.msg);
							}
						},
						error: function (xhr, ajaxOptions, thrownError) {
							Metronic.unblockUI('#content-div');
							bootbox.alert("发生错误！");
						}
					});
		}
	});
}
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
