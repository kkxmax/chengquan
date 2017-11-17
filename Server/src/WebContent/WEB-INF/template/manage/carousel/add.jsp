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
				<i class="fa fa-circle-o"></i>轮播图列表
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse" data-original-title=""
					title=""></a>
			</div>
			<div class="actions"></div>
		</div>
		<div class="portlet-body form">
			<form action="${cur_page}?pAct=upload" id="_frm" class="form-horizontal" method="post" accept-charset="utf-8" enctype="multipart/form-data">
				<div class="form-body">
					<div class="form-group">
						<label class="col-md-2 control-label">* 类型:</label>
						<div class="col-md-4">
							<div class="radio-list">
								<label class="radio-inline"><input type="radio" name="kind" id="kind1" value="1" onclick="clickMediaKind(1);" checked> 图片</label>
								<label class="radio-inline"><input type="radio" name="kind" id="kind2" value="2" onclick="clickMediaKind(2);"> 视频</label>
							</div>
						</div>
					</div>
					<div class="form-group" id="part_video_title" style="display: none;">
						<label class="col-md-2 control-label">* 视频名称:</label>
						<div class="col-md-4">
							<select name="video_id" class="form-control select2me">
								<option value="">请选择视频名称。</option>
								<c:forEach items="${videosList}" var="video" varStatus="i">
								<option value="${video.id}">${video.title}</option>
								</c:forEach>
							</select>
							默认显示所有活动名称。可以按活动名称搜索选择
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">* 排序:</label>
						<div class="col-md-4">
							<input type="number" id="ord" name="ord" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">* 上传图片并裁剪:</label>
						<div class="col-md-7">
							<img src="/BFIP/Upload/image/carousel/bg.png" id="img" class="img-responsive" />
							<label id="lbl_img_original" style="position: absolute; top: 48%; left: 295px; font-size: 23px; font-weight: bold;">未选择图片</label>
							<div style="margin-top: 10px;">
								<span class="btn default btn-file">
									<span class="fileinput-new">选择图片</span>
									<input type="file" name="img_file_url" id="img_file_url">
								</span>
							</div>
						</div>
						<div class="col-md-3">
							<div id="preview-pane">
								<div class="preview-container" style="width: 175px; height: 58px">
									<img src="/BFIP/Upload/image/carousel/bg.png" class="jcrop-preview" />
									<label id="lbl_img_preview" style="position: absolute; top: 46%; left: 60px; font-size: 18px; font-weight: bold;">暂无图片</label>
									<input type="hidden" id="screen_w" name="sw"/>
									<input type="hidden" id="screen_h" name="sh"/>
									<input type="hidden" id="crop_x" name="x"/>
									<input type="hidden" id="crop_y" name="y"/>
									<input type="hidden" id="crop_w" name="w"/>
									<input type="hidden" id="crop_h" name="h"/>
								</div>
							</div>
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
	var g_sel_index_media = 1;
	var g_img_file;
	function clickMediaKind(kind) {
		g_sel_index_media = kind;
		if (kind == 1) {
			$('#part_video_title').hide();
		} else {
			$('#part_video_title').show();
		}
	}

	jQuery(document).ready(function() {
		toastr.options = {
			"closeButton": true,
			"timeOut": "2000",
		};

		// image upload part
		var input = $('#img_file_url');
		var img_original = $('#img');
		input.on('change', function(e){
			var files = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
			e.stopPropagation();
			if (files.length === 0) {
				return;
			}

			var file = files[0];
			g_img_file = file;

			if ((typeof file.type !== "undefined" ? file.type.match(/^image\/(png|jpeg)$/) : file.name.match(/\.(png|jpe?g)$/i)) && typeof FileReader !== "undefined") {
				$('#lbl_img_original, #lbl_img_preview').hide();

				var reader = new FileReader();
				reader.onload = function(re) {
					img_original.attr('src', re.target.result);
					$(img_original).next().find('img').each(function(index, element) {
						$(element).attr('src', re.target.result);
					});
				}
				reader.readAsDataURL(file);
			} else {
				toastr['error']('图片格式错误，要求是jpg、jpeg、png格式。');
				return;
			}
		});

		// image crop part
		var jcrop_api,
				boundx,
				boundy,
				// Grab some information about the preview pane
				$preview = $('#preview-pane'),
				$pcnt = $('#preview-pane .preview-container'),
				$pimg = $('#preview-pane .preview-container img'),
				xsize = $pcnt.width(),
				ysize = $pcnt.height();

		$('#img').Jcrop({
			onChange: updatePreview,
			onSelect: updatePreview,
			aspectRatio: 3
		}, function() {
			// Use the API to get the real image size
			var bounds = this.getBounds();
			boundx = bounds[0];
			boundy = bounds[1];
			// Store the API in the jcrop_api variable
			jcrop_api = this;
			// Move the preview into the jcrop container for css positioning
			$preview.appendTo(jcrop_api.ui.holder);
		});

		function updatePreview(c) {
			if (parseInt(c.w) > 0) {
				var rx = xsize / c.w;
				var ry = ysize / c.h;

				$('#screen_w').val($('#img').width());
				$('#screen_h').val($('#img').height());
				$('#crop_x').val(c.x);
				$('#crop_y').val(c.y);
				$('#crop_w').val(c.w);
				$('#crop_h').val(c.h);

				$pimg.css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			}
		}

		// Form Submit part
		var form = $('#_frm');
		form.validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block help-block-error', // default input error message class
			rules: {
				video_id: {
					required: function() {
						return g_sel_index_media == 2;
					}
				},
				ord: {
					required: true
				},
				img_file_url: {
					required: true
				}
			},
			messages: {// custom messages for radio buttons and checkboxes
				video_id: {
					required: "请选择视频名称。"
				},
				ord: {
					required: "请输入排序。"
				},
				img_file_url: {
					required: "请选择图片并裁剪。"
				}
			},
		});

		form.ajaxForm({
			beforeSubmit: function(formData, $form, options) {
				if (!$form.valid()) {
					return false;
				}

				if ($('#crop_x').val() == 0 || $('#crop_y').val() == 0 || $('#crop_w').val() == 0 || $('#crop_h').val() == 0) {
					toastr['error']('选择图片。');
					return false;
				}

				if (g_img_file.size >= 10 * 1024 * 1024) {
					toastr['error']('图片不超过10M。');
					return false;
				}

				var filename = $('#img_file_url').val();
				var pos = filename.indexOf(".");
				var ext = filename.substr(pos + 1, filename.length - pos).toLowerCase().toString();
				if (ext != "png" && ext != "jpeg" && ext != "jpg") {
					toastr['error']("图片格式错误，要求是jpg、jpeg、png格式。");
					return false;
				}

				return true;
			},
			success: function(resp) {
				if (resp.flag == true) {
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
