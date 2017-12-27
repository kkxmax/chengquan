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
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<style>
	.horizontal-splitter {border: none; border-bottom: solid 1px #f0f0f0}
	.border-none {border: none;}
	.width-full {width: 100%}
	.label-title {color: #b8b8b8; font-size: 18px; float: left; padding-right: 10px}
	.frm-signup input {font-size: 18px; max-width: 150px !important}
	input:focus {outline: none;}
</style>
<head>
<%@ include file="/WEB-INF/template/front/layout/head.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp" %>

            <!-- BEGIN CONTENT -->
            <div class="row init-div" style="height:150px"></div>
            
            <div class="row init-div">
                <div class="col-md-12 init-div-horizontal" style="text-align:center">
                    <img src="assets/custom/img/login_successful.png" style="width:85px"/>
                </div>
                
                <div class="col-md-12 init-div-horizontal margin-top-10" style="text-align:center">
                    <span class="text-blue font-lg">恭喜您，注册成功</span>
                </div>
            </div>
            
            
            
            <div id="bottombar" style="border-top: solid 1px #e5e5e5; position: absolute; top: 1000px; left: 0; right: 0; padding-top: 7px">
                <div class="row init-div">
                    <div class="col-xs-8">
                        <img src="assets/custom/img/login_logo.png" style="width: 50px; float: left">
                        <div style="padding-left: 56px; padding-top: 6px">
                            <p style="font-size: 18px; font-weight: bold; color: #1793e5; margin-bottom: 0">诚乎</p>
                            <p style="color: #1793e5">你的话语权主场</p>
                        </div>
                    </div>
                    <div class="col-xs-4" style="padding-top: 8px">
                        <div style="border: solid 1px #368fe7; border-radius: 5px !important; padding: 5px; text-align: center;">
                            <a href="#" id="btn_send_verifycode" style="color: #368fe7; text-decoration: none; font-size: 14px">下载客户端</a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- END CONTENT -->

        </div>
		
		<%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp" %> 
       </body>
    <script>

        jQuery(document).ready(function() {
            Metronic.init(); // init metronic core componets
            Layout.init(); // init layout

            toastr.options = {
                "positionClass": "toast-bottom-center"
            }

            $('#bottombar').css('top', $(window).outerHeight(true) - $('#bottombar').height() - 7);
        });

    </script>
 </html>
