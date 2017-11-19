<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<modal_width val="80%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">查看</h4>
</div>
<div class="modal-body font-gothic">
	<div class="row">
		<div class="col-xs-12">
			<div class="form-horizontal">
				<div class="form-body col-xs-12">
					<div class="form-group">
						<label class="col-xs-2 text-right">文章标题:</label> <label
							class="col-xs-10 text-left">${record.title}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 text-right">行业:</label> <label
							class="col-xs-10 text-left">${record.xyleixingLevel1Name}&nbsp;&nbsp;&nbsp;${record.xyleixingName}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 text-right">文章正文:</label> <label
							class="col-xs-10 text-left">${record.content}</label>
					</div>
					<div class="form-group">
						<label class="col-xs-2 text-right">图片:</label> 
						<div class="col-xs-10 text-left">
							<c:forEach items="${record.imgPaths}" var="imgPath">
								<img src="${C_UPLOAD_PATH}${imgPath}" style="width: 150px">
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
