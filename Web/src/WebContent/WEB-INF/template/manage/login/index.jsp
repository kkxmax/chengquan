<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!--[if IE 8]>
<html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]>
<html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<head>
  	<meta charset="UTF-8">
  	<title>${C_SYSTEM_TITLE}</title>
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/font-awesome/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/simple-line-icons/simple-line-icons.min.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/uniform/css/uniform.default.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap-toastr/toastr.min.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/select2/select2.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/css/components.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/css/plugins.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/admin/layout/css/layout.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/admin/layout/css/themes/darkblue.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/admin/pages/css/login-soft.css" />
	<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/custom/css/global.css" />
	<link rel="shortcut icon" href="favicon.ico"/>
	<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.min.js" ></script>

</head>
<body class="login">
<div class="logo">
</div>
<!-- END LOGO -->
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGIN -->
<div class="content">
  <!-- BEGIN LOGIN FORM -->
  <form class="login-form" action="${cur_page}?pAct=loginDo" method="post">
    <h3 class="form-title">请先登录后台</h3>

    <div class="form-group">
      <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
      <label class="control-label visible-ie8 visible-ie9">账号</label>
      <div class="input-icon">
        <i class="fa fa-user"></i>
        <input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="账号" name="username"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label visible-ie8 visible-ie9">密码</label>
      <div class="input-icon">
        <i class="fa fa-lock"></i>
        <input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="密码"
               name="password"/>
      </div>
    </div>
    <div class="form-actions" style="padding-bottom:40px">
      <button type="submit" class="btn blue pull-right"> 登录 <i class="m-icon-swapright m-icon-white"></i>
      </button>
    </div>
  </form>
  <!-- END LOGIN FORM -->
</div>
<!-- END LOGIN -->
<!-- BEGIN COPYRIGHT -->
<div class="copyright">
  2017 &copy; 诚全信息发布平台
</div>
<!-- END COPYRIGHT -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/respond.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/excanvas.min.js" ></script>
<![endif]-->
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-migrate.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.blockui.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/uniform/jquery.uniform.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.cokie.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery-validation/js/jquery.validate.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/backstretch/jquery.backstretch.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/select2/select2.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.form.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-toastr/toastr.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/scripts/metronic.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/admin/layout/scripts/layout.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/admin/layout/scripts/demo.js" ></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
  jQuery(document).ready(function () {
    Metronic.init(); // init metronic core components
    Layout.init(); // init current layout
    // init background slide images
    $.backstretch([
              "${C_ASSETS_PATH}/admin/pages/media/bg/1.jpg",
              "${C_ASSETS_PATH}/admin/pages/media/bg/2.jpg",
              "${C_ASSETS_PATH}/admin/pages/media/bg/3.jpg",
              "${C_ASSETS_PATH}/admin/pages/media/bg/4.jpg"
            ], {
              fade: 1000,
              duration: 8000
            }
    );

    $('.login-form').validate({
      errorElement: 'span', //default input error message container
      errorClass: 'help-block', // default input error message class
      focusInvalid: false, // do not focus the last invalid input
      rules: {
        username: {
          required: true
        },
        password: {
          required: true
        },
        remember: {
          required: false
        }
      },

      messages: {
        username: {
          required: "请输入账号"
        },
        password: {
          required: "请输入密码"
        }
      },

      highlight: function (element) { // hightlight error inputs
        $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
      },

      success: function (label) {
        label.closest('.form-group').removeClass('has-error');
        label.remove();
      },

      errorPlacement: function (error, element) {
        error.insertAfter(element.closest('.input-icon'));
      },

      submitHandler: function (form) {
        $(form).ajaxSubmit({
          beforeSubmit: function () {
          },
          success: function (resp) {
            if (resp.retcode == 200) {
              location.href = "personal_account.html";
            } else {
              toastr['error'](resp.msg);
            }
          }
        });
      }
    });

    $('.login-form input').keypress(function (e) {
      if (e.which == 13) {
        if ($('.login-form').validate().form()) {
          $('.login-form').submit();
        }
        return false;
      }
    });

    toastr.options = {
      "positionClass": "toast-bottom-right"
    }
  });
</script>
</body>
</html>

