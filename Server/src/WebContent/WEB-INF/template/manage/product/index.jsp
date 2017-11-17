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
        <i class="fa fa-circle-o"></i>产品目录
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
        <a href="javascript:;" class="reload" onclick="javascript:loadTable();"></a>
      </div>
      <div class="actions">
      </div>
    </div>
    <div class="portlet-body">
      <div class="row margin-bottom-10">
        <form class="form-inline" id="search-form">
          <div class="form-body col-md-12 col-sm-12">
            &nbsp;&nbsp;
            <div class="form-group">
              <label>产品编号:</label>
              <input type="text" class="form-control form-like-filter input-small" name="code">
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>账号:</label>
              <input type="text" class="form-control form-like-filter input-small" name="account_mobile">
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>公司全称:</label>
              <input type="text" class="form-control form-like-filter input-small" name="enter_name">
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>产品名称:</label>
              <input type="text" class="form-control form-like-filter input-small" name="name">
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>企业类型:
              </label>
              <select class="form-control form-filter select2me input-small" name="enter_kind">
                <option value="">全部</option>
                <c:forEach items="${C_ACCOUNT_ENTER_TYPE}" var="item">
              	<option value="${item.key}">${item.value}</option>
                </c:forEach>
              </select>
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>发布时间:
              </label>
              <div name="write_time" class="form-date-filter input-group input-large date-picker input-daterange" data-date-format="yyyy-mm-dd">
              	<input type="text" class="form-control" name="from" id="from">
              	<span class="input-group-addon">至</span>
              	<input type="text" class="form-control" name="to" id="to">
              </div>
              <button class="btn btn-sm yellow" onclick="loadTable();return false;"><i class="fa fa-search"></i> 查询
              </button>
            </div>
            &nbsp;&nbsp;
          </div>
        </form>
      </div>
      <div class="table-container">
        <table class="table table-striped table-bordered table-hover" id="table-data">
          <thead>
          <tr>
          	<th>序号</th>
            <th>产品编号</th>
            <th>账号</th>
            <th>产品名称</th>
            <th>产品价格</th>
            <th>公司全称</th>
            <th>企业类型</th>
            <th>发布时间</th>
            <th>状态</th>
            <th style="width:120px">操作</th>
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
			"url": "product.html?pAct=search"
		},
        "columns": [
                    {"orderable": false},
                    {"name": "code", "orderable": true},
                    {"name": "account_mobile", "orderable": true},
                    {"name": "name", "orderable": true},
                    {"name": "price", "orderable": true},
                    {"name": "enter_name", "orderable": true},
                    {"name": "enter_kind_name", "orderable": true},
                    {"name": "write_time", "orderable": true},
                    {"name": "status_name", "orderable": true},
                    {"orderable": false},
                   ],
        "bFilter": false,
        "bInfo": true,
        "bPaginate": true,
        "order": [
                  [7, "desc"]
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
	
	$('#from , #to').change(function(){
		loadTable();
	});
	
});

function delete_record(id) {
	
	bootbox.confirm("是否删除？", function (result) {
		if (result) {
			Metronic.blockUI({target: '#content-div', animate: true});
			$.ajax({
				type: "POST",
				url: "product.html?pAct=delete",
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
