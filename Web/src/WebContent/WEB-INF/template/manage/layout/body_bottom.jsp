<%@ page contentType="text/html;charset=utf-8" %>

      </div>
    </div>
  </div>
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<div class="page-footer">
  <div class="page-footer-inner">
    2017 &copy; 诚全信息发布平台
  </div>
  <div class="scroll-to-top">
    <i class="icon-arrow-up"></i>
  </div>
</div>
<!-- END FOOTER -->

<div class="modal fade" id="global-modal" role="basic" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-body">
        <img src="${C_ASSETS_PATH}/global/img/loading-spinner-grey.gif" alt="" class="loading">
        <span>&nbsp;&nbsp;数据下载中... </span>
      </div>
    </div>
  </div>
</div>

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/respond.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/excanvas.min.js" ></script>
<![endif]-->
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-migrate.min.js" ></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-ui/jquery-ui.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.blockui.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.cokie.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/uniform/jquery.uniform.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.form.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/datatables/media/js/jquery.dataTables.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootbox/bootbox.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-toastr/toastr.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-validation/lib/jquery.form.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-validation/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/select2/select2.min.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-select/bootstrap-select.min.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/scripts/datatable.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/scripts/metronic.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/admin/layout/scripts/layout.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/custom/scripts/global.js" ></script>
<!-- END CORE PLUGINS -->
<script>
  jQuery(document).ready(function () {
    Metronic.init(); // init metronic core componets
    Layout.init(); // init layout
    toastr.options = {
      "positionClass": "toast-bottom-right"
    }
    
    refreshNoticeCnt();
//    setTimer();
  });
  
</script>
<!-- END JAVASCRIPTS -->
