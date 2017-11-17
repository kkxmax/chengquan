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
        <i class="fa fa-circle-o"></i>新增内容
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
      </div>
      <div class="actions">
      </div>
    </div>
    <div class="portlet-body">
      <form class="form-horizontal" action="${cur_page}?pAct=editDo" method="post" enctype="multipart/form-data" id="frm_edit">
      	<input type="hidden" name="id" value="${record.id}">
      	<input type="hidden" id="hide_xyleixing_id" value="${record.xyleixingId}">
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
	                <select class="form-control form-filter select2me input-small" id="xyleixing_level1_id" name="xyleixing_level1_id" onchange="xyleixing_level1_id_changed();">
	                	<option value="">选择一级</option>
	                	<c:forEach items="${leixings}" var="item">
	                		<option value="${item.id}" <c:if test="${record.xyleixingLevel1Id == item.id}">selected</c:if>>${item.title}</option>
	                	</c:forEach>
	                </select>
	                &nbsp;
	                <select class="form-control form-filter select2me input-small" id="xyleixing_id" name="xyleixingId">
		            	<option value="">选择二级</option>
		            </select>
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-2 control-label">文章正文: </label>
	            <div class="col-md-8">
	                <textarea name="content" class="form-control autosizeme" rows="10">${record.content}</textarea>
	                <!-- <div id="editor1"></div> -->
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-2 control-label">图片: </label>
	            <div class="col-md-2">
	            	图片1: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath1 != ''}">${C_UPLOAD_PATH}${record.imgPath1}</c:if><c:if test="${record.imgPath1 == null}">${C_EMPTY_HOT_IMAGE}</c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath1">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片2: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath2 != ''}">${C_UPLOAD_PATH}${record.imgPath2}<c:if test="${record.imgPath2 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath2">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片3: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath3 != ''}">${C_UPLOAD_PATH}${record.imgPath3}<c:if test="${record.imgPath3 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath3">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片4: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath4 != ''}">${C_UPLOAD_PATH}${record.imgPath4}<c:if test="${record.imgPath4 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath4">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片5: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath5 != ''}">${C_UPLOAD_PATH}${record.imgPath5}<c:if test="${record.imgPath5 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath5">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	        </div>
	        <div class="form-group">
	            <div class="col-md-offset-2 col-md-2">
	            	图片6: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath6 != ''}">${C_UPLOAD_PATH}${record.imgPath6}<c:if test="${record.imgPath6 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath6">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片7: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath7 != ''}">${C_UPLOAD_PATH}${record.imgPath7}<c:if test="${record.imgPath7 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath7">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片8: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath8 != ''}">${C_UPLOAD_PATH}${record.imgPath8}<c:if test="${record.imgPath8 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath8">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片9: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath9 != ''}">${C_UPLOAD_PATH}${record.imgPath9}<c:if test="${record.imgPath9 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath9">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片10: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath10 != ''}">${C_UPLOAD_PATH}${record.imgPath10}<c:if test="${record.imgPath10 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath10">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	        </div>
	        <div class="form-group">
	            <div class="col-md-offset-2 col-md-2">
	            	图片11: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath11 != ''}">${C_UPLOAD_PATH}${record.imgPath11}<c:if test="${record.imgPath11 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath11">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片12: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath12 != ''}">${C_UPLOAD_PATH}${record.imgPath12}<c:if test="${record.imgPath12 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath12">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片13: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath13 != ''}">${C_UPLOAD_PATH}${record.imgPath13}<c:if test="${record.imgPath13 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath13">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片14: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath14 != ''}">${C_UPLOAD_PATH}${record.imgPath14}<c:if test="${record.imgPath14 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath14">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
	            </div>
	            <div class="col-md-2">
	            	图片15: 
	                <div class="fileinput fileinput-new" data-provides="fileinput">
	                	<div class="fileinput-preview thumbnail" data-trigger="fileinut" style="width: 150px">
	                		<img src="<c:if test="${record.imgPath15 != ''}">${C_UPLOAD_PATH}${record.imgPath15}<c:if test="${record.imgPath15 == null}">${C_EMPTY_HOT_IMAGE}</c:if></c:if>">
	                	</div>
	                	<div>
	                		<span class="btn default btn-file">
		                		<span class="fileinput-new">选择图片</span>
		                		<span class="fileinput-exists">修改</span>
		                		<input type="file" name="imgPath15">
	                		</span>
	                		<a href="javascript:;" class="btn red fileinput-exists" data-dismiss="fileinput">删除</a>
	                	</div>
	                </div>
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
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/autosize/autosize.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js" ></script>
<script>
var ue;
$(document).ready(function () {
	
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
            'xyleixingId': {
                required: true,
            },
            'content': {
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
            'xyleixingId': {
                required: "请选择二级行业",
            },
            'content': {
                required: "请输入热点内容",
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
	var hide_xyleixing_id = $('#hide_xyleixing_id').val();
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
							if(record.id == hide_xyleixing_id) {
								html += "<option value=" + record.id + " selected>" + record.title + "</option>";
							}
							else {
								html += "<option value=" + record.id + ">" + record.title + "</option>";	
							}
						}
						$('#xyleixing_id').html(html);
						$('#xyleixing_id').select2();
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
