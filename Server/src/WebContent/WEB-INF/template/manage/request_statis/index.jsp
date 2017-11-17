<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!--[if IE 8]>
<html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]>
<html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<%@ include file="/WEB-INF/template/manage/layout/head.jsp"%>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp"%>

<!-- BEGIN CONTENT -->

<div class="col-md-12">
	<div class="portlet box blue-hoki">
		<div class="portlet-title">
			<div class="caption">
				<i class="icon-bar-chart"></i>邀请好友
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse" data-original-title=""
					title="collapse"> </a> <a href="javascript:;" class="remove"
					data-original-title="" title="Close"> </a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="row margin-bottom-10">
				<form class="form-inline" id="search-form">
					<div class="form-body col-md-12 col-sm-12">
						&nbsp;&nbsp;
						<div class="form-group">
							<label>发布时间: </label>
							<div name="down_time"
								class="form-date-filter input-group input-large date-picker input-daterange"
								data-date-format="yyyy-mm-dd">
								<input type="text" class="form-control" name="from" id="from"> <span
									class="input-group-addon">至</span> <input type="text"
									class="form-control" name="to" id="to">
							</div>
							<button class="btn btn-sm yellow hidden"
								onclick="loadTable();return false;">
								<i class="fa fa-search"></i> 查询
							</button>
						</div>
						&nbsp;&nbsp;
					</div>
				</form>
			</div>

			<div id="chart_2" class="chart"></div>
		</div>
	</div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp"%>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<!-- ArmCharts JS -->
<script
	src="${C_ASSETS_PATH}/global/plugins/amcharts/amcharts/amcharts.js"
	type="text/javascript"></script>
<script
	src="${C_ASSETS_PATH}/global/plugins/amcharts/amcharts/serial.js"
	type="text/javascript"></script>


<script>

function load_statis() {
	$.ajax({
		type: "POST",
		url: "${cur_page}?pAct=statis_search",
				data:{'from' : $('#from').val() , 'to': $('#to').val()},
				success: function (resp) {
					if (resp.retcode == 200) {
						records = resp.records;
						console.log(records);
						chart(records);
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					bootbox.alert("发生错误！");
				}
			});
}
	jQuery(document).ready(function() {
		$(".date-picker").datepicker($.extend({}, {language: "zh_TW"}));
		load_statis();
		
		$('#from , #to').change(function(){
			load_statis();
		});
		
	});
	function chart(data) {
		var chart = AmCharts.makeChart("chart_2", {
			"type" : "serial",
			"theme" : "light",

			"fontFamily" : 'Open Sans',
			"color" : '#888888',

			"legend" : {
				"equalWidths" : false,
				"useGraphSettings" : true,
				"valueAlign" : "left",
				"valueWidth" : 120
			},
			"dataProvider" : data,
			"valueAxes" : [ {
				"id" : "personalAxis",
				"axisAlpha" : 0,
				"gridAlpha" : 0,
				"position" : "left",
				"title" : "邀请好友统计"
			}],
			"graphs" : [ {
				"balloonText" : "邀请好友:[[value]]",
				"bullet" : "round",
				"bulletBorderAlpha" : 1,
				"useLineColorForBulletBorder" : true,
				"bulletColor" : "#FFFFFF",
				"bulletSizeField" : "10",
				"dashLengthField" : "dashLength",
				"legendValueText" : "[[value]]",
				"title" : "邀请好友",
				"fillAlphas" : 0,
				"valueField" : "request_cnt",
				"valueAxis" : "personalAxis"
			} ],
			"chartCursor" : {
				"categoryBalloonDateFormat" : "YYYY-MM-DD",
				"cursorAlpha" : 0.1,
				"cursorColor" : "#000000",
				"fullWidth" : true,
				"valueBalloonsEnabled" : false,
				"zoomable" : false
			},
			"dataDateFormat" : "YYYY-MM-DD",
			"categoryField": "click_date",
            "categoryAxis": {
                "gridPosition": "start"
            },
			"exportConfig" : {
				"menuBottom" : "20px",
				"menuRight" : "22px",
				"menuItems" : [ {
					"icon" : Metronic.getGlobalPluginsPath()
							+ "amcharts/amcharts/images/export.png",
					"format" : 'png'
				} ]
			}
		});

		$('#chart_2').closest('.portlet').find('.fullscreen').click(function() {
			chart.invalidateSize();
		});
	}
</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
