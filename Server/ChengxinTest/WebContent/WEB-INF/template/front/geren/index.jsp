<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en" class="no-js">
    <!-- BEGIN HEAD -->
    <head>
        <%@ include file="/WEB-INF/template/front/layout/head.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/front/layout/body_top.jsp" %>

        <div id="content_div" class="row init-div">
            <!--title bar-->
            <div class="navbar navbar-fixed-top">
                <div class="page-quick-sidebar page-nav">
                    <h4 style="margin-right:20px">
                        个人信息
                    </h4>
                </div>
                <!--header-->
                <div class="row init-div blue-gradient" style="padding: 10px 10px 10px 10px;margin-top: -1px">
                    <div class="col-xs-12 init-div-horizontal" style=" margin-bottom: 10px;">
                        <div style="float:left; width: 50px; height: 50px;">
                            <div style="width: 50px; height: 50px;">
                                <img id="img_logo" alt="" src="assets/custom/img/no_image.png" style="width: 50px; height: 50px;" />
                            </div>
                        </div>
                        <div class="row init-div" style="position: absolute;margin-top: 33px;width: 50px; height: 13px;">
                            <div class=" blue-gradient text-center">
                                <span id="" class="text-white font-xs">个人</span>
                            </div>
                        </div>
                        <div style="float:left; margin-left: 10px;margin-top:3px">
                            <div class="row init-div">
                                <span id="txt_name" class="text-white font-lg"></span>
                                <span id="txt_leixing" class="text-white font-sm"  style="border: solid 1px white; border-radius: 4px !important; padding: 1px 4px 1px 4px;"> </span>
                            </div>
                            <div class="row init-div"><span class="text-white font-sm">诚信代码：<span id="txt_code"></span></span></div>
                        </div>
                    </div>
                    <div class="col-xs-12 horizontal-divider-white"></div>
                    <div class="col-xs-12 init-div-horizontal" style="margin-top:7px">
                        <div class="col-xs-4 init-div-horizontal">
                            <div class="col-xs-6 init-div-horizontal text-right">
                                <span id="" class="text-white font-xs">诚信度：</span>
                            </div>
                            <div class="col-xs-6 init-div-horizontal text-left">
                                <span id="txt_rate" class="text-white font-xs"></span>
                            </div>
                        </div>
                        <div class="col-xs-4 init-div-horizontal">
                            <div class="col-xs-6 init-div-horizontal text-right">
                                <span class="text-white font-xs">点赞：</span>
                            </div>
                            <div class="col-xs-6 init-div-horizontal text-left">
                                <span id="txt_elect_cnt" class="text-white font-xs"></span>
                            </div>
                        </div>
                        <div class="col-xs-4 init-div-horizontal">
                            <div class="col-xs-6 init-div-horizontal text-right">
                                <span class="text-white font-xs">评价：</span>
                            </div>
                            <div class="col-xs-6 init-div-horizontal text-left">
                                <span id="txt_feedback_cnt" class="text-white font-xs"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 init-div-horizontal" style="margin-top: 153px; padding-bottom:60px;">
                <!--body-->
                <div class="row init-div">
                    <div class="col-xs-12 horizontal-divider-8"></div>

                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="col-xs-6 init-div-horizontal">
                            <div class="blue-prefix"></div>
                            <span class="text-gray font-md">其本信息</span>
                        </div>
                    </div>

                    <div class="col-xs-12 horizontal-divider"></div>

                    <div class="col-xs-12">
                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">所在公司：</span>
                                <a><span id="txt_enter" class="text-blue font-sm"></span></a>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>

                        <div class="col-xs-12 horizontal-divider"></div>

                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">公司网址：<a
								id="txt_weburl" class="text-blue" style="margin-left: 10px;"
								href="#"></a></span>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>

                        <div class="col-xs-12 horizontal-divider"></div>

                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-12 init-div-horizontal">
                                <span class="text-gray-light font-sm">所在地：</span>
                                <span id="txt_place" class="text-gray-light font-sm"></span>
                            </div>
                        </div>

                        <div class="col-xs-12 horizontal-divider"></div>

                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-12 init-div-horizontal">
                                <span class="text-gray-light font-sm">微信号：</span>
                                <span id="txt_weixin" class="text-gray-light font-sm"></span>
                            </div>
                        </div>

                        <div class="col-xs-12 horizontal-divider"></div>

                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-10 init-div-horizontal">
                                <span class="text-gray-light font-sm">引荐人：<a
								id="txt_link_man" class="text-blue" style="margin-left: 10px;"
								href="#"></a></span>
                            </div>
                            <div class="col-xs-2 init-div text-right">
                                <span class="text-gray-light"><i class="fa fa-angle-right"></i></span>
                            </div>
                        </div>

                        <div class="col-xs-12 horizontal-divider"></div>

                        <div class="col-xs-12 init-div-horizontal margin-bottom-10 margin-top-10">
                            <div class="col-xs-12 init-div-horizontal">
                                <span class="text-gray-light font-sm">职位：</span>
                                <span id="txt_job" class="text-gray-light font-sm"></span>
                            </div>
                        </div>

                    </div>

                    <div class="col-xs-12 horizontal-divider-8"></div>

                    <div class="col-xs-12  init-div-horizontal">
                        <ul class="nav nav-tabs nav-tabs-sm" style="margin-bottom: 0;">
                            <li class="active" style="width: 25%;">
                                <a href="#tab_01" data-toggle="tab" class="text-center"> 评价 </a>
                            </li>
                            <li style="width: 25%;">
                                <a href="#tab_02" data-toggle="tab" class="text-center"> 产品 </a>
                            </li>
                            <li style="width: 25%;">
                                <a href="#tab_03" data-toggle="tab" class="text-center"> 项目 </a>
                            </li>
                            <li style="width: 25%;">
                                <a href="#tab_04" data-toggle="tab" class="text-center"> 服务 </a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="tab_01">
                                <div class="row init-div" style="padding-top:8px;    padding-bottom: 16px;">
                                    <div class="col-xs-4 init-div-horizontal text-center">
                                        <button id="quanbu" clicked ="1" class="orange-gradient btn-eval text-white" style="width: 90%;">全部()</button>
                                    </div>
                                    <div class="col-xs-4 init-div-horizontal text-center">
                                        <button id="front" clicked ="0" class="btn-eval" style="width: 90%;">正面评价()</button>
                                    </div>
                                    <div class="col-xs-4 init-div-horizontal text-center">
                                        <button id="end" clicked ="0" class="btn-eval" style="width: 90%;">负面评价()</button>
                                    </div>
                                </div>

                                <div class="row init-div" id="item_pingjia">

                                </div>
                            </div>

                            <div class="tab-pane fade" id="tab_02">
                                <div class="row init-div" style="background: #666666;margin-top:1px">
                                </div>
                            </div>

                            <div class="tab-pane fade" id="tab_03">
                            </div>

                            <div class="tab-pane fade" id="tab_04">
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
        
        <%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp" %>
    </body>

	<script>
	var allEstimateData = new Array();
    var frontEstimateData = new Array();
    var endEstimateData = new Array();

    jQuery(document).ready(function() {
    	var shareUserId = getParameterByName("shareUserId");
    	var accountId = getParameterByName("accountId");

		$.ajax({
			type : "POST",
			url : "api.html?share=1&pAct=getAccountDetail",
			data:{
				shareUserId:shareUserId,
				accountId:accountId
			},
			success : function(res) {
				Metronic.unblockUI('#content_div');
				if (res.retCode == 200) {
					var account = res.account;
					//toastr['success']('验证码发送成功了，请您确认短信。');
					$('#img_logo').attr('src' , uploadPath + account.logo);
					$('#txt_enter').html(account.enterName);
					$('#txt_recommend').html(account.recommend);
					$('#txt_comment').html(account.comment);
					$('#txt_weburl').html(account.weburl);
					$('#txt_mainjob').html(account.mainjob);
					$('#txt_job').html(account.job);
					$('#txt_place').html(account.cityName);
					$('#txt_weixin').html(account.weixin);
					$('#txt_boss_name').html(account.bossName);
					$('#txt_boss_pos').html(account.bossJob);
					$('#txt_boss_mobile').html(account.bossMobile);
					$('#txt_boss_weixin').html(account.bossWeixin);
					$('#txt_city').html(account.cityName + " "+ account.addr);
					if (account.reqCodeSenderId > 0) {
						if (account.reqCodeSenderAkind == 1) {
							var senderName = account.reqCodeSenderRealname;
							if (senderName == "") {
								senderName = account.reqCodeSenderMobile;
							}
							if (account.inviterFriendLevel != "") {
								senderName = account.inviterFriendLevel+ "-"+ senderName;
							}
							$('#txt_link_man').html(senderName);
						} else {
							var senderName = account.reqCodeSenderEnterName;
							if (senderName == "") {
								senderName = account.reqCodeSenderMobile;
							}
							if (account.inviterFriendLevel != "") {
								senderName = account.inviterFriendLevel+ "-"+ senderName;
							}
							$('#txt_link_man').html(senderName);
						}
					} else {
						$('#txt_link_man').html(" ");
					}

					$('#img_cert_photo').attr('src',uploadPath + account.logo);
					$('#txt_leixing').html(account.xyName);
					$('#txt_name').html(account.realname);
					$('#txt_code').html(account.code);
					$('#txt_rate').html(account.credit+"%");
					$('#txt_elect_cnt').html(account.electCnt);
					$('#txt_feedback_cnt').html(account.feedbackCnt);
					
					allEstimateData = account.estimates;
					$('#quanbu').html('全部(' + allEstimateData.length + ")");
					if (allEstimateData.length > 0) {
						for(var j = 0; j < allEstimateData.length ; j++ ){
							if(allEstimateData[j].kind == 1){
								frontEstimateData.push(allEstimateData[j]);
							}else{
								endEstimateData.push(allEstimateData[j]);
							}
						}
					}
					$('#front').html('正面评价(' + frontEstimateData.length + ")");
					$('#end').html('负面评价(' + endEstimateData.length + ")");

					load_estimate(allEstimateData);

					load_comedity(account.products);
					load_item(account.items);
					load_serve(account.services);
					
					checkImgUrl();
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions,
					thrownError) {
				Metronic.unblockUI('#content_div');
				toastr['error']('网路错误！');
			}
		});

		$('#arrow_up').click(function() {
			var content = $('#arrow_up').html();
			if (content == "收起") {
				$('#slide_content').slideUp();
				$('#img_arrow_up').attr('src','assets/custom/img/arrow_down.png');
				$('#arrow_up').html("展开");
			} else {
				$('#slide_content').slideDown();
				$('#img_arrow_up').attr('src','assets/custom/img/arrow_up.png');
				$('#arrow_up').html("收起");
			}
		});

		$('#quanbu').click(function() {
			var clicked = $('#quanbu').attr('clicked');
			if (clicked != 1) {
				$('#quanbu').addClass("orange-gradient");
				$('#quanbu').addClass("text-white");
				$('#quanbu').removeClass("text-gray");
				$('#quanbu').attr('clicked', '1');
				$('#front').removeClass("orange-gradient");
				$('#front').attr('clicked', '0');
				$('#front').removeClass("text-white");
				$('#front').addClass("text-gray");
				$('#end').removeClass("orange-gradient");
				$('#end').attr('clicked', '0');
				$('#end').removeClass("text-white");
				$('#end').addClass("text-gray");
				load_estimate(allEstimateData);
				checkImgUrl();
			}
		});
		$('#front').click(function() {
			var clicked = $('#front').attr('clicked');
			if (clicked != 1) {
				$('#quanbu').removeClass("orange-gradient");
				$('#quanbu').removeClass("text-white");
				$('#quanbu').addClass("text-gray");
				$('#quanbu').attr('clicked', '0');
				$('#front').addClass("orange-gradient");
				$('#front').attr('clicked', '1');
				$('#front').addClass("text-white");
				$('#front').removeClass("text-gray");
				$('#end').removeClass("orange-gradient");
				$('#end').attr('clicked', '0');
				$('#end').removeClass("text-white");
				$('#end').addClass("text-gray");
				load_estimate(frontEstimateData);
				checkImgUrl();
			}
		});
		$('#end').click(function() {
			var clicked = $('#end').attr('clicked');
			if (clicked != 1) {
				$('#quanbu').removeClass("orange-gradient");
				$('#quanbu').removeClass("text-white");
				$('#quanbu').addClass("text-gray");
				$('#quanbu').attr('clicked', '0');
				$('#front').removeClass("orange-gradient");
				$('#front').attr('clicked', '0');
				$('#front').removeClass("text-white");
				$('#front').addClass("text-gray");
				$('#end').addClass("orange-gradient");
				$('#end').attr('clicked', '1');
				$('#end').addClass("text-white");
				$('#end').removeClass("text-gray");
				load_estimate(endEstimateData);
				checkImgUrl();
			}
		});
	});

    function make_call() {
    	alert("Call");
    }

    function load_item(items) {
    	var strHtml = "";
    	if(items.length > 0){
    		for (var i = 0; i < items.length ; i++) {
    			var item = items[i];
    			var img = (item.logo) ? (uploadPath + item.logo) : 'assets/custom/img/no_image.png'; 
    			strHtml += '<div class="row init-div" style="padding: 8px;">'
    					+ '<div style="width: 50px; height: 50px;position: relative;">'
    					+ '<img alt="" src="' + img + '" style="width: 50px; height: 50px;" />'
    					+ '</div>'
    					+ '<div  style="float: left; margin-left: 58px; margin-top: -50px;">'
    					+ '<div class="row init-div">'
    					+ '<span id="txt_name_' + i + '" class="text-dark font-md bold">' + item.name + '</span>'
    					+ '<span id="txt_leixing" class="text-blue font-sm"  style="border: solid 1px #1793E5; border-radius: 4px !important; padding: 1px 4px 1px 4px;margin-left: 4px;">'+ item.fenleiName +'</span>'
    					+ '</div>'
    					+ '<div class="row init-div margin-top-5" style="max-height: 60px;overflow-y: hidden;text-overflow: ellipsis;">'
    					+ '<span id="txt_content_' + i + '" class="text-dark font-md">'+ item.comment +'</span>'
    					+ '</div>'
    					+ '</div>'
    					+ '<div class="col-xs-12 horizontal-divider margin-top-10"></div>'
    					+ '</div>';
    		}
    	}

    	$('#tab_03').html(strHtml);
    }
    function load_serve(services) {
    	var strHtml = "";
    	if(services.length > 0){
    		for (var i = 0; i < services.length; i++) {
    			var item = services[i];
    			var img = (item.logo) ? (uploadPath + item.logo) : 'assets/custom/img/no_image.png';
    			strHtml += '<div class="row init-div" style="padding: 8px;">'
    					+ '<div style="width: 50px; height: 50px;position: relative;">'
    					+ '<img alt="" src="' +uploadPath + img + '" style="width: 50px; height: 50px;" />'
    					+ '</div>'
    					+ '<div  style="float: left; margin-left: 58px; margin-top: -50px;">'
    					+ '<div class="row init-div">'
    					+ '<span id="txt_name_' + i + '" class="text-dark font-md bold">'+ item.name +'</span>'
    					+ '<span id="txt_leixing" class="text-blue font-sm"  style="border: solid 1px #1793E5; border-radius: 4px !important; padding: 1px 4px 1px 4px;margin-left: 4px;">'+ item.fenleiName +'</span>'
    					+ '</div>'
    					+ '<div class="row init-div margin-top-5" style="max-height:60px;overflow-y:hidden">'
    					+ '<span id="txt_content_' + i + '" class="text-dark font-md">' + item.comment + '</span>'
    					+ '</div>'
    					+ '</div>'
    					+ '<div class="col-xs-12 horizontal-divider margin-top-10"></div>'
    					+ '</div>';
    		}
    	}
    	$('#tab_04').html(strHtml);

    }
    function load_estimate(estimates){
    	var strHtml = "";
    	if (estimates.length > 0) {
    		for (var i = 0; i < estimates.length; i++) {
    			var item = estimates[i];
    			var img = (item.ownerLogo) ? (uploadPath + item.ownerLogo) : 'assets/custom/img/no_image.png';
    			var name = (item.ownerAkind == 1) ? item.ownerName : item.ownerEnterName;
    			
    			strHtml += '<div class="row init-div" style="padding: 8px;">' ;
    			if(item.isFalse == 1){
    				strHtml += '<div style="position: absolute;right: 0;">'+
    								'<img src="assets/custom/img/label_bushushi.png" style="width: 55px;">'+
    						'</div>';
    			}
    			strHtml += '<div style="width: 50px; height: 50px;position: relative;">' +
                '<img alt="" id="img_avatar_0" src="' + img + '" style="width: 50px; height: 50px;">' +
                '</div>' ;

                strHtml += '<div style="margin-left: 58px;margin-top:-50px">';
                		
                strHtml += '<div class="row init-div">' +
                				'<div class="col-xs-12 init-div-horizontal">' +
                					'<div class="col-xs-6 init-div-horizontal">' +
                						'<span id="txt_name_0" class="text-orange font-md">'+ name +'</span>' +
                					'</div>' +
                					'<div class="col-xs-6 init-div-horizontal text-right" style="margin-left: -10px;">' +
                						'<span id="txt_name_0" class="text-orange font-md">' + item.kindName + '</span>' +
                					'</div>' +
                				'</div>' +
                			'</div>' +
                			'<div class="row init-div margin-top-5">' +
                				'<span id="txt_content_0" class="text-dark font-md">'+ item.content +'</span>' +
                			'</div>' +
                			'<div class="row init-div margin-top-10">';

                            if (item.imgPaths.length > 0) {
                            	strHtml +='<div class="col-xs-12 init-div-horizontal" style="height: 91px; overflow-x: auto; overflow-y: hidden">';
            	        		strHtml +='<div style="height:90px; width: ' + (125 * item.imgPaths.length) + 'px">';
            					for (var x = 0; x < item.imgPaths.length; x++) {
            						strHtml += '<img alt="" src="' + (uploadPath + item.imgPaths[x]) + '" style="width: 120px; height: 90px; margin-right: 5px;">';
            					}	        		
            	                strHtml += '</div></div>';
                            }

                            strHtml += '</div>' +
                            			'<div class="row init-div margin-top-5">' +
                				'<div class="col-xs-6 init-div-horizontal">' +
                					'<span id="txt_comment_date_0" class="text-gray-light font-sm">' + item.writeTimeString + '</span>' +
                				'</div>' +
                				'<div class="col-xs-6 text-right">' +
                					'<span class="text-blue font-sm">' +
                						'<img src="assets/custom/img/zan_sel.png" style="width: 15px; height: 15px;padding:0 4px 4px 0;">' +
                						'<span id="txt_elect_cnt_0">('+ item.electCnt +')</span>' +
                						'<img src="assets/custom/img/evaluate.png" style="width: 15px; height: 15px;padding:0 4px 4px 0; margin-left: 4px;">' +
                						'<span id="txt_eval_cnt_0">('+ item.replys.length +')</span>' +
                						'<img src="assets/custom/img/error.png" style="width: 15px; height: 15px;padding:0 4px 4px 0; margin-left: 4px;">' +
                					'</span>' +
                				'</div>' +
                			'</div>' ;
                if(item.replys.length >0){
                strHtml += '<div class="row init-div margin-top-5" style="background: #f5f5f5; padding: 4px; border-radius: 4px !important;">' +
                				'<span id="txt_comment_0" class="text-gray font-md">'+item.replys[0].content+'</span>' +
                			'</div>' +
                			'<div class="row init-div margin-top-5">' +
                				'<span class="text-blue font-md">查看全部 ' +
                					'<span id="txt_comment_cnt_0">'+ item.replys.length +'条回复 &gt;</span>        ' +
                				'</span>' +
                			'</div>' ;
                }
                strHtml += '</div>' +
                			'<div class="col-xs-12 horizontal-divider margin-top-10"></div>' +
                			'</div>';
    		}
    	}
    	
    	$('#item_pingjia').html(strHtml);
    }

    function load_comedity(products) {
		var strHtml = "";
		if(products.length > 0){
			for (var i = 0; i < products.length; i++) {
				var item = products[i]
				var img = (item.imgPaths[0]) ? (uploadPath + item.imgPaths[0]) : 'assets/custom/img/no_image.png';  
				if (i % 2 == 0) {
					strHtml += '<div class="col-xs-12 init-div-horizontal">';
				}
				strHtml += '<div class="col-xs-6 init-div-horizontal" style="background:#FFFFFF;padding-right: 2px">'
					+ '<div class="row init-div">'
					+ '<img alt="" src="'+ img +'" style="width: 100%; height: 180px;" />'
					+ '</div>'
					+ '<div class="row init-div" style="padding: 7px 7px 3px 7px;max-height:31px;overflow-y:hidden;">'
					+ '<span class="text-gray-light font-md" id="comedity_title">'+ item.name +'</span>'
					+ '</div>'
					+ '<div class="row init-div" style="padding: 0px 7px 7px 7px;">'
					+ '<span class="text-orange font-md" id="comedity_price">￥'+ item.price.toFixed(2) + '</span>'
					+ '</div>' + '</div>' ;
				if(i%2 != 0){ 
					strHtml += '</div>';
				}
				
			}
		}
		$('#tab_02').html(strHtml);
	}
    </script>
</html>