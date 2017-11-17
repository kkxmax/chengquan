<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
						<label class="col-xs-4 align-left">${record.code}</label>
						<label class="col-xs-2 align-right">账号:</label> 
						<label class="col-xs-4 align-left">${record.accountMobile}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 align-right">项目名称:</label> 
						<label class="col-xs-4 align-left">${record.name}</label>
						<label class="col-xs-2 align-right">地址:</label> 
						<label class="col-xs-4 align-left">${record.cityName} ${record.addr}</label>
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
							<img class="avatar-small" src="${C_UPLOAD_PATH}${record.logo}" alt="logo图像" style="width:18%">
							<c:forEach items="${record.imgPaths}" var="imgPath">
								<img class="avatar-small" src="${C_UPLOAD_PATH}${imgPath}" alt="项目图像" style="width:18%">
							</c:forEach>
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
