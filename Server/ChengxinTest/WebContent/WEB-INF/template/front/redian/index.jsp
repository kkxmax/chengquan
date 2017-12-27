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
                    <h4>热点详情</h4>
                </div>
            </div>

            <div class="col-xs-12 init-div-horizontal" style="margin-top: 48px; padding-bottom:60px;">
                <div class="row init-div">
                    <div class="col-xs-12 margin-top-10">
                        <span id="txt_title" class="text-dark font-lg">从事食品行业的人，色不会吃哪些食品？</span>
                    </div>
                    <div class="col-xs-12  init-div-horizontal margin-bottom-10 margin-top-10">
                        <div class="col-xs-6">
                            <span class="text-gray-light font-sm">阅读：<span id="txt_read_count" class="text-blue font-sm"> 00</span></span>
                        </div>
                        <div class="col-xs-6 text-right">
                            <span id="txt_write_time" class="text-gray-light font-sm">2017-00-00 00:00</span>
                        </div>
                    </div>
                    
                    <div id="">
                        <iframe id="txt_content" src="" style="width:100%;height: 400px;"></iframe>
                    </div>
                    
                    <div class="col-xs-12 horizontal-divider-8"></div>
                    <div class="col-xs-12  init-div-horizontal">
                        <ul class="nav nav-tabs nav-tabs-sm" style="margin-bottom: 0;">
                            <li class="active" style="width: 50%;">
                                <a href="#tab_01" data-toggle="tab" class="text-center"> 评价</a>
                            </li>
                            <li style="width: 50%;">
                                <a href="#tab_02" data-toggle="tab" class="text-center"> 同行企业 </a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane fade active in" id="tab_01">
                            </div>
                            
                            <div class="tab-pane fade" id="tab_02">
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

            <div class="float-action-button">
                <img style="width:100%; height:100%;" src="assets/custom/img/call.png"/>
            </div>
            <!-- END CONTENT -->

        </div>
        <!-- END CONTAINER -->
