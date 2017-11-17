<%@ page contentType="text/html;charset=utf-8"%>

<modal_width val="70%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">项目详情</h4>
</div>
<div class="modal-body font-gothic">
	<div class="row">
		<div class="col-xs-12">
			<div class="form form-horizontal">
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-2 align-right">项目编号:</label> 
						<label class="col-xs-4 align-left">${record.num}</label>
						<label class="col-xs-2 align-right">账号:</label> 
						<label class="col-xs-4 align-left">${record.account}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 align-right">项目名称:</label> 
						<label class="col-xs-4 align-left">${record.name}</label>
						<label class="col-xs-2 align-right">地址:</label> 
						<label class="col-xs-4 align-left">${record.addr}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 align-right">项目介绍:</label> 
						<label class="col-xs-10 align-left">${record.comment}%</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 align-right">所需资源:</label> 
						<label class="col-xs-10 align-left">${record.need}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 align-right">项目网址:</label> 
						<label class="col-xs-10 align-left">${record.weburl}</label>
					</div>
				</div>
				<div class="form-actions col-xs-12" style="padding: 10px">
					<span style="font-weight: bold">联系人信息</span>
				</div>
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-2 align-right">姓名:</label> 
						<label class="col-xs-4 align-left">${record.contactName}</label>
						<label class="col-xs-2 align-right">手机号:</label> 
						<label class="col-xs-4 align-left">${record.contactMobile}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-4 align-right">项目图片:</label>
						<div class="col-xs-10">
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.imgPath1}" alt="项目图片1" style="height: 80px;"> 
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.imgPath2}" alt="项目图片2" style="height: 80px;"> 
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.imgPath3}" alt="项目图片3" style="height: 80px;">
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.imgPath4}" alt="项目图片4" style="height: 80px;"> 
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.imgPath5}" alt="项目图片5" style="height: 80px;">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer" style="text-align: center">
	<button type="button" class="btn default" data-dismiss="modal">返回</button>
</div>
