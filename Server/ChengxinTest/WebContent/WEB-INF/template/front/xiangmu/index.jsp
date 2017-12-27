<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html lang="en" class="no-js">
<!-- BEGIN HEAD -->
<head>
<%@ include file="/WEB-INF/template/front/layout/head.jsp"%>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp"%>            <!--title bar-->
            <div class="navbar navbar-fixed-top">
                <div class="page-quick-sidebar page-nav">
                    <h4>项目详情</h4>
                </div>
                <div class="row init-div blue-gradient" style="padding: 10px 10px 10px 15px;margin-top:-1px">
                    <div>
                        <div style="float:left; width: 50px; height: 50px;">
                            <div style="width: 50px; height: 50px;">
                                <img id="img_logo" alt="" src="assets/custom/img/no_image.png" style="width: 50px; height: 50px;" />
                            </div>
                        </div>
                        <div style="float:left; margin-left: 10px;">
                            <div class="row init-div">
                                <span id="txt_name" class="text-white font-lg bold">北京阿里</span>
                                <span id="txt_leixing" class="text-white font-sm"  style="border: solid 1px white; border-radius: 4px !important; padding: 1px 4px 1px 4px; margin-left: 4px;">服装</span>
                            </div>
                            <div class="row init-div"><span class="text-white font-sm">所在城市：<span id="txt_city"> 北京</span></span></div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-xs-12 init-div-horizontal" style="margin-top: 120px; padding-bottom:60px;">

                <div class="row init-div">
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="col-xs-6 init-div-horizontal">
                            <div class="blue-prefix"></div>
                            <span id="txt_name" class="text-gray font-md">项目介绍</span>
                        </div>
                        <div class="col-xs-6 text-right">
                            <span class="text-gray-light font-sm">编号：<span id="txt_code">000000</span></span>
                        </div>
                    </div>
                    <div class="col-xs-12 horizontal-divider"></div>
                    <div class="col-xs-12 margin-bottom-10 margin-top-10">
                        <span id="txt_comment" class="text-dark font-md">我想知道如何解决开发中遇到的这个问题呢？困扰我好久了。主播，您好!  我想知道如何解决开发中遇到的这个问题呢？</span>
                    </div>
                    
                    <div class="col-xs-12">
                        <div class="col-xs-12 horizontal-divider"></div>
                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">网址：<a id="txt_weburl" class="text-blue" style="margin-left: 10px;" href="#">www.xiaoming.com</a></span>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12">
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">所需资源：</span><span id="txt_resource" class="text-dark font-md">北京市朝阳区新源里109</span>
                        </div>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="blue-prefix"></div>
                         <span id="txt_name" class="text-gray font-md">联系我信息</span>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider"></div>
                    <div class="col-xs-12">
                        <div class="row init-div">
                            <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                                <span class="text-gray-light font-sm">姓名：</span><span id="txt_relation_name" class="text-dark font-md">小明</span>
                            </div>
                        </div>
                        
                        <div class="col-xs-12 horizontal-divider"></div>
                        <div class="row init-div">
                            <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                                <span class="text-gray-light font-sm">手机号：</span><span id="txt_relation_mobile" class="text-dark font-md">167827829</span>
                            </div>
                        </div>
                        
                        <div class="col-xs-12 horizontal-divider"></div>
                        <div class="row init-div">
                            <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                                <span class="text-gray-light font-sm">验证号：</span><span id="txt_relation_weixin" class="text-dark font-md">167827829</span>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12  init-div-horizontal">
                        <div class="blue-prefix-high"></div>
                        <div class="margin-bottom-10 margin-top-10" style="margin-left:10px;">
                            <div style="float:left; width: 50px; height: 56px;">
                                <div style="width: 50px; height: 38px; overflow-y:hidden;">
                                    <img id="img_avatar" alt="" src="assets/custom/img/no_image.png" style="width: 50px; height: 50px;" />
                                </div>
                                <div id="txt_enter_type" class="orange-gradient font-sm text-center" style="color:white; padding:2px;width: 50px; height: 18px;">
                                    公司
                                </div>
                            </div>
                            <div style="float:left; margin-left: 10px;">
                                <div class="row init-div"><span id="txt_enter_name" class="text-dark font-md">北京阿里有限公司</span></div>
                                <div class="row init-div margin-top-5"><span class="text-gray font-sm">诚信代码: <span id="txt_enter_code"> 1234567890</span></span></div>
                                <div class="row init-div margin-top-5"><span class="text-blue font-xs">诚信度:<span id="txt_enter_rate"> 90%</span></span></div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="row init-div">
                        <div class="col-xs-12 margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">发布时间：<span id="txt_write_time">2000-00-00 00:00:00</span></span>
                        </div>
                    </div>
                </div>
                
            </div>
            
            <div class="page-footer">
                <div class="row init-div">
                    <div class="col-xs-7">
                        <img src="assets/custom/img/login_logo.png" style="width: 50px; float: left">
                        <div style="padding-left: 56px; padding-top: 6px">
                            <p style="font-size: 18px; font-weight: bold; color: #1793e5; margin-bottom: 0">诚乎</p>
                            <p style="color: #1793e5">你的话语权主场</p>
                        </div>
                    </div>
                    <div class="col-xs-5" style="padding-top: 8px">
                        <div style="border: solid 1px #368fe7; border-radius: 4px !important; padding: 4px; text-align: center;">
                            <a href="#" id="btn_send_verifycode" style="color: #368fe7; text-decoration: none; font-size: 14px">下载客户端</a>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- <div class="float-action-button">
                <img style="width:100%; height:100%;" src="assets/custom/img/call.png"/>
            </div> -->
            <!-- END CONTENT -->

        </div>
        <!-- END CONTAINER -->


        <%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp"%>
    </body>
    
    <script>
        jQuery(document).ready(function() {
//            $('#bottombar').css('top', $(window).outerHeight(true) - $('#bottombar').height() - 7);
				getItemDetail();
				checkImgUrl();
        });
        
        function make_call() {
            alert("Call");
        }

        function getItemDetail() {
        	var itemId = getParameterByName("itemId");
            $.ajax({
                type: "POST",
                url: "api.html?share=1&pAct=getItemDetail",
                data: {
                    itemId: itemId,
                },
                success: function(res) {
                    Metronic.unblockUI('#content_div');
                    if (res.retCode == 200) {
                    	var item = res.item;
                    	var img_logo = (item.logo) ? uploadPath + item.logo : "assets/custom/img/no_image.png";
                    	var img_enter_logo = (item.accountLogo) ? uploadPath + item.accountLogo : "assets/custom/img/no_image.png";
                    	$('#img_logo').attr('src' , img_logo);
                    	$('#img_avatar').attr('src' , img_enter_logo);
                    	$('#txt_name').html(item.name);
                    	$('#txt_leixing').html(item.fenleiName);
                    	$('#txt_city').html(item.cityName);
                    	$('#txt_code').html(item.code);
                    	$('#txt_comment').html(item.comment);
                    	$('#txt_weburl').html(item.weburl);
                    	$('#txt_resource').html(item.need);
                    	$('#txt_relation_name').html(item.contactName);
                    	$('#txt_relation_mobile').html(item.contactMobile);
                    	$('#txt_relation_weixin').html(item.contactWeixin);
                    	if(item.akind == 1){
                    		$('#txt_enter_type').html("个人");	
                    	}else{
                    		if(item.enterKind == 2){
                    			$('#txt_enter_type').html("个体户");
                    		}else{
                    			$('#txt_enter_type').html("企业");
                    		}
                    	}
                    	$('#txt_enter_name').html(item.accountName);
                    	$('#txt_enter_code').html(item.accountCode);
                    	$('#txt_write_time').html(item.writeTimeString);
                    	if(item.accountCredit < 0){
                    		$('#txt_enter_rate').html("暂无");
                    	}else{
                    		$('#txt_enter_rate').html(item.accountCredit + "%");
                    	}
                        //toastr['success']('验证码发送成功了，请您确认短信。');
                    } else {
                        toastr['error'](res.msg);
                    }
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    Metronic.unblockUI('#content_div');
                    toastr['error']('网路错误！');
                }
            });
        }

    </script>
    <!-- END BODY -->
</html>