<%@ include file="/WEB-INF/template/front/layout/body_bottom.jsp"%>
</body>

    <script>
    var hotId = getParameterByName("hotId");
    jQuery(document).ready(function() {
    	getHotDetail();
        loadEval();
        loadEnter();
        
    });

    function make_call() {
        alert("Call");
    }
    
    function getHotDetail(){
    	$.ajax({
			type : "POST",
			url : "api.html?share=1&pAct=getHotDetail",
		    data: {
            	hotId: hotId,
            },
			success : function(res) {
				Metronic.unblockUI('#content_div');
				if (res.retCode == 200) {
					var hot = res.hot;
					$('#txt_title').html(hot.title);
					$('#txt_content').attr('src' , uploadPath + hot.content);
					$('#txt_read_count').html(hot.visitCnt);
					$('#txt_write_time').html(hot.writeTimeString);
					$('#txt_comment_count').html(hot.commentCnt);
					$('#txt_elect_count').html(hot.electCnt);
					//toastr['success']("success");
					checkImgUrl();
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Metronic.unblockUI('#content_div');
				toastr['error']('网路错误！');
			}
		});
    }
    
    function loadEval() {
    	$.ajax({
			type : "POST",
			url : "api.html?share=1&pAct=getEstimateListForHot",
		    data: {
            	hotId: hotId,
            },
			success : function(res) {
				Metronic.unblockUI('#content_div');
				if (res.retCode == 200) {
					var data = res.data;
					var strHtml = "";
					if(data.length > 0){
				        for (var i=0; i < data.length; i++) {
				        	var item = data[i];
				        	var img = (item.ownerLogo) ? uploadPath + item.ownerLogo : "assets/custom/img/no_image.png"; 
				            strHtml += '<div class="row init-div" style="padding: 8px;">'+
				                                    '<div style="width: 50px; height: 50px;position: relative;">'+
				                                        '<img alt="" id="img_avatar_'+ i +'" src="' + img + '" style="width: 50px; height: 50px;" />'+
				                                    '</div>'+
				                                    '<div  style=" margin-left: 58px; margin-top: -50px;">'+
				                                        '<div class="row init-div">'+
				                                            '<span id="txt_name_'+ i +'" class="text-orange font-md">'+ item.ownerName +'</span>'+
				                                        '</div>'+
				                                        '<div class="row init-div margin-top-5">'+
				                                            '<span id="txt_content_'+ i +'" class="text-dark font-md">'+ item.content +'</span>'+
				                                        '</div>'+
				                                        '<div class="row init-div margin-top-5">'+
				                                            '<div class="col-xs-6 init-div-horizontal">'+
				                                                '<span id="txt_comment_date_'+ i +'" class="text-gray-light font-sm">'+ item.writeTimeString +'</span>'+
				                                            '</div>'+
				                                            '<div class="col-xs-6 text-right">'+
				                                                '<span class="text-blue font-sm">'+
				                                                    '<img src="assets/custom/img/zan_sel.png" style="width: 15px; height: 15px;padding:0 4px 4px 0;"><span id="txt_elect_cnt_'+ i +'">'+ item.electCnt +'</span>'+
				                                                    '<img src="assets/custom/img/evaluate.png" style="width: 15px; height: 15px;padding:0 4px 4px 0; margin-left: 4px;"><span id="txt_eval_cnt_'+ i +'">'+ item.replys.length +'</span>'+
				                                                '</span>'+
				                                            '</div>'+
				                                        '</div>';
				                                        if(item.replys.length > 0){
				                                        	strHtml += '<div class="row init-div margin-top-5" style="background: #f5f5f5; padding: 4px; border-radius: 4px !important;">'+
								                                            '<span id="txt_comment_'+ i +'" class="text-gray font-md">'+ item.replys[0].content +'</span>'+
								                                        '</div>'+
								                                        '<div class="row init-div margin-top-5">'+
								                                            '<span class="text-blue font-md">查看全部 <span id="txt_comment_cnt_'+ i +'">'+ item.replys.length +'条回复 ></span></span>'+
								                                        '</div>';
				                                        }
				                                    strHtml += '</div>'+
				                                    '<div class="col-xs-12 horizontal-divider margin-top-10"></div>'+
				                                '</div>';
				        }
					}
			        $('#tab_01').html(strHtml);
			        checkImgUrl();
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Metronic.unblockUI('#content_div');
				toastr['error']('网路错误！');
			}
		});
    }
    
    function loadEnter() {
    	$.ajax({
			type : "POST",
			url : "api.html?share=1&pAct=getPartnerList",
		    data: {
            	hotId: hotId,
            },
			success : function(res) {
				Metronic.unblockUI('#content_div');
				if (res.retCode == 200) {
					var data = res.data;
					var strHtml = "";
					if(data.length > 0){
				        for (var i=0; i < data.length; i++) {
				        	var item = data[i];
				        	var img = (item.logo) ? uploadPath + item.logo : "assets/custom/img/no_image.png";
				        	var product = "";
				        	if(item.products.length > 0){
					        	for(var j = 0 ; j < item.products.length ; j++){
					        		if(item.products.length == (j+1)){
					        			product += item.products[j].content;	
					        		}else{
					        			product += item.products[j].content +",";
					        		}
					        	}
				        	}
				        	var service = "";
				        	if(item.services.length > 0){
					        	for(var k = 0 ; k < item.services.length ; k++){
					        		if(item.services.length == (k+1)){
					        			service += item.services[k].content;	
					        		}else{
					        			service += item.services[k].content +",";
					        		}
					        	}
				        	}
				        	var name = "";
				        	var kind = "";
				        	if(item.akind == 1){
				        		name = item.realname;
				        		kind = "个人";
				        	}else{
				        		name = item.enterName;
				        		kind = "企业";
				        	}
				        
				            strHtml += '<div class="row init-div" style="padding: 8px;">'+
				                                    '<div style="width: 50px; height: 56px;position: relative;">'+
				                                        '<div style="width: 50px; height: 38px; overflow-y:hidden;">'+
				                                            '<img id="img_avatar'+ i +'" alt="" src="'+ img +'" style="width: 50px; height: 50px;" />'+
				                                        '</div>'+
				                                        '<div id="txt_enter_type'+ i +'" class="orange-gradient font-sm text-center" style="color:white; padding:2px;width: 50px; height: 18px;">'+
				                                            kind+
				                                        '</div>'+
				                                    '</div>'+
				                                    '<div style="float: left; margin-left: 58px; margin-top: -56px;">'+
				                                        '<div class="row init-div">'+
				                                            '<span id="txt_name'+ i +'" class="text-dark font-md bold">' + name + '</span>'+
				                                            '<span id="txt_leixing'+ i +'" class="text-blue font-sm"  style="border: solid 1px #1793E5; border-radius: 4px !important; padding: 1px 4px 1px 4px;margin-left: 4px;">'+ item.xyName +'</span>'+
				                                        '</div>'+
				                                        '<div class="row init-div margin-top-5"><span class="text-blue font-xs">诚信度：<span id="txt_rate'+ i +'"> '+ item.credit +'%</span></span></div>'+
				                                        '<div class="row init-div margin-top-5">'+
				                                            '<span class="blue-tag">主营产品</span><span id="txt_main_commodity'+ i +'" class="text-dark font-sm" style="margin-left: 4px;">'+ product +'</span>'+
				                                        '</div>'+
				                                        '<div class="row init-div margin-top-5">'+
				                                            '<span class="blue-tag">服务</span><span id="txt_service'+ i +'" class="text-dark font-sm" style="margin-left: 4px;">'+ service +'</span>'+
				                                        '</div>'+
				                                    '</div>'+
				                                    '<div class="col-xs-12 horizontal-divider margin-top-10"></div>'+
				                                '</div>';
				        }
					}
			        $('#tab_02').html(strHtml);
			        
			        checkImgUrl();
					//toastr['success']("success");
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Metronic.unblockUI('#content_div');
				toastr['error']('网路错误！');
			}
		});
    }
    

    </script>
    <!-- END BODY -->
</html>
