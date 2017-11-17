<%@ page contentType="text/html;charset=utf-8"%>

<modal_width val="60%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">账户详情</h4>
</div>
<div class="modal-body font-gothic">
	<div class="row">
		<div class="col-xs-12">
			<div class="form-horizontal">
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-3 text-right">服务编号:</label> <label
							class="col-xs-9 text-left">${record.num}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">账号:</label> <label
							class="col-xs-9 text-left">${record.booknum}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">服务名称:</label> <label
							class="col-xs-9 text-left">${record.name}%</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">服务介绍:</label> <label
							class="col-xs-9 text-left">${record.comment}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">网址:</label><label
							class="col-xs-9 text-left">${record.weburl}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">实体店地址:</label> <label
							class="col-xs-9 text-left">${record.addr}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">联系人信息:</label>
						<div class="col-xs-9"></div>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">姓名:</label> <label
							class="col-xs-9 text-left">${record.contact_name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">手机号:</label> <label
							class="col-xs-9 text-left">${record.contact_mobile}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">微信号:</label> <label
							class="col-xs-9 text-left">${record.contact_weixin}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">服务图片:</label>
						<div class="col-xs-9 text-left">
							<img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path1}" alt="身份证图像"
								style="width: 18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path2}" alt="身份证图像"
								style="width: 18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path3}" alt="身份证图像"
								style="width: 18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path4}" alt="身份证图像"
								style="width: 18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path5}" alt="身份证图像"
								style="width: 18%">
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
