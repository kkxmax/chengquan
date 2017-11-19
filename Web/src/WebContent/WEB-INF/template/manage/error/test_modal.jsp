<%@ page contentType="text/html;charset=utf-8" %>

<modal_width val="15%"></modal_width>
<div class="modal-body font-gothic">
  <div class="row">
    <div class="col-xs-12">
      <form id="test-form" class="form form-horizontal" method="post" action="${cur_page}?pAct=changeTestStatus">
        <input type="hidden" name="id" value="${record.id}">
        <p>纠错是否属实?</p>
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
