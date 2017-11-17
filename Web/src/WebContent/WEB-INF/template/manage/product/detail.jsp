<%@ page contentType="text/html;charset=utf-8"%>

<modal_width val="60%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">产品详情</h4>
</div>
<div class="modal-body font-gothic">
	<div class="row">
		<div class="col-xs-12">
			<div class="form-horizontal">
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-3 text-right">产品编号:</label> <label
							class="col-xs-9 text-left">${record.num}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">账号:</label> <label
							class="col-xs-9 text-left">${record.booknum}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">公司全称:</label> <label
							class="col-xs-9 text-left">${record.enter_name}%</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">是否是主营产品:</label> <label
							class="col-xs-9 text-left">是</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品名称:</label><label
							class="col-xs-9 text-left">${record.name}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品价格:</label> <label
							class="col-xs-9 text-left">${record.price}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品分类:</label>
						<div class="col-xs-9">${record.type_name}</div>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品介绍:</label> <label
							class="col-xs-9 text-left">${record.comment}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品网址:</label> <label
							class="col-xs-9 text-left">${record.weburl}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">实体店地址:</label> <label
							class="col-xs-9 text-left">${record.sale_weburl}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">产品图片:</label>
						<div class="col-xs-9 text-left">
							<img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path1}" alt="身份证图像"
								style="width:18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path2}" alt="身份证图像"
								style="width:18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path3}" alt="身份证图像"
								style="width:18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path4}" alt="身份证图像"
								style="width:18%"> <img class="avatar-small"
								src="${C_UPLOAD_PATH}${record.img_path5}" alt="身份证图像"
								style="width:18%">
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
