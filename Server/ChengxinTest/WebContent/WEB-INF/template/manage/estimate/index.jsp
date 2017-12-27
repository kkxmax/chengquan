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
				<i class="fa fa-circle-o"></i>评价目录
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse"></a> <a href="javascript:;"
					class="reload" onclick="javascript:loadTable();"></a>
			</div>
			<div class="actions"></div>
		</div>
		<div class="portlet-body">
			<div class="row margin-bottom-10">
				<form class="form-inline" id="search-form">
					<div class="form-body col-md-12 col-sm-12">
						&nbsp;&nbsp;
						<div class="form-group">
							<label>账号:</label> <input type="text"
								class="form-control form-like-filter input-small" name="owner_mobile">
						</div>
						&nbsp;&nbsp;
						<div class="form-group">
							<label>评价人:</label> <input type="text"
								class="form-control form-like-filter input-small" name="owner_name">
						</div>
						&nbsp;&nbsp;
						<div class="form-group">
							<label>评价对象:</label> <input type="text"
								class="form-control form-like-filter input-small" name="target_account_name">
						</div>
						&nbsp;&nbsp;
						<div class="form-group">
							<label>评价类型: </label> <select
								class="form-control form-filter select2me input-small"
								name="kind">
								<option value="">全部</option>
								<c:forEach items="${C_ESTIMATE_KIND_NAME}" var="item">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
						</div>
						&nbsp;&nbsp;
						<div class="form-group">
							<label>评价方式: </label> <select
								class="form-control form-filter select2me input-small"
								name="method">
								<option value="">全部</option>
								<c:forEach items="${C_ESTIMATE_METHOD_NAME}" var="item">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
							<button class="btn btn-sm yellow" onclick="loadTable(true);return false;"><i class="fa fa-search"></i> 查询</button>
						</div>
					</div>
				</form>
			</div>
			<div class="table-container">
				<table class="table table-striped table-bordered table-hover"
					id="table-data">
					<thead>
						<tr>
							<th>账号</th>
							<th>评价人</th>
							<th>评价内容</th>
							<th>评价类型</th>
							<th>评价方式</th>
							<th>评价对象</th>
							<th>评价时间</th>
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
var g_table;
function loadTable(resetPage) {
	resetPage = typeof resetPage == 'undefined' ? false : resetPage;
	$('#table-data').DataTable().ajax.reload(g_table.oSettings, resetPage);	
}

jQuery(document).ready(function () {
	$(".date-picker").datepicker($.extend({}, {language: "zh_TW"}));

	g_table = $('#table-data').dataTable({
							"ajax" : {
								"type" : "post",
								"data" : function(d) {
									d.filter = {
										'filter_param' : JSON.stringify($(
												'#search-form')
												.formToSearchArray())
									}
								},
								"url" : "estimate.html?pAct=search"
							},
							"columns" : [ {
								"name" : "owner_mobile",
								"orderable" : true
							}, {
								"name" : "name",
								"orderable" : true
							}, {
								"name" : "content",
								"orderable" : true
							}, {
								"name" : "kind_name",
								"orderable" : true
							}, {
								"name" : "method_name",
								"orderable" : true
							}, {
								"name" : "reason",
								"orderable" : true
							}, {
								"name" : "write_time",
								"orderable" : true
							}, {
								"orderable" : false
							}, ],
							"bFilter" : false,
							"bInfo" : true,
							"bPaginate" : true,
							"order" : [ [ 6, "desc" ] ]
						});

				$('select.form-filter, select.form-like-filter',
						$('#search-form')).change(function() {
					loadTable(true);
				});

			});

	function delete_record(id) {

		bootbox.confirm("是否删除？", function(result) {
			if (result) {
				Metronic.blockUI({
					target : '#content-div',
					animate : true
				});
				$.ajax({
					type : "POST",
					url : "hots.html?pAct=delete",
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
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
