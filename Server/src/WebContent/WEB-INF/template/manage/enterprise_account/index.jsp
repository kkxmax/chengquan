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
        <i class="fa fa-circle-o"></i>企业账户列表
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
                <label>账号:</label>
                <input type="text" class="form-control form-like-filter input-small" name="mobile">
              </div>
              &nbsp;&nbsp;
              <div class="form-group">
                <label>公司全称:</label>
                <input type="text" class="form-control form-like-filter input-small" name="enter_name">
              </div>
              &nbsp;&nbsp;
              <div class="form-group">
                <label>诚信代码:</label>
                <input type="text" class="form-control form-like-filter input-small" name="code">
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
                <label>审核状态:
                </label>
                <select class="form-control form-filter select2me input-small" name="test_status">
                  <option value="">全部</option>
                  <c:forEach items="${C_ACCOUNT_TEST_STATUS}" var="item">
                	<option value="${item.key}">${item.value}</option>
                  </c:forEach>
                </select>
              </div>
              &nbsp;&nbsp;
              <div class="form-group">
                <label>禁用状态:
                </label>
                <select class="form-control form-filter select2me input-small" name="ban_status">
                  <option value="">全部</option>
                  <c:forEach items="${C_BAN_STATUS}" var="item">
                	<option value="${item.key}">${item.value}</option>
                  </c:forEach>
                </select>
              </div>
              &nbsp;&nbsp;
              <div class="form-group">
	              <label>所在地:</label>
	              <select class="form-control form-filter select2me input-small" id="province_id" name="province_id" onchange="province_id_changed();">
	                <option value="">全部</option>
	                  <c:forEach items="${provinces}" var="item">
	                	<option value="${item.id}">${item.name}</option>
	                  </c:forEach>
	              </select>
	              <select class="form-control form-filter select2me input-small" id="city_id" name="city_id" onchange="city_id_changed();">
	                <option value="">全部</option>
	              </select>
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
              <th>logo</th>
              <th>创造时间</th>
              <th>账号</th>
              <th>公司全称</th>
              <th>所在地</th>
              <th>企业类型</th>
              <th>诚信代码</th>
              <th>诚信度</th>
              <th>推荐人数</th>
              <th>审核状态</th>
              <th>禁用状态</th>
              <th>操作</th>
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
			"url": "${cur_page}?pAct=search"
		},
        "columns": [
                    {"orderable": false},
                    {"orderable": false},
                    {"name": "write_time", "orderable": true, "visible": false},
                    {"name": "account", "orderable": true},
                    {"name": "enter_name", "orderable": true},
                    {"name": "prov_city", "orderable": true},
                    {"name": "enter_kind", "orderable": true},
                    {"name": "code", "orderable": true},
                    {"name": "credit", "orderable": true},
                    {"name": "elect_cnt", "orderable": true},
                    {"name": "test_status_name", "orderable": true},
                    {"name": "ban_status_name", "orderable": true},
                    {"orderable": false},
                   ],
        "bFilter": false,
        "bInfo": true,
        "bPaginate": true,
        "order": [
                  [2, "desc"]
        ]
	});

	$('select.form-filter, select.form-like-filter', $('#search-form')).change(function () {
		loadTable();
	});
	
	province_id_changed();
});

function province_id_changed() {
	$.ajax({
		type: "POST",
		url: "personal_account.html?pAct=getCities",
				data: {'provinceId': $('#province_id').val()},
				success: function (resp) {
					if (resp.retcode == 200) {
						records = resp.records;
						html = "<option value=''>全部</option>";
						for(var i=0; i<records.length; i++) {
							record = records[i];
							html += "<option value=" + record.id + ">" + record.name + "</option>";
						}
						$('#city_id').html(html);
						$('#city_id').select2();
						city_id_changed();
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					bootbox.alert("发生错误！");
				}
			});
}

function city_id_changed() {
	loadTable();
}

function changeBanStatus(id, targetStatus) {
	if(targetStatus == 1) {
		confirmMsg = "确认要取消禁用此用户?";	
	}
	else {
		confirmMsg = "确认要禁用此用户?";
	}
	
	bootbox.confirm(confirmMsg, function (result) {
		if (result) {
			Metronic.blockUI({target: '#content-div', animate: true});
			$.ajax({
				type: "POST",
				url: "${cur_page}?pAct=changeBanStatus",
						data: {'id': id, 'targetStatus': targetStatus},
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
