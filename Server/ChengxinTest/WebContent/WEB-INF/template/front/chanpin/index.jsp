<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html lang="en" class="no-js">
<!-- BEGIN HEAD -->
<head>
<%@ include file="/WEB-INF/template/front/layout/head.jsp"%>
<style>
#owl-demo .item img{
        display: block;
        width: 100%;
        height: auto;
    }
</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp"%>
            <!--title bar-->
            <div class="navbar navbar-fixed-top">
                <div class="page-quick-sidebar page-nav">
                    <h4>产品详情</h4>
                </div>
            </div>
            
            <div class="col-xs-12 init-div-horizontal" style="margin-top: 48px; padding-bottom:60px;">
                <!--Slider-->
                <!-- <div class="page-slider">
                    <div id="owl-carousel" class="owl-carousel owl-theme">
					</div>
                </div> -->
                
                <div id="owl-demo" class="owl-carousel owl-theme">
				</div>

                <div class="row init-div">
                    <div class="col-xs-12 margin-bottom-10 margin-top-10">
                        <span class="blue-tag">主营产品</span>
                        <span id="txt_name" class="text-dark font-md">恒大牛肉干</span>
                    </div>
                    <div class="col-xs-12 margin-bottom-5">
                        <span id="txt_price" class="text-orange font-md bold">¥00.00</span>
                    </div>
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="col-xs-6 init-div-horizontal">
                            <div class="blue-prefix"></div>
                            <span id="txt_name" class="text-gray font-md">产品介绍</span>
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
                            <div class="col-xs-2 text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>
                        
                        <div class="col-xs-12 horizontal-divider"></div>
                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">实体店地址：</span><span id="txt_place" class="text-dark font-md">北京市朝阳区新源里109</span>
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
                                <div class="row init-div margin-top-5"><span class="text-gray font-sm">诚信代码： <span id="txt_enter_code"> 1234567890</span></span></div>
                                <div class="row init-div margin-top-5"><span class="text-blue font-xs">诚信度：<span id="txt_enter_rate"> 90%</span></span></div>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="col-xs-12" style="background: #f5f5f5; padding: 10px;">
                        <a id="btn_buy" href="#" class="col-xs-12 orange-gradient font-lg text-center" style="color:white; padding: 8px; border-radius:4px !important;;">立即购买</a>
                    </div> -->
                </div>
                
            </div>
            
            <div class="front-page-footer">
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
            getProductDetail();
            checkImgUrl();
        });
        
        
        function initOwlCarousel(){
        	$("#owl-demo").owlCarousel({
                navigation: false, // Show next and prev buttons
                slideSpeed: 300,
                paginationSpeed: 400,
                singleItem: true,
                autoPlay: true,
                stopOnHover: true
            });
        }
        

        function getProductDetail() {
        	var productId = getParameterByName("productId");
            $.ajax({
                type: "POST",
                url: "api.html?share=1&pAct=getProductDetail",
                data: {
                	productId: productId,
                },
                success: function(res) {
                    Metronic.unblockUI('#content_div');
                    if (res.retCode == 200) {
                    	var product = res.product;
                    	var imgStr = "";
                    	var imgPaths = product.imgPaths;
                    	if(imgPaths.length > 0){
                    		for(var i = 0 ; i < imgPaths.length ; i++){
                    			var imgs = (imgPaths[i]) ? uploadPath + imgPaths[i] : "assets/custom/img/no_image.png";
                    			imgStr += '<div class="item"><img src="' + imgs + '" alt="产品图片"></div>';
	   
	                       	}
                    	}
                    	if(imgStr == ""){
                    		imgStr += '<div class="item"><img src="assets/custom/img/no_image.png" alt="产品图片"></div>';
                    	}
             
                    	$('#owl-demo').html(imgStr);
                    	initOwlCarousel();
                    	$('#txt_name').html(product.name);
                    	$('#txt_price').html("￥ " + product.price.toFixed(2));
                    	$('#txt_code').html(product.code);
                    	$('#txt_comment').html(product.comment);
                    	$('#txt_weburl').html(product.weburl);
                    	$('#txt_place').html(product.cityName + " " + product.saleAddr);
                    	$('#txt_enter_name').html(product.enterName);
                    	if(product.enterKind == 2){
                    		$('#txt_enter_type').html("个体户");	
                    	}else{
                    		$('#txt_enter_type').html("企业");
                    	}
                    	
                    	$('#txt_enter_code').html(product.accountCode);
                    	if(product.accountCredit <= 0){
                    		$('#txt_enter_rate').html("暂无");
                    	}else{
                    		$('#txt_enter_rate').html(product.accountCredit + "%");
                    	}
                    	var img = (product.accountLogo) ? (uploadPath + product.accountLogo) : "assets/custom/img/no_image.png";
                    	$('#img_avatar').attr("src" , img);
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
