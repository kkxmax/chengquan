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
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/jcrop/css/jquery.Jcrop.min.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/admin/pages/css/image-crop.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" />

<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jcrop/js/jquery.Jcrop.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput-crop.js" ></script>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp"%>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
	<div class="portlet box blue-hoki">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-circle-o"></i>新增视频
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse" data-original-title="" title=""></a>
			</div>
			<div class="actions"></div>
		</div>
		<div class="portlet-body form">
			<form action="${cur_page}?pAct=upload" id="_frm" class="form-horizontal" method="post" accept-charset="utf-8" enctype="multipart/form-data">
				<div class="form-body">
					<div class="form-group">
						<label class="col-md-2 control-label">视频名称:</label>
						<div class="col-md-4">
							<input type="text" id="title" name="title" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">上传视频:</label>
						<div class="col-md-4">
							<div class="error-pos">
								<span class="btn default btn-file">
									<span class="fileinput-new">选择视频</span>
									<input type="file" name="video_file_url" id="video_file_url">
								</span>
								<span id="video_file_info" class="fileinput-filename"></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">视频介绍:</label>
						<div class="col-md-4">
							<textarea id="comment" name="comment" rows="5" class="form-control"></textarea>
						</div>
					</div>
				</div>
				<div class="form-actions">
					<div class="row">
						<div class="col-md-11" style="text-align: right;">
							<button type="submit" class="btn green"><i class="fa fa-save"></i> 提交 </button>
							<button type="button" class="btn default" onclick="location.href='${cur_page}'">取消</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp"%>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>
	var g_video_file;
	jQuery(document).ready(function() {

		// video upload part
		$('#video_file_url').on('change', function(e){
			var files = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
			e.stopPropagation();
			if (files.length === 0) {
				$('#video_file_info').html('');
				return;
			}

			var file = files[0];
			g_video_file = file;

			if ((typeof file.type !== "undefined" ? file.type.match(/^video\/(avi|mp4)$/) : file.name.match(/\.(avi|mp4)$/i)) && typeof FileReader !== "undefined") {
				$('#video_file_info').html(file.name + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + file.size + '&nbsp;bytes');
			} else {
				$('#video_file_url').val('');
				$('#video_file_info').html('');
				toastr['error']('视频文件支持MP4、AVI格式。');
			}
		});

		// Form Submit part
		var form = $('#_frm');
		form.validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block help-block-error', // default input error message class
			rules: {
				title: {
					required: true,
					maxlength: 10,
				},
				video_file_url: {
					required: true,
				},
				comment: {
					maxlength: 500,
				}
			},
			messages: {// custom messages for radio buttons and checkboxes
				title: {
					required: "请输入视频名称",
					maxlength: "不超过10个字符",
				},
				video_file_url: {
					required: "请选择视频"
				},
				comment: {
					maxlength: "不超过500个字符",
				}
			},
			highlight: function(e) {
                $(e).closest('.form-group').addClass('has-error');
            },
            unhighlight: function(e) {
                $(e).closest('.form-group').removeClass('has-error');
            },
		});

		form.ajaxForm({
			beforeSubmit: function(formData, $form, options) {
				if (!$form.valid()) {
					return false;
				}

				if (g_video_file.size >= 20 * 1024 * 1024) {
					toastr['error']('视频不超过20M。');
					return false;
				}
				var filename = $('#video_file_url').val();
				var pos = filename.indexOf(".");
				var ext = filename.substr(pos + 1, filename.length - pos).toLowerCase().toString();
				if (ext != "avi" && ext != "mp4") {
					toastr['error']("视频文件支持MP4、AVI格式，不超过20M。");
					return false;
				}
				Metronic.blockUI({target: '#content-div', animate: true});
				return true;
			},
			success: function(resp) {
				Metronic.unblockUI('#content-div');
				if (resp.retcode == 200) {
					location.href = "${cur_page}";
				} else {
					toastr['error'](resp.msg);
				}
			}
		});
	});
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
