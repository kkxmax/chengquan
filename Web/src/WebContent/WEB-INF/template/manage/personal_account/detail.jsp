<%@ page contentType="text/html;charset=utf-8" %>

<modal_width val="40%"></modal_width>
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
  <h4 class="modal-title font-gothic text-center">账户详情</h4>
</div>
<div class="modal-body font-gothic">
  <div class="row">
    <div class="col-xs-12">
      <div class="form-horizontal">
        <div class="form-body col-xs-12">
          <div class="form-group">
            <label class="col-xs-4 align-right">账号:</label>
            <label class="col-xs-8 align-left">${record.mobile}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">诚信代码:</label>
            <label class="col-xs-8 align-left">${record.code}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">信用度:</label>
            <label class="col-xs-8 align-left">0%</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">推荐人数:</label>
            <label class="col-xs-8 align-left">${record.elect}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">头像:</label>
            <div class="col-xs-8"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.logo}" alt="头像图片" style="width: 50px; max-height: 50px;"></div>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">真实姓名:</label>
            <label class="col-xs-8 align-left">${record.realname}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">身份证:</label>
            <div class="col-xs-8"><img class="avatar-small" src="${C_UPLOAD_PATH}${record.certImage}" alt="身份证图像" style="height: 130px;"></div>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">公司:</label>
            <label class="col-xs-8 align-left">${record.enterName}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">行业:</label>
            <label class="col-xs-8 align-left">${record.mainJob}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">所在地:</label>
            <label class="col-xs-8 align-left">${record.addr}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">职位:</label>
            <label class="col-xs-8 align-left">${record.bossJob}</label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">工作经验:</label>
            <label class="col-xs-8 align-left"></label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">个人经历:</label>
            <label class="col-xs-8 align-left"></label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">特长或喜欢的行业:</label>
            <label class="col-xs-8 align-left"></label>
          </div>
          <div class="form-group">
            <label class="col-xs-4 align-right">喜欢的资源或朋友:</label>
            <label class="col-xs-8 align-left"></label>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="modal-footer" style="text-align: center">
  <button type="button" class="btn default" data-dismiss="modal">返回</button>
</div>
