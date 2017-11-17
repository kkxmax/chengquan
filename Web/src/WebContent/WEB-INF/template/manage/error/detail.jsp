<%@ page contentType="text/html;charset=utf-8"%>

<modal_width val="60%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">纠错详情</h4>
</div>
<div class="modal-body font-gothic">
	<div class="row">
		<div class="col-xs-12">
			<div class="form-horizontal">
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-3 text-right">账号:</label> <label
							class="col-xs-9 text-left">${record.booknum}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">纠错人:</label> <label
							class="col-xs-9 text-left">${record.name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">被评价人:</label> <label
							class="col-xs-9 text-left">${record.no_name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价人:</label> <label
							class="col-xs-9 text-left">${record.real_name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价内容:</label><label
							class="col-xs-9 text-left">${record.content}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">纠错原因:</label> <label
							class="col-xs-9 text-left">${record.reason}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">纠错依据:</label> <label
							class="col-xs-9 text-left">${record.whyis}</label>

					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">服务图片:</label>
						<div class="col-xs-9 text-left"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer" style="text-align: center">
	<button type="button" class="btn green" onclick="pass(${record.id})">审核</button>
	<button type="button" class="btn default" data-dismiss="modal">返回</button>
</div>


<script>
function pass(id) {

	bootbox.confirm("是否通过？", function(result) {
		if (result) {
			Metronic.blockUI({
				target : '#content-div',
				animate : true
			});
			$.ajax({
				type : "POST",
				url : "error.html?pAct=pass",
				data : {
					'id' : id
				},
				success : function(resp) {
					Metronic.unblockUI('#content-div');
					if (resp.retcode == 200) {
						toastr['success'](resp.msg);
						$('#global-modal').modal('hide');
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