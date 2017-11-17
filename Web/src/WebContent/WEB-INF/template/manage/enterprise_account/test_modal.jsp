<%@ page contentType="text/html;charset=utf-8" %>

<modal_width val="40%"></modal_width>
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
  <h4 class="modal-title font-gothic text-center">账户详情</h4>
</div>
<div class="modal-body font-gothic">
  <div class="row">
    <div class="col-xs-12">
      <form id="test-form" class="form form-horizontal" method="post" action="${cur_page}?pAct=changeTestStatus">
        <input type="hidden" name="id" value="${record.id}">
        <div class="form-actions top" style="padding: 10px">
          <span style="font-weight: bold">企业信息</span>
        </div>
        <div class="form-body col-xs-12">
          <div class="form-group">
            <label class="col-xs-4 align-right">企业logo:</label>
            <div class="col-xs-8"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.logo}" alt="头像图片" style="width: 50px; max-height: 50px;"></div>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">企业类型:</label>
            <label class="col-xs-8 align-left">${record.enterKindName}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">公司全称:</label>
            <label class="col-xs-8 align-left">${record.enterName}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">行业:</label>
            <label class="col-xs-8 align-left"></label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">办公地址:</label>
            <label class="col-xs-8 align-left">${record.addr}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">公司官网:</label>
            <label class="col-xs-8 align-left">${record.weburl}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">微信公众号:</label>
            <label class="col-xs-8 align-left">${record.weixin}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">主营业务:</label>
            <label class="col-xs-8 align-left">${record.mainJob}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">营业执照:</label>
            <div class="col-xs-8"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.certImage}" alt="营业执照图像" style="height: 130px;"></div>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">公司介绍:</label>
            <label class="col-xs-8 align-left">${record.comment}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">我们承诺:</label>
            <label class="col-xs-8 align-left">${record.recommend}</label>
          </div>
        </div>
        <div class="form-actions col-xs-12" style="padding: 10px">
          <span style="font-weight: bold">负责人信息</span>
        </div>
        <div class="form-body col-xs-12">
          <div class="form-group">
            <label class="col-xs-4 align-right">姓名:</label>
            <label class="col-xs-8 align-left">${record.bossName}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">职位:</label>
            <label class="col-xs-8 align-left">${record.bossJob}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">手机号:</label>
            <label class="col-xs-8 align-left">${record.bossMobile}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">微信号:</label>
            <label class="col-xs-8 align-left">${record.bossWeixin}</label>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
<div class="modal-footer" style="text-align: center">
  <button type="button" class="btn btn-primary" onclick="changeTestStatus(2);">通过</button>
  <button type="button" class="btn default" onclick="changeTestStatus(3);">不通过</button>
</div>

<script>
  function changeTestStatus(targetStatus) {
    $('#test-form').ajaxSubmit({
      beforeSubmit: function(formData, $form, options) {
        Metronic.blockUI({target: '#content-div',animate: true});
        formData.push({'name': 'targetStatus', 'value': targetStatus});
      },
      success: function(resp) {
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

</script>
