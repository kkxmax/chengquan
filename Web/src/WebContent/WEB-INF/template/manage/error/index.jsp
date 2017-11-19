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
				<i class="fa fa-circle-o"></i>纠错目录
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
							<label>纠错人:</label> <input type="text"
								class="form-control form-like-filter input-small" name="owner_name">
						</div>
						&nbsp;&nbsp;
						<div class="form-group">
							<label>评价方式: </label> <select
								class="form-control form-filter select2me input-small"
								name="status">
								<option value="">全部</option>
								<c:forEach items="${C_ERROR_STATUS_NAME}" var="item">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
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
							<th>纠错人</th>
							<th>被评价人</th>
							<th>评价人</th>
							<th>评价内容</th>
							<th>纠错原因</th>
							<th>状态</th>
							<th>纠错时间</th>
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

	jQuery(document).ready(
			function() {
				$(".date-picker").datepicker($.extend({}, {
					language : "zh_TW"
				}));

				$('#table-data').dataTable(
						{
							"ajax" : {
								"type" : "post",
								"data" : function(d) {
									d.filter = {
										'filter_param' : JSON.stringify($(
												'#search-form')
												.formToSearchArray())
									}
								},
								"url" : "error.html?pAct=search"
							},
							"columns" : [ {
								"name" : "owner_mobile",
								"orderable" : true
							}, {
								"name" : "owner_name",
								"orderable" : true
							}, {
								"name" : "estimatee_name",
								"orderable" : true
							}, {
								"name" : "estimater_name",
								"orderable" : true
							}, {
								"name" : "estimate_content",
								"orderable" : true
							}, {
								"name" : "reason",
								"orderable" : true
							}, {
								"name" : "status_name",
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
							"order" : [ [ 7, "desc" ] ]
						});

				$('select.form-filter, select.form-like-filter',
						$('#search-form')).change(function() {
					loadTable();
				});

				$('.form-like-filter').on('keyup', function(e) {
					if (e.keyCode == 13) {
						loadTable();
					}
				});

			});

</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
