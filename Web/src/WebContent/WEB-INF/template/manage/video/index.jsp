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
				<i class="fa fa-circle-o"></i>视频管理
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse"></a> <a href="javascript:;"
					class="reload" onclick="javascript:loadTable();"></a>
			</div>
			<div class="actions">
				<a class="btn btn-default btn-sm" href="${cur_page}?pAct=add">
					<i class="fa fa-plus"></i> 新增
				</a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="table-container">
				<table class="table table-striped table-bordered table-hover"
					id="table-data">
					<thead>
						<tr>
							<th>视频名称</th>
							<th>新增时间</th>
							<th>状态</th>
							<th style="width: 193px">操作</th>
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

	jQuery(document).ready(
			function() {
				$('#table-data').dataTable(
						{
							"ajax" : {
								"type" : "post",
								"data" : function(d) {
									d.filter = {
											'filter_param' : JSON.stringify($('#search-form').formToSearchArray())
									}
								},
								"url" : "${cur_page}?pAct=search"
							},
							"columns" : [ {
								"name" : "title",
								"orderable" : true
							}, {
								"name" : "write_time",
								"orderable" : true
							}, {
								"orderable" : false,
							}, {
								"orderable" : false
							}, ],
							"bFilter" : false,
							"bInfo" : true,
							"bPaginate" : true,
							"order" : [ [ 1, "desc" ] ]
						});
			});

	function deleteRecord(id) {
		bootbox.confirm("是否删除？", function(result) {
			if (result) {
				Metronic.blockUI({
					target : '#content-div',
					animate : true
				});
				$.ajax({
					type : "POST",
					url : "${cur_page}?pAct=delete",
					data : {
						'id' : id
					},
					success : function(resp) {
						Metronic.unblockUI('#content-div');
						if (resp.retcode == 200) {
							toastr['success'](resp.msg);
							loadTable();
						} else {
							toastr['error'](resp.msg);
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						Metronic.unblockUI('#content-div');
						bootbox.alert("发生错误！");
					}
				});
			}
		});
	}

	function editRecord(id) {
		location.href = "${cur_page}?pAct=edit&id=" + id;
	}
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
