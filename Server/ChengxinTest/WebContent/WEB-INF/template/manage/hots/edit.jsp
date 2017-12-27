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
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/jcrop/css/jquery.Jcrop.min.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/admin/pages/css/image-crop.css" />
<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css" />

<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jcrop/js/jquery.Jcrop.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-fileinput/bootstrap-fileinput-crop.js" ></script>
<style>
	#lbl_img_original, #lbl_img_original2, #lbl_img_original3 {position: absolute; top: 36%; left: 127px; font-size: 23px; font-weight: bold;}
	#lbl_img_preview, #lbl_img_preview2, #lbl_img_preview3 {position: absolute; top: 38%; left: 45px; font-size: 18px; font-weight: bold;}
</style>
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
    <div class="portlet-body hot">
      <form class="form-horizontal" action="${cur_page}?pAct=editDo" method="post" enctype="multipart/form-data" id="frm_edit">
      	<input type="hidden" name="id" value="${record.id}">
      	<input type="hidden" id="hide_xyleixing_id" value="${record.xyleixingId}">
      	<div class="form-body">
	      	<div class="form-group">
	            <label class="col-md-1 control-label">文章标题: </label>
	            <div class="col-md-11">
	                <input type="text" class="form-control" name="title" value="${record.title}">
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-1 control-label">行业: </label>
	            <div class="col-md-11">
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
	            <label class="col-md-1 control-label">简介: </label>
	            <div class="col-md-11">
	                <textarea class="form-control autosizeme" name="summary" maxlength="255">${record.summary}</textarea>
	            </div>
	        </div>
	        <div class="form-group">
	            <label class="col-md-1 control-label">文章正文: </label>
	            <div class="col-md-11">
	                <textarea id="editor" name="editor" class="form-control ckeditor" rows="300">${record.contentString}</textarea>
	                <input type="hidden" id="content" name="content" value="">
	            </div>
	        </div>
	        <div class="form-group" style="margin-top: 150px">
	            <label class="col-md-1 control-label">图片: </label>
	            <div class="col-md-11">
	            	<div class="row">
	            		<div class="col-md-4">
	            			<div class="row">
	            				<div class="col-md-12">
	            					<img src="<c:if test="${record.imgPath1 != '' && record.imgPath1 != null}">${C_UPLOAD_PATH}${record.imgPath1}</c:if><c:if test="${record.imgPath1 == null || record.imgPath1 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="img" class="img-responsive" />
									<c:if test="${record.imgPath1 == null || record.imgPath1 == ''}">
										<label id="lbl_img_original">未选择图片</label>
									</c:if>
									<div style="margin-top: 10px;">
										<span class="btn default btn-file">
											<span class="fileinput-new">选择图片</span>
											<input type="file" name="imgPath1" id="imgPath1">
										</span>
									</div>
	            				</div>
	            			</div>
	            			<div class="row">
	            				<div class="col-md-12">
	            					<div id="preview-pane">
										<div class="preview-container">
											<img src="<c:if test="${record.imgPath1 != '' && record.imgPath1 != null}">${C_UPLOAD_PATH}${record.imgPath1}</c:if><c:if test="${record.imgPath1 == null || record.imgPath1 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="jcrop-preview" />
											<c:if test="${record.imgPath1 == null || record.imgPath1 == ''}">
												<label id="lbl_img_preview">暂无图片</label>
											</c:if>
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
	            		<div class="col-md-4">
	            			<div class="row">
	            				<div class="col-md-12">
	            					<img src="<c:if test="${record.imgPath2 != '' && record.imgPath2 != null}">${C_UPLOAD_PATH}${record.imgPath2}</c:if><c:if test="${record.imgPath2 == null || record.imgPath2 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="img2" class="img-responsive" />
									<c:if test="${record.imgPath2 == null || record.imgPath2 == ''}">
										<label id="lbl_img_original2">未选择图片</label>
									</c:if>
									<div style="margin-top: 10px;">
										<span class="btn default btn-file">
											<span class="fileinput-new">选择图片</span>
											<input type="file" name="imgPath2" id="imgPath2">
										</span>
									</div>
	            				</div>
	            			</div>
	            			<div class="row">
	            				<div class="col-md-12">
	            					<div id="preview-pane2">
										<div class="preview-container">
											<img src="<c:if test="${record.imgPath2 != '' && record.imgPath2 != null}">${C_UPLOAD_PATH}${record.imgPath2}</c:if><c:if test="${record.imgPath2 == null || record.imgPath2 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="jcrop-preview2" />
											<c:if test="${record.imgPath2 == null || record.imgPath2 == ''}">
												<label id="lbl_img_preview2">暂无图片</label>
											</c:if>
											<input type="hidden" id="screen_w2" name="sw2"/>
											<input type="hidden" id="screen_h2" name="sh2"/>
											<input type="hidden" id="crop_x2" name="x2"/>
											<input type="hidden" id="crop_y2" name="y2"/>
											<input type="hidden" id="crop_w2" name="w2"/>
											<input type="hidden" id="crop_h2" name="h2"/>
										</div>
									</div>
	            				</div>
	            			</div>
	            		</div>
	            		<div class="col-md-4">
	            			<div class="row">
	            				<div class="col-md-12">
	            					<img src="<c:if test="${record.imgPath3 != '' && record.imgPath3 != null}">${C_UPLOAD_PATH}${record.imgPath3}</c:if><c:if test="${record.imgPath3 == null || record.imgPath3 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="img3" class="img-responsive" />
									<c:if test="${record.imgPath3 == null || record.imgPath3 == ''}">
										<label id="lbl_img_original3">未选择图片</label>
									</c:if>
									<div style="margin-top: 10px;">
										<span class="btn default btn-file">
											<span class="fileinput-new">选择图片</span>
											<input type="file" name="imgPath3" id="imgPath3">
										</span>
									</div>
	            				</div>
	            			</div>
	            			<div class="row">
	            				<div class="col-md-12">
	            					<div id="preview-pane3">
									<div class="preview-container">
										<img src="<c:if test="${record.imgPath3 != '' && record.imgPath3 != null}">${C_UPLOAD_PATH}${record.imgPath3}</c:if><c:if test="${record.imgPath3 == null || record.imgPath3 == ''}">${C_UPLOAD_PATH}${C_CROP_BG_IMAGE}</c:if>" id="jcrop-preview3" />
										<c:if test="${record.imgPath3 == null || record.imgPath3 == ''}">
											<label id="lbl_img_preview3" style="">暂无图片</label>
										</c:if>
										<input type="hidden" id="screen_w3" name="sw3"/>
										<input type="hidden" id="screen_h3" name="sh3"/>
										<input type="hidden" id="crop_x3" name="x3"/>
										<input type="hidden" id="crop_y3" name="y3"/>
										<input type="hidden" id="crop_w3" name="w3"/>
										<input type="hidden" id="crop_h3" name="h3"/>
									</div>
								</div>
	            				</div>
	            			</div>
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
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/ckeditor/ckeditor.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/autosize/autosize.min.js" ></script>
<script>

var g_img_file, g_img_file2, g_img_file3;
var g_crop_src_img_width = 300;
var g_crop_ratio = 228/160;
var g_default_crop_width = 200;
var recordImgPath1 = "${record.imgPath1}";
var recordImgPath2 = "${record.imgPath2}";
var recordImgPath3 = "${record.imgPath3}";

$(document).ready(function () {

	var input = $('#imgPath1');
	var img_original = $('#img');
	var input2 = $('#imgPath2');
	var img_original2 = $('#img2');
	var input3 = $('#imgPath3');
	var img_original3 = $('#img3');

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
				var img_file = new Image();
				img_file.onload = function() {
					$('#img').css('width', g_crop_src_img_width + 'px');
					$('#img').css('height', img_file.height * g_crop_src_img_width / img_file.width + 'px');
					
					img_original.attr('src', re.target.result);
					$(img_original).next().find('img').each(function(index, element) {
						$(element).attr('src', re.target.result);
					});
					$('#jcrop-preview').attr('src', re.target.result);
					
					if (jcrop_api)
						jcrop_api.destroy();
					$('#img').Jcrop({
						onChange: updatePreview,
						onSelect: updatePreview,
						aspectRatio: g_crop_ratio
					}, function() {
						// Use the API to get the real image size
						var bounds = this.getBounds();
						boundx = bounds[0];
						boundy = bounds[1];
						// Store the API in the jcrop_api variable
						jcrop_api = this;
						// Move the preview into the jcrop container for css positioning
						$preview.appendTo(jcrop_api.ui.holder);
						
						jcrop_api.setSelect([(boundx - g_default_crop_width) > 0 ? (boundx - g_default_crop_width) : 0 , 0, boundx, 1]);
					});
				};
				img_file.setAttribute('src', re.target.result);
			};
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
		aspectRatio: g_crop_ratio
	}, function() {
		// Use the API to get the real image size
		var bounds = this.getBounds();
		boundx = bounds[0];
		boundy = bounds[1];
		// Store the API in the jcrop_api variable
		jcrop_api = this;
		// Move the preview into the jcrop container for css positioning
		$preview.appendTo(jcrop_api.ui.holder);
		
		if(recordImgPath1) {
			jcrop_api.setSelect([(boundx - g_default_crop_width) > 0 ? (boundx - g_default_crop_width) : 0 , 0, boundx, 1]);	
		}
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
	
	input2.on('change', function(e){
		var files2 = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
		e.stopPropagation();
		if (files2.length === 0) {
			return;
		}

		var file2 = files2[0];
		g_img_file2 = file2;

		if ((typeof file2.type !== "undefined" ? file2.type.match(/^image\/(png|jpeg)$/) : file2.name.match(/\.(png|jpe?g)$/i)) && typeof FileReader !== "undefined") {
			
			$('#lbl_img_original2, #lbl_img_preview2').hide();

			var reader2 = new FileReader();
			reader2.onload = function(re) {
				var img_file2 = new Image();
				img_file2.onload = function() {
					$('#img2').css('width', g_crop_src_img_width + 'px');
					$('#img2').css('height', img_file2.height * g_crop_src_img_width / img_file2.width + 'px');
					
					img_original2.attr('src', re.target.result);
					$(img_original2).next().find('img').each(function(index, element) {
						$(element).attr('src', re.target.result);
					});
					$('#jcrop-preview2').attr('src', re.target.result);
					
					if (jcrop_api2)
						jcrop_api2.destroy();
					$('#img2').Jcrop({
						onChange: updatePreview2,
						onSelect: updatePreview2,
						aspectRatio: g_crop_ratio
					}, function() {
						// Use the API to get the real image size
						var bounds2 = this.getBounds();
						boundx2 = bounds2[0];
						boundy2 = bounds2[1];
						// Store the API in the jcrop_api variable
						jcrop_api2 = this;
						// Move the preview into the jcrop container for css positioning
						$preview2.appendTo(jcrop_api2.ui.holder);
						
						jcrop_api2.setSelect([(boundx2 - g_default_crop_width) > 0 ? (boundx2 - g_default_crop_width) : 0 , 0, boundx2, 1]);	
					});
				};
				img_file2.setAttribute('src', re.target.result);
			};
			reader2.readAsDataURL(file2);
		} else {
			toastr['error']('图片格式错误，要求是jpg、jpeg、png格式。');
			return;
		}
	});

	// image crop part 2
	var jcrop_api2,
			boundx2,
			boundy2,
			// Grab some information about the preview pane
			$preview2 = $('#preview-pane2'),
			$pcnt2 = $('#preview-pane2 .preview-container'),
			$pimg2 = $('#preview-pane2 .preview-container img'),
			xsize2 = $pcnt2.width(),
			ysize2 = $pcnt2.height();

	$('#img2').Jcrop({
		onChange: updatePreview2,
		onSelect: updatePreview2,
		aspectRatio: g_crop_ratio
	}, function() {
		// Use the API to get the real image size
		var bounds2 = this.getBounds();
		boundx2 = bounds2[0];
		boundy2 = bounds2[1];
		// Store the API in the jcrop_api variable
		jcrop_api2 = this;
		// Move the preview into the jcrop container for css positioning
		$preview2.appendTo(jcrop_api2.ui.holder);
		
		if(recordImgPath2) {
			jcrop_api2.setSelect([(boundx2 - g_default_crop_width) > 0 ? (boundx2 - g_default_crop_width) : 0 , 0, boundx2, 1]);
		}
	});

	function updatePreview2(c) {
		if (parseInt(c.w) > 0) {
			var rx2 = xsize2 / c.w;
			var ry2 = ysize2 / c.h;

			$('#screen_w2').val($('#img2').width());
			$('#screen_h2').val($('#img2').height());
			$('#crop_x2').val(c.x);
			$('#crop_y2').val(c.y);
			$('#crop_w2').val(c.w);
			$('#crop_h2').val(c.h);

			$pimg2.css({
				width: Math.round(rx2 * boundx2) + 'px',
				height: Math.round(ry2 * boundy2) + 'px',
				marginLeft: '-' + Math.round(rx2 * c.x) + 'px',
				marginTop: '-' + Math.round(ry2 * c.y) + 'px'
			});
		}
	}
	
	input3.on('change', function(e){
		var files = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
		e.stopPropagation();
		if (files.length === 0) {
			return;
		}

		var file = files[0];
		g_img_file3 = file;

		if ((typeof file.type !== "undefined" ? file.type.match(/^image\/(png|jpeg)$/) : file.name.match(/\.(png|jpe?g)$/i)) && typeof FileReader !== "undefined") {
			
			$('#lbl_img_original3, #lbl_img_preview3').hide();

			var reader = new FileReader();
			reader.onload = function(re) {
				var img_file = new Image();
				img_file.onload = function() {
					$('#img3').css('width', g_crop_src_img_width + 'px');
					$('#img3').css('height', img_file.height * g_crop_src_img_width / img_file.width + 'px');
					
					img_original3.attr('src', re.target.result);
					$(img_original3).next().find('img').each(function(index, element) {
						$(element).attr('src', re.target.result);
					});
					$('#jcrop-preview3').attr('src', re.target.result);
					
					if (jcrop_api3)
						jcrop_api3.destroy();
					$('#img3').Jcrop({
						onChange: updatePreview3,
						onSelect: updatePreview3,
						aspectRatio: g_crop_ratio
					}, function() {
						// Use the API to get the real image size
						var bounds = this.getBounds();
						boundx3 = bounds[0];
						boundy3 = bounds[1];
						// Store the API in the jcrop_api variable
						jcrop_api3 = this;
						// Move the preview into the jcrop container for css positioning
						$preview3.appendTo(jcrop_api3.ui.holder);
						
						jcrop_api3.setSelect([(boundx3 - g_default_crop_width) > 0 ? (boundx3 - g_default_crop_width) : 0 , 0, boundx3, 1]);
					});
				};
				img_file.setAttribute('src', re.target.result);
			};
			reader.readAsDataURL(file);
		} else {
			toastr['error']('图片格式错误，要求是jpg、jpeg、png格式。');
			return;
		}
	});

	// image crop part 2
	var jcrop_api3,
			boundx3,
			boundy3,
			// Grab some information about the preview pane
			$preview3 = $('#preview-pane3'),
			$pcnt3 = $('#preview-pane3 .preview-container'),
			$pimg3 = $('#preview-pane3 .preview-container img'),
			xsize3 = $pcnt3.width(),
			ysize3 = $pcnt3.height();

	$('#img3').Jcrop({
		onChange: updatePreview3,
		onSelect: updatePreview3,
		aspectRatio: g_crop_ratio
	}, function() {
		// Use the API to get the real image size
		var bounds = this.getBounds();
		boundx3 = bounds[0];
		boundy3 = bounds[1];
		// Store the API in the jcrop_api variable
		jcrop_api3 = this;
		// Move the preview into the jcrop container for css positioning
		$preview3.appendTo(jcrop_api3.ui.holder);
		
		if(recordImgPath3) {
			jcrop_api3.setSelect([(boundx3 - g_default_crop_width) > 0 ? (boundx3 - g_default_crop_width) : 0 , 0, boundx3, 1]);	
		}
	});

	function updatePreview3(c) {
		if (parseInt(c.w) > 0) {
			var rx = xsize3 / c.w;
			var ry = ysize3 / c.h;

			$('#screen_w3').val($('#img3').width());
			$('#screen_h3').val($('#img3').height());
			$('#crop_x3').val(c.x);
			$('#crop_y3').val(c.y);
			$('#crop_w3').val(c.w);
			$('#crop_h3').val(c.h);

			$pimg3.css({
				width: Math.round(rx * boundx3) + 'px',
				height: Math.round(ry * boundy3) + 'px',
				marginLeft: '-' + Math.round(rx * c.x) + 'px',
				marginTop: '-' + Math.round(ry * c.y) + 'px'
			});
		}
	}
	
	var editor = 'editor';
	var allowTags = [
		'img[!src,id,alt,width,height,align,valign];',
		'table[width,height,border,cellspacing,cellpadding,align];caption; thead; tbody; tr; th[width,height,align,valign,colspan,rowspan]; td[width,height,align,valign,colspan,rowspan];',
		'a[name,href,title,target]{color};',
		'p{color,text-align};p span{!color};',
		'div{!page-break-after};div span{!display};',
		'embed[*];',
		'ul; ol; li; dl; dt; dd; pre; blockquote; br; hr; strong; i; u; em;'
	];
	var disallowTags = [
		'td{*};th{*};embed[style];* span[!style];*{float,position,z-index,margin,padding,border};'
	];
	CKEDITOR.replace(editor, {
//		baseHref     : './Public/upload/',
		extraPlugins : 'pasteupload',
		uiColor      : '#F5F5F5',
		fullPage     : false,
		height		: 500,
		toolbar      : 'App',
		toolbar_Full : [
			['Source','-','NewPage','Preview','-','Templates','ShowBlocks'],
			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
			['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
			['BidiLtr', 'BidiRtl'],
			'/',
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
			['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
			['Link','Unlink','Anchor'],
			['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
			'/',
			['Styles','Format','Font','FontSize'],
			['TextColor','BGColor']
		],
		toolbar_Basic : [['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','ShowBlocks']],
		toolbar_App : [
			['Source','-','NewPage','Preview','-','Templates','ShowBlocks'],
			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
			['Styles','Format','Font','FontSize'],
			['TextColor','BGColor'],
			'/',
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
			['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
			['Link','Unlink','Anchor'],
			['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
			'/',
			['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
			['BidiLtr', 'BidiRtl']
		],

		image_previewText         : ' ',
		allowedContent            : allowTags.join(' '),
		disallowedContent         : disallowTags.join(' '),
		startupOutlineBlocks      : false,
		autoUpdateElement         : true,
		forcePasteAsPlainText     : false,
		pasteFromWordRemoveFontStyles : true,
		pasteFromWordRemoveStyle  : true,
		filebrowserImageUploadUrl : '${cur_page}?pAct=uploadEditorImage',
		filebrowserFlashUploadUrl : '${cur_page}?pAct=uploadEditorFlash',
		filebrowserUploadUrl      : '${cur_page}?pAct=uploadEditorLink',

		wordImagePasteApiUrl      : 'http://127.0.0.1:10101',  //建议端口大于10000
		imagePasteUploadEnabled   : true,
		imageRemoteUploadUrl      : '${cur_page}?pAct=uploadEditorRemoteImage',
		imageRemoteUploadBaseHref : './Public/upload/'         //只用来过滤不用上传远程图片地址
	});
	
	$('#frm_edit').validate({
        errorElement: 'span',
        errorClass: 'help-block-error',
        focusInvalid: false,
        ignore: '',
        rules: {
        	'title': {
                required: true,
                maxlength: 255,
            },
            'summary': {
                required: true,
                maxlength: 255,
            },
        	'xyleixing_level1_id': {
                required: true,
            },
            'xyleixingId': {
                required: true,
            },
        },
        messages: {
        	'title': {
                required: "请输入文章标题",
                maxlength: "最多255数字或文字",
            },
            'summary': {
                required: "请输入简绍",
                maxlength: "最多255数字或文字",
            },
            'xyleixing_level1_id': {
                required: "请选择一级行业",
            },
            'xyleixingId': {
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
            $(form).ajaxSubmit({
                beforeSubmit: function(formData, $form, options) {
                	var content = CKEDITOR.instances[editor].getData();
                	/* var contentText = CKEDITOR.instances.editor.document.getBody().getText(); */
                	if(content == '') {
                		toastr['error']("热点内容不能为空。");
                		return false;
                	}
                	formData.push({'name': 'content', 'value': content});

                	if(g_img_file) {
                		if (g_img_file.size >= 10 * 1024 * 1024) {
        					toastr['error']('图片不超过10M。');
        					return false;
        				}

        				var filename = $('#imgPath1').val();
        				var pos = filename.indexOf(".");
        				var ext = filename.substr(pos + 1, filename.length - pos).toLowerCase().toString();
        				if (ext != "png" && ext != "jpeg" && ext != "jpg") {
        					toastr['error']("图片格式错误，要求是jpg、jpeg、png格式。");
        					return false;
        				}
                	}
                	if(g_img_file2) {
                		if (g_img_file2.size >= 10 * 1024 * 1024) {
        					toastr['error']('图片不超过10M。');
        					return false;
        				}

        				var filename = $('#imgPath2').val();
        				var pos = filename.indexOf(".");
        				var ext = filename.substr(pos + 1, filename.length - pos).toLowerCase().toString();
        				if (ext != "png" && ext != "jpeg" && ext != "jpg") {
        					toastr['error']("图片格式错误，要求是jpg、jpeg、png格式。");
        					return false;
        				}
                	}
                	if(g_img_file3) {
                		if (g_img_file3.size >= 10 * 1024 * 1024) {
        					toastr['error']('图片不超过10M。');
        					return false;
        				}

        				var filename = $('#imgPath3').val();
        				var pos = filename.indexOf(".");
        				var ext = filename.substr(pos + 1, filename.length - pos).toLowerCase().toString();
        				if (ext != "png" && ext != "jpeg" && ext != "jpg") {
        					toastr['error']("图片格式错误，要求是jpg、jpeg、png格式。");
        					return false;
        				}
                	}
    				
                    Metronic.blockUI({target: '#content-div',animate: true});
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
