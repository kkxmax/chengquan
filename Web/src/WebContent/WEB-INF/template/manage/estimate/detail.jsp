<%@ page contentType="text/html;charset=utf-8"%>

<modal_width val="60%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">评价详情</h4>
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
						<label class="col-xs-3 text-right">评价人:</label> <label
							class="col-xs-9 text-left">${record.name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价人名称:</label> <label
							class="col-xs-9 text-left">${record.name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价类型:</label> <label
							class="col-xs-9 text-left">${record.kind_name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价方式:</label><label
							class="col-xs-9 text-left">${record.method_name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价原因:</label> <label
							class="col-xs-9 text-left">${record.content}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价内容:</label> <label
							class="col-xs-9 text-left">${record.content}</label>

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
	<button type="button" class="btn default" data-dismiss="modal">返回</button>
</div>
