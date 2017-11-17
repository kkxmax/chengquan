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
	<div class="portlet box blue-hoki">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-circle-o"></i>角色管理
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse"></a> <a href="javascript:;"
					class="reload" onclick="javascript:loadTable();"></a>
			</div>
			<div class="actions">
				<a class="btn btn-default btn-sm" href="role.html?pAct=add" data-target='#global-modal' data-toggle='modal'><i
					class="fa fa-plus"></i> 新增</a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="row margin-bottom-10">
				<form class="form-inline" id="search-form">
					<div class="form-body col-md-12 col-sm-12"></div>
				</form>
			</div>
			<div class="table-container">
				<table class="table table-striped table-bordered table-hover"
					id="table-data">
					<thead>
						<tr>
							<th>序号</th>
							<th>用户角色</th>
							<th>功能设置</th>
							<th style="width: 120px">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp"%>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>
function loadTable() {
	$('#table-data').DataTable().ajax.reload();
}

jQuery(document).ready(function () {
	$(".date-picker").datepicker($.extend({}, {language: "zh_TW"}));

	$('#table-data').dataTable({
		"ajax": {
			"type": "post",
			"data": function (d) {
				d.filter = {'filter_param' : JSON.stringify($('#search-form').formToSearchArray())}
			},
			"url": "role.html?pAct=search"
		},
        "columns": [
                    {"orderable": false},
                    {"name": "title", "orderable": true},
                    {"orderable": false},
                    {"orderable": false},
                   ],
        "bFilter": false,
        "bInfo": true,
        "bPaginate": true,
        "order": [
                  [1, "desc"]
        ]
	});

	$('select.form-filter, select.form-like-filter', $('#search-form')).change(function () {
		loadTable();
	});
	
	$('.form-like-filter').on('keyup' , function(e){
		if(e.keyCode == 13){
			loadTable();
		}
	});
	
});

function delete_record(id) {
	
	bootbox.confirm("是否删除？", function (result) {
		if (result) {
			Metronic.blockUI({target: '#content-div', animate: true});
			$.ajax({
				type: "POST",
				url: "role.html?pAct=delete",
						data: {'id': id},
						success: function (resp) {
							Metronic.unblockUI('#content-div');
							if (resp.retcode == 200) {
								toastr['success'](resp.msg);
								loadTable();
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
