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
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
  <div class="portlet box blue-hoki">
    <div class="portlet-title">
      <div class="caption">
        <i class="fa fa-circle-o"></i>新增内容
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
      </div>
      <div class="actions">
      </div>
    </div>
    <div class="portlet-body">
      <form class="form-horizontal" action="${cur_page}?pAct=editDo" method="post" id="frm_edit">
      	<input type="hidden" name="id" value="${record.id}">
      	<input type="hidden" id="hide_kind" value="${record.kind}">
      	<div class="form-body">
	      	<div class="form-group">
	            <label class="col-md-2 control-label">文章标题: </label>
	            <div class="col-md-8">
	                <input type="text" class="form-control" name="title" value="${record.title}">
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-2 control-label">行业: </label>
	            <div class="col-md-10">
	                <select class="form-control form-filter select2me input-small" id="xyleixing_level1_id" onchange="xyleixing_level1_id_changed();">
	                	<option value="">选择一级</option>
	                	<c:forEach items="${leixings}" var="item">
	                		<option value="${item.id}" <c:if test="${record.xyleixing_level1_id == item.id}">selected</c:if>>${item.title}</option>
	                	</c:forEach>
	                </select>
	                &nbsp;
	                <select class="form-control form-filter select2me input-small" id="kind" name="kind">
		            	<option value="">选择二级</option>
		            </select>
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-2 control-label">文章正文: </label>
	            <div class="col-md-8">
	                <textarea name="content" class="hide">${record.content}</textarea>
	                <div id="editor1"></div>
	            </div>
	        </div>
      	</div>
      	<div class="form-actions">
            <div class="row">
                <div class="col-md-offset-5 col-md-4">
                    <a href="${cur_page}" class="btn btn-default"> 取消 </a>
                    <button class="btn green"><i class="fa fa-save"></i> 确认</button>
                </div>
            </div>
        </div>
      </form>
    </div>
  </div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp" %>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/third-party/codemirror/codemirror.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/third-party/zeroclipboard/ZeroClipboard.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/ueditor.config.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/ueditor.all.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ueditor-1.4.3/lang/zh-cn/zh-cn.js" ></script>
<script>
var ue;
$(document).ready(function () {
	ue = UE.getEditor('editor1', {
        onready: function() {
            this.setContent($("textarea[name=content]").val(), false);
            $('#editor1').find('#edui1').css("width", "100%");
        }
    });
	
	$('#frm_edit').validate({
        errorElement: 'span',
        errorClass: 'help-block-error',
        focusInvalid: false,
        ignore: '',
        rules: {
        	'title': {
                required: true,
            },
        	'xyleixing_level1_id': {
                required: true,
            },
            'kind': {
                required: true,
            },
        },
        messages: {
        	'title': {
                required: "请输入文章标题",
            },
            'xyleixing_level1_id': {
                required: "请选择一级行业",
            },
            'kind': {
                required: "请选择二级行业",
            },
        },
        highlight: function(e) {
            $(e).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(e) {
            $(e).closest('.form-group').removeClass('has-error');
        },
        submitHandler: function(form) {
        	$("textarea[name=content]").val(UE.getEditor('editor1').getContent());
            $(form).ajaxSubmit({
                beforeSubmit: function() {
                    Metronic.blockUI({target: '#content-div',animate: true});
                },
                success: function(resp) {
                    Metronic.unblockUI('#content-div');
                    if (resp.retcode == 200) {
                        toastr['success'](resp.msg);
                    } else {
                        toastr['error'](resp.msg);
                    }
                }
            });
        }
	});
	
	xyleixing_level1_id_changed();
	
});

function xyleixing_level1_id_changed() {
	var hide_kind = $('#hide_kind').val();
	$.ajax({
		type: "POST",
		url: "${cur_page}?pAct=getLevel2Xyleixings",
				data: {'level1leixing': $('#xyleixing_level1_id').val()},
				success: function (resp) {
					if (resp.retcode == 200) {
						records = resp.records;
						html = "<option value=''>选择二级</option>";
						for(i=0; i<records.length; i++) {
							record = records[i];
							if(record.id == hide_kind) {
								html += "<option value=" + record.id + " selected>" + record.title + "</option>";
							}
							else {
								html += "<option value=" + record.id + ">" + record.title + "</option>";	
							}
						}
						$('#kind').html(html);
						$('#kind').select2();
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					bootbox.alert("发生错误！");
				}
			});
}
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
