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
        <i class="fa fa-circle-o"></i>系统消息列表
      </div>
      <div class="tools">
        <a href="javascript:;" class="collapse"></a>
        <a href="javascript:;" class="reload" onclick="javascript:loadTable();"></a>
      </div>
      <div class="actions">
      </div>
    </div>
    <div class="portlet-body">
	    <form id="frm_notice" action="${cur_page}?pAct=remove">
	    	<div class="row margin-bottom-10">
		        <div class="form-inline">
		          <div class="form-body col-md-12 col-sm-12">
		            <div class="form-group">
		              <button type="submit" class="btn btn-sm red"><i class="fa fa-trash-o"></i> 删除
		              </button>
		            </div>
		          </div>
		        </div>
		      </div>
		      <div class="table-container">
		        <table class="table table-striped table-bordered table-hover" id="table-data">
		          <thead>
		          <tr>
		            <th width="10px"><input type="checkbox" onclick="selectAll(this);">全选</th>
		            <th>序号</th>
		            <th>消息类型</th>
		            <th>消息内容</th>
		            <th>状态</th>
		            <th>发送日期</th>
		            <th>操作</th>
		          </tr>
		          </thead>
		          <tbody>
		          </tbody>
		        </table>
		      </div>
	    </form>
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

	jQuery(document).ready(
			function() {
				$(".date-picker").datepicker($.extend({}, {language : "zh_CN"}));

				$('#table-data').dataTable(
						{
							"ajax" : {
								"type" : "post",
								"data" : function(d) {
									d.filter = {'filter_param' : JSON.stringify($('#search-form').formToSearchArray())}
								},
								"url" : "${cur_page}?pAct=search"
							},
							"columns" : [ 
											{"orderable": false},
											{"orderable": false},
											{"name": "kind_name", "orderable": true},
											{"name": "msg_content", "orderable": true},
											{"name": "status_name", "orderable": true},
											{"name": "write_time", "orderable": true},
											{"orderable": false},
							            ],
							"bFilter" : false,
							"bInfo" : true,
							"bPaginate" : true,
							"order" : [ [ 5, "desc" ] ]
						});

				$('select.form-filter, select.form-like-filter', $('#search-form')).change(function() {
					loadTable();
				});
				
				$('#frm_notice').ajaxForm({
					beforeSubmit: function(formData, $form, options) {
						var selIds = [];
						$(":checkbox[name='sel_ids[]']").each(function(i, o) {
							if($(o).is(':checked')) {
								selIds.push($(o).attr('notice_id'));	
							}
						});
						if(selIds.length == 0) {
							toastr['error']("请选要删除的消息");
							return false;
						}
						formData.push({'name': 'del_ids', 'value': selIds});
					},
					success: function(resp) {
						if(resp.retcode == 200) {
							toastr['success'](resp.msg);
							loadTable();
						} else {
							toastr['error'](resp.msg);
						}
					}
				});

			});

	function read(id) {
		Metronic.blockUI({
			target : '#content-div',
			animate : true
		});
		$.ajax({
			type : "POST",
			url : "${cur_page}?pAct=read",
			data : {
				'id' : id
			},
			success : function(res) {
				Metronic.unblockUI('#content-div');
				if (res.retcode == 200) {
					toastr['success'](res.msg);
					loadTable();
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Metronic.unblockUI('#content-div');
				bootbox.alert("发生错误！");
			}
		});
	}
	
	function selectAll(elem) {
		if($(elem).is(':checked'))
            $(":checkbox[name='sel_ids[]']").prop('checked', true);
        else
            $(":checkbox[name='sel_ids[]']").prop('checked', false);
	}
	
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
