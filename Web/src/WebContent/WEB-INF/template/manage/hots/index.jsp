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
        <i class="fa fa-circle-o"></i>热点管理
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
        <a href="javascript:;" class="reload" onclick="javascript:loadTable();"></a>
      </div>
      <div class="actions">
        <a class="btn btn-default btn-sm" href="${cur_page}?pAct=edit"><i class="fa fa-plus"></i> 新增</a>
      </div>
    </div>
    <div class="portlet-body">
      <div class="row margin-bottom-10">
        <form class="form-inline" id="search-form">
          <div class="form-body col-md-12 col-sm-12">
            &nbsp;&nbsp;
            <div class="form-group">
              <label>上架状态:
              </label>
              <select class="form-control form-filter select2me input-small" name="st_name">
                <option value="">全部</option>
                <c:forEach items="${C_HOTS_ST_NAME}" var="item">
                	<option value="${item.key}">${item.value}</option>
                </c:forEach>
              </select>
            </div>
            &nbsp;&nbsp;
            <div class="form-group">
              <label>行业:</label>
              <select class="form-control form-filter select2me input-small" id="xyleixing_level1_id" name="xyleixing_level1_id" onchange="xyleixing_level1_id_changed();">
                <option value="">全部</option>
                  <c:forEach items="${leixings}" var="item">
                	<option value="${item.id}">${item.title}</option>
                  </c:forEach>
              </select>
              <select class="form-control form-filter select2me input-small" id="kind" name="kind">
                <option value="">全部</option>
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
            <th>文章标题</th>
            <th>行业</th>
            <th>write_time</th>
            <th>阅读量</th>
            <th>评价数</th>
            <th>点赞数</th>
            <th>分享数</th>
            <th>上架状态</th>
            <th>发布时间</th>
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
			"url": "hots.html?pAct=search"
		},
        "columns": [
                    {"name": "title", "orderable": true},
                    {"name": "kind_name", "orderable": true},
                    {"name": "write_time", "orderable": true, "visible": false},
                    {"name": "visit_cnt", "orderable": true},
                    {"name": "comment_cnt", "orderable": true},
                    {"name": "remark_cnt", "orderable": true},
                    {"name": "share_cnt", "orderable": true},
                    {"name": "st_name", "orderable": true},
                    {"name": "up_time", "orderable": true},
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
	
	$('.form-like-filter').on('keyup' , function(e){
		if(e.keyCode == 13){
			loadTable();
		}
	});
	
	xyleixing_level1_id_changed();
	
});

function xyleixing_level1_id_changed() {
	$.ajax({
		type: "POST",
		url: "${cur_page}?pAct=getLevel2Xyleixings",
				data: {'level1leixing': $('#xyleixing_level1_id').val()},
				success: function (resp) {
					if (resp.retcode == 200) {
						records = resp.records;
						html = "<option value=''>全部</option>";
						for(i=0; i<records.length; i++) {
							record = records[i];
							html += "<option value=" + record.id + ">" + record.title + "</option>";
						}
						$('#kind').html(html);
						$('#kind').select2();
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					bootbox.alert("发生错误！");
				}
			});
}

function delete_record(id) {
	
	bootbox.confirm("是否删除？", function (result) {
		if (result) {
			Metronic.blockUI({target: '#content-div', animate: true});
			$.ajax({
				type: "POST",
				url: "hots.html?pAct=delete",
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
