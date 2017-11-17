<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<modal_width val="30%"></modal_width>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true"></button>
	<h4 class="modal-title font-gothic text-center">功能设置</h4>
</div>
<form id="frm_edit" action="role.html?pAct=editDo" method="post">
	<div class="modal-body font-gothic">
		<input type="hidden" value="${record.id}" name="id"> <input
			type="hidden" value="${record.title}" name="title">
		<div class="row">
			<div class="col-xs-12">
				<div class="form-horizontal">
					<div class="form-body col-xs-12">
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcPersonal"
									value="${record.funcPersonal}"
									<c:if test="${record.funcPersonal == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">个人账号</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcEnter"
									value="${record.funcEnter}"
									<c:if test="${record.funcEnter == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">企业账号</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcProduct"
									value="${record.funcProduct}"
									<c:if test="${record.funcProduct == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">产品管理</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcItem"
									value="${record.funcItem}"
									<c:if test="${record.funcItem == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">项目管理</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcCarousel"
									value="${record.funcCarousel}"
									<c:if test="${record.funcCarousel== 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">轮播图管理</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcVideo"
									value="${record.funcVideo}"
									<c:if test="${record.funcVideo == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">视频管理</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcHot" value="${record.funcHot}"
									<c:if test="${record.funcHot == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">热点管理</label>
						</div>
						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcError"
									value="${record.funcError}"
									<c:if test="${record.funcError == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">纠错管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcSystem"
									value="${record.funcSystem}"
									<c:if test="${record.funcSystem == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">系统管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcFenlei"
									value="${record.funcFenlei}"
									<c:if test="${record.funcFenlei == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">分类管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcChanpin"
									value="${record.funcChanpin}"
									<c:if test="${record.funcChanpin == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">产品管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcXingye"
									value="${record.funcXingye}"
									<c:if test="${record.funcXingye == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">行业管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcComment"
									value="${record.funcComment}"
									<c:if test="${record.funcComment == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">内用管理</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcOpinion"
									value="${record.funcOpinion}"
									<c:if test="${record.funcOpinion == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">意见犯规</label>
						</div>

						<div class="form-group">
							<div class="col-xs-5 text-right">
								<input type="checkbox" name="funcStatistic"
									value="${record.funcStatistic}"
									<c:if test="${record.funcStatistic == 1}">checked</c:if> />
							</div>
							<label class="col-xs-6 text-left">统计管理</label>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div class="modal-footer" style="text-align: center">
		<button type="submit" class="btn green">确定</button>
		<button type="button" class="btn default" data-dismiss="modal">返回</button>
	</div>
</form>
<script>
	jQuery(document).ready(function() {
		$('#frm_edit').validate({
			errorElement : 'span',
			errorClass : 'help-block-error',
			focusInvalid : false,
			ignore : '',
			highlight : function(e) {
				$(e).closest('.form-group').addClass('has-error');
			},
			unhighlight : function(e) {
				$(e).closest('.form-group').removeClass('has-error');
			},
			submitHandler : function(form) {
				$(form).ajaxSubmit({
					beforeSubmit : function() {
						Metronic.blockUI({
							target : '#content-div',
							animate : true
						});
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
					}
				});
			}
		});
	});
	function onSetting(id) {

	}
</script>
