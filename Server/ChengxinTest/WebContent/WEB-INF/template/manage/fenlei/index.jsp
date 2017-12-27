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
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
  <div class="portlet box blue-hoki">
    <div class="portlet-title">
      <div class="caption">
        <i class="fa fa-circle-o"></i>分类管理
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
        <a href="javascript:;" class="reload" onclick="javascript:loadTable();"></a>
      </div>
      <div class="actions">
        <a class="btn btn-default btn-sm" href="${cur_page}?pAct=edit" data-target="#global-modal" data-toggle="modal"><i class="fa fa-plus"></i> 新增</a>
      </div>
    </div>
    <div class="portlet-body">
      <div class="row margin-bottom-10">
        <form class="form-inline" id="search-form">
          <div class="form-body col-md-12 col-sm-12">
            <div class="form-group">
              <label>类型:</label>
              <select class="form-control form-filter select2me input-small" name="leixing">
                <option value="">全部</option>
                  <c:forEach items="${C_FENLEI_LEIXING}" var="item">
                	<option value="${item.key}">${item.value}</option>
                  </c:forEach>
              </select>
            </div>
            &nbsp;&nbsp;
          </div>
        </form>
      </div>
      <div class="table-container">
        <table class="table table-striped table-bordered table-hover" id="table-data">
          <thead>
          <tr>
            <th>write_time</th>
            <th style="width:50px">序号</th>
            <th>分类</th>
            <th>类型</th>
            <th style="width:200px">操作</th>
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

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp" %>
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
		"ajax": {
			"type": "post",
			"data": function (d) {
				d.filter = {'filter_param' : JSON.stringify($('#search-form').formToSearchArray())}
			},
			"url": "${cur_page}?pAct=search"
		},
        "columns": [
                    {"name": "write_time", "orderable": true, "visible": false},
                    {"orderable": false},
                    {"name": "fenlei", "orderable": true},
                    {"name": "leixing_name", "orderable": true},
                    {"orderable": false},
                   ],
        "bFilter": false,
        "bInfo": true,
        "bPaginate": true,
        "order": [
                  [0, "desc"]
        ]
	});

	$('select.form-filter, select.form-like-filter', $('#search-form')).change(function () {
		loadTable(true);
	});
	
});

function remove(id) {
	
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
