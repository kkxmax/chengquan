<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
							class="col-xs-9 text-left">${record.ownerMobile}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">纠错人:</label> <label
							class="col-xs-9 text-left">
							<c:if test="${record.ownerAkind == 1}">${record.ownerRealname}</c:if>
							<c:if test="${record.ownerAkind == 2}">${record.ownerEnterName}</c:if>
						</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">被评价人:</label> <label
							class="col-xs-9 text-left">
							<c:if test="${record.estimateeAkind == 1}">${record.estimateeRealname}</c:if>
							<c:if test="${record.estimateeAkind == 2}">${record.estimateeEnterName}</c:if>
						</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价人:</label> <label
							class="col-xs-9 text-left">
							<c:if test="${record.estimaterAkind == 1}">${record.estimaterRealname}</c:if>
							<c:if test="${record.estimaterAkind == 2}">${record.estimaterEnterName}</c:if>
						</label>
					</div>
					<div class="form-group">
						<label class="col-xs-3 text-right">评价内容:</label><label
							class="col-xs-9 text-left">${record.estimateContent}</label>
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


<script>

</script>