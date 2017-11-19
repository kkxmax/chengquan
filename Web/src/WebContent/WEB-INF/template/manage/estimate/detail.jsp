<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
							class="col-xs-9 text-left">${record.ownerMobile}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价人:</label> <label
							class="col-xs-9 text-left">${record.ownerName}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价对象:</label> <label
							class="col-xs-9 text-left">${record.targetAccountName}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价类型:</label> <label
							class="col-xs-9 text-left">${record.kindName}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价方式:</label><label
							class="col-xs-9 text-left">${record.methodName}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价原因:</label> <label
							class="col-xs-9 text-left">${record.reason}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价内容:</label> <label
							class="col-xs-9 text-left">${record.content}</label>

					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">图片:</label>
						<div class="col-xs-9 text-left">
							<c:forEach items="${record.imgPaths}" var="imgPath">
								<img class="avatar-small" src="${C_UPLOAD_PATH}${imgPath}" alt="图像" style="width:18%">
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
