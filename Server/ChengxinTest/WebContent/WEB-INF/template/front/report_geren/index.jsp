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
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp"%>
            <!--title bar-->
            <div class="navbar navbar-fixed-top">
                <div class="page-quick-sidebar page-nav">
                    <h4>诚信报告</h4>
                </div>
                <div class="row init-div blue-gradient" style="padding: 10px 10px 10px 15px;margin-top: -1px;">
                    <div class="row init-div">
                        <div style="float:left; width: 50px; height: 50px;">
                            <div style="width: 50px; height: 50px;">
                                <img id="img_logo" alt="" src="assets/custom/img/no_image.png" style="width: 50px; height: 50px;" />
                            </div>
                        </div>
                        <div style="float:left; margin-left: 10px;">
                            <div class="row init-div">
                                <span id="txt_name" class="text-white font-lg"></span>
                                <span id="txt_leixing" class="text-white font-sm"  style="border: solid 1px white; border-radius: 4px !important; padding: 1px 4px 1px 4px; margin-left: 4px;"></span>
                            </div>
                            <div class="row init-div margin-top-5"><span class="text-white font-sm">诚信代码：<span id="txt_code"></span></span></div>
                        </div>
                    </div>
                    <div class="row init-div horizontal-divider-white margin-top-5"></div>
                    <div class="row init-div margin-top-10">
                        <div class="col-xs-4 init-div-horizontal text-center text-white font-xs">
                            <span>诚信度：<span id="txt_rate"></span></span>
                        </div>
                        <div class="col-xs-4 init-div-horizontal text-center text-white font-xs">
                            <span>点赞：<span id="txt_elect_cnt"></span></span>
                        </div>
                        <div class="col-xs-4 init-div-horizontal text-center text-white font-xs">
                            <span>评价：<span id="txt_feedback_cnt"></span></span>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-xs-12 init-div-horizontal" style="margin-top: 150px; padding-bottom:60px;">
                

                <div class="row init-div">
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="blue-prefix"></div>
                        <span class="text-gray font-md">基本信息</span>
                    </div>
                    <div class="col-xs-12 horizontal-divider"></div>
                    <div class="col-xs-12">
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">所在公司：<span id="txt_enter_name" class="text-blue" style="margin-left: 10px;"></span></span>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>
                        <div class="row init-div horizontal-divider"></div>
                        <!-- <div class="row init-div margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">公司官网：<a id="txt_weburl" class="text-blue" style="margin-left: 10px;" href="#"></a></span>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div> -->
                        <div class="row init-div horizontal-divider"></div>
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">所在地：</span><span id="txt_city" class="text-dark font-md"></span>
                        </div>
                        <div class="row init-div horizontal-divider"></div>
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">微信号：</span><span id="txt_weixin" class="text-dark font-md"></span>
                        </div>
                        <div class="row init-div horizontal-divider"></div>
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">职位：</span><span id="txt_pos" class="text-dark font-md"></span>
                        </div>
                        <div class="row init-div horizontal-divider"></div>
                        <div class="row init-div margin-bottom-10 margin-top-10">
                            <span class="text-gray-light font-sm">引荐人：</span><span id="txt_recommender" class="text-blue font-md"></span>
                        </div>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="blue-prefix"></div>
                        <span class="text-gray font-md">个人经历</span>
                    </div>
                    <div class="col-xs-12 horizontal-divider"></div>
                    <div class="col-xs-12 margin-bottom-10 margin-top-10">
                        <span id="txt_history" class="text-dark font-md"></span>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="blue-prefix"></div>
                        <span class="text-gray font-md">工作经验</span>
                    </div>
                    <div class="col-xs-12 horizontal-divider"></div>
                    <div class="col-xs-12 margin-bottom-10 margin-top-10">
                        <span id="txt_experience" class="text-dark font-md"></span>
                    </div>
                    
                    <div class="row init-div" id="recommender_part" style="display: none;">
	                    <div class="col-xs-12 horizontal-divider-8"></div>
	                    <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
	                        <div class="blue-prefix"></div>
	                         <span class="text-gray font-md">我的引荐人</span>
	                    </div>
	                    
	                    <div class="col-xs-12 horizontal-divider"></div>
	                    <div class="col-xs-12">
	                        <div class="margin-bottom-10 margin-top-10">
	                            <div style="float:left; width: 50px; height: 56px;">
	                                <div style="width: 50px; height: 38px; overflow-y:hidden;">
	                                    <img id="img_avatar" alt="" src="assets/custom/img/no_image.png" style="width: 50px; height: 50px;" />
	                                </div>
	                                <div id="txt_enter_type" class="orange-gradient font-sm text-center" style="color:white; padding:2px;width: 50px; height: 18px;">
	                                    公司
	                                </div>
	                            </div>
	                            <div style="float:left; margin-left: 10px;">
	                                <div class="row init-div">
	                                    <span id="txt_enter_name_1" class="text-dark font-md"></span>
	                                    <span id="txt_enter_leixing" class="text-blue font-sm"  style="border: solid 1px #1793E5; border-radius: 4px !important; padding: 1px 4px 1px 4px;margin-left: 4px;">服装</span>
	                                </div>
	                                <div class="row init-div margin-top-5"><span class="text-gray font-sm">诚信代码: <span id="txt_enter_code"> </span></span></div>
	                                <div class="row init-div margin-top-5"><span class="text-blue font-xs">诚信度:<span id="txt_enter_rate"> </span></span></div>
	                            </div>
	                        </div>
	                        <div class="col-xs-12 horizontal-divider margin-bottom-10 margin-top-10"></div>
	                        <div class="row init-div margin-bottom-10">
	                            <div class="col-xs-4 init-div-horizontal text-center text-gray-light font-xs">
	                                <span>诚信度：<span id="txt_enter_rate_1" class="text-blue"></span></span>
	                            </div>
	                            <div class="col-xs-4 init-div-horizontal text-center text-gray-light font-xs">
	                                <span>点赞：<span id="txt_enter_elect_cnt" class="text-blue"></span></span>
	                            </div>
	                            <div class="col-xs-4 init-div-horizontal text-center text-gray-light font-xs">
	                                <span>评价：<span id="txt_enter_feedback_cnt" class="text-blue"></span></span>
	                            </div>
	                        </div>
	                    </div>
                    </div>
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
       	<%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp"%>
       </body>
    
    <script>
    var shareUserId = getParameterByName("shareUserId");
	var accountId = getParameterByName("accountId");
        jQuery(document).ready(function() {
//            $('#bottombar').css('top', $(window).outerHeight(true) - $('#bottombar').height() - 7);
        	getReportGerenCode();
        	getInviterInfo();
        	checkImgUrl();
        });
        
        function make_call() {
            alert("Call");
        }

        function getReportGerenCode() {
            $.ajax({
                type: "POST",
                url: "api.html?share=1&pAct=getAccountDetail",
                data:{
					shareUserId:shareUserId,
					accountId:accountId
				},
                success: function(res) {
                    Metronic.unblockUI('#content_div');
                    if (res.retCode == 200) {
                    	var account = res.account;
                    	var img = "";
                    	img = (account.logo) ? (uploadPath+account.logo) : "assets/custom/img/no_image.png";
                    	$('#img_logo').attr('src' , img);
                    	var name = "";
                    	if(account.testStatus == 2){
                    		name = (account.akind == "1") ? account.realname : account.enterName;
                    	}else{
                    		name = account.mobile;
                    	}
                    	$('#txt_name').html(name);
                    	$('#txt_leixing').html(account.xyName);
                    	$('#txt_code').html(account.code);
                    	var rate = (account.credit == 0)? "暂无" :"" + account.credit + "%" ;
                    	$('#txt_rate').html(rate);
                    	$('#txt_elect_cnt').html(account.electCnt);
                    	$('#txt_feedback_cnt').html(account.feedbackCnt);
                    	$('#txt_enter_name').html(account.enterName);
                    	$('#txt_city').html(account.cityName);
                    	$('#txt_weburl').html(account.weburl);
                    	$('#txt_weixin').html(account.weixin);
                    	$('#txt_pos').html(account.job);
                    	$('#txt_history').html(account.history);
                    	$('#txt_experience').html(account.experience);
                    	
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
        
        function getInviterInfo(){
            $.ajax({
                type: "POST",
                url: "api.html?share=1&pAct=getInviterInfo",
                data:{
					accountId:accountId
				},
                success: function(res) {
                    Metronic.unblockUI('#content_div');
                    if (res.retCode == 200) {
                    	if (res.inviterInfo) {
	                    	var inviterInfo = res.inviterInfo;
	                    	var img = "";
	                    	if (inviterInfo.akind == 1) {
	                    		img = (inviterInfo.certImage) ? uploadPath + inviterInfo.certImage : "assets/custom/img/no_image.png";
	                    	} else {
	                    		img = (inviterInfo.enterCertImage) ? uploadPath + inviterInfo.enterCertImage : "assets/custom/img/no_image.png";
	                    	}
	                    	$('#img_avatar').attr('src' , img);
	                    	$('#txt_enter_type').text(inviterInfo.akind == 1 ? "个人" : "企业");
	                    	var inviterName = inviterInfo.akind == 1 ? inviterInfo.realname : inviterInfo.enterName;
	                    	$('#txt_enter_name_1').html(inviterName);
	                    	$('#txt_enter_leixing').html(inviterInfo.xyName);
	                    	$('#txt_enter_code').html(inviterInfo.code);
	                    	var credit = inviterInfo.credit == 0 ? "暂无" : inviterInfo.credit + "%";
	                    	$('#txt_enter_rate').html(credit);
	                    	$('#txt_enter_rate_1').html(credit);
	                    	$('#txt_enter_elect_cnt').html(inviterInfo.electCnt);
	                    	$('#txt_enter_feedback_cnt').html(inviterInfo.feedbackCnt);
	                    	
	                    	var recommendName = "没有";
	                    	if(inviterInfo.akind == 1){
	                    		recommendName = (inviterInfo.realName) ? inviterInfo.realName : inviterInfo.mobile; 
	                    	}else{
	                    		recommendName = inviterInfo.enterName;
	                    	}
	                    	$('#txt_recommender').html(recommendName);
	                    	$('#recommender_part').show();
                    	} else {
                    		$('#recommender_part').hide();
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
