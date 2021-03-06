<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
<meta charset="utf-8"/>
<title>Test Api</title>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.min.js" ></script>
</head>
<style>
.form div {padding: 3px}
</style>
<body>
<select id="sel_func">
	<option value="authPersonal">authPersonal</option>
	<option value="authEnter">authEnter</option>
	<option value="leaveEstimate">leaveEstimate</option>
	<option value="makeCorrect">makeCorrect</option>
</select>
<div class="form" id="authPersonal" style="display:none">
	<form action="api.html?pAct=authPersonal" method="post" enctype="multipart/form-data" accept-charset="utf-8">
		<div><label>logo :</label><input type="file" name="logo"></div>
		<div><label>realname :</label><input type="text" name="realname" value="崔光浩"></div>
		<div><label>certNum :</label><input type="text" name="certNum" value="34534234"></div>
		<div><label>certImange :</label><input type="file" name="certImage"></div>
		<div><label>enterName :</label><input type="text" name="enterName" value="PIC"></div>
		<div><label>xyleixingId :</label><input type="text" name="xyleixingId" value="1"></div>
		<div><label>cityId :</label><input type="text" name="cityId" value="1"></div>
		<div><label>job :</label><input type="text" name="job" value="职位"></div>
		<div><label>weixin :</label><input type="text" name="weixin" value="435345"></div>
		<div><label>experience :</label><input type="text" name="experience" value="工作经验"></div>
		<div><label>history :</label><input type="text" name="history" value="个人经历"></div>
		<div><label>xyWatch :</label><input type="text" name="xyWatch" value="1,2,4"></div>
		<div><label>xyWatched :</label><input type="text" name="xyWatched" value="2,3,5"></div>
		<input type="submit" value="Submit">
	</form>
</div>

<div class="form" id="authEnter" style="display:none">
	<form action="api.html?pAct=authEnter" method="post" enctype="multipart/form-data" accept-charset="utf-8">
		<div><label>logo :</label><input type="file" name="logo"></div>
		<div>
			<label>enterKind :</label>
			<select name="enterKind">
				<option value="1">企业</option>
				<option value="2">个体户</option>
			</select>
		</div>
		<div><label>enterCertNum :</label><input type="text" name="enterCertNum" value="34534234"></div>
		<div><label>enterCertImage :</label><input type="file" name="enterCertImage"></div>
		<div><label>enterName :</label><input type="text" name="enterName" value="PIC"></div>
		<div><label>xyleixingId :</label><input type="text" name="xyleixingId" value="1"></div>
		<div><label>cityId :</label><input type="text" name="cityId" value="1"></div>
		<div><label>addr :</label><input type="text" name="addr" value="具体地址"></div>
		<div><label>webUrl :</label><input type="text" name="webUrl" value="公司官网"></div>
		<div><label>weixin :</label><input type="text" name="weixin" value="435345"></div>
		<div><label>mainJob :</label><input type="text" name="mainJob" value="主营业务"></div>
		<div><label>comment :</label><input type="text" name="comment" value="公司介绍"></div>
		<div><label>recommend :</label><input type="text" name="recommend" value="我们承诺"></div>
		<div><label>bossName :</label><input type="text" name="bossName" value="负责人姓名"></div>
		<div><label>bossJob :</label><input type="text" name="bossJob" value="负责人职位"></div>
		<div><label>bossMobile :</label><input type="text" name="bossMobile" value="负责人手机号"></div>
		<div><label>bossWeixin :</label><input type="text" name="bossWeixin" value="负责人微信号"></div>
		<input type="submit" value="Submit">
	</form>
</div>

<div class="form" id="leaveEstimate" style="display:none">
	<form action="api.html?pAct=leaveEstimate" method="post" enctype="multipart/form-data" accept-charset="utf-8">
		<div>
			<label>type :</label>
			<select name="type">
				<option value="1">个人或企业</option>
				<option value="2">热点</option>
			</select>
		</div>
		<div><label>accountId :</label><input type="text" name="accountId" value="1"></div>
		<div><label>hotId :</label><input type="text" name="hotId" value="2"></div>
		<div>
			<label>kind :</label>
			<select name="kind">
				<option value="1">正面评价</option>
				<option value="2">负面评价</option>
			</select>
		</div>
		<div>
			<label>method :</label>
			<select name="method">
				<option value="1">完全评价</option>
				<option value="2">快捷评价</option>
			</select>
		</div>
		<div><label>reason :</label><input type="text" name="reason" value="原因"></div>
		<div><label>content :</label><input type="text" name="content" value="内容"></div>
		<div><label>images :</label><input type="file" name="images" multiple></div>
		<input type="submit" value="Submit">
	</form>
</div>
<div class="form" id="makeCorrect" style="display:none">
	<form action="api.html?pAct=makeCorrect" method="post" enctype="multipart/form-data" accept-charset="utf-8">
		<div><label>token :</label><input type="text" name="token" value=""></div>
		<div>
			<label>kind :</label>
			<select name="kind">
				<option value="1">夸大评价</option>
				<option value="2">虚假评价</option>
			</select>
		</div>
		<div><label>estimateId :</label><input type="text" name="estimateId" value=""></div>
		<div><label>reason :</label><input type="text" name="reason" value="原因"></div>
		<div><label>whyis :</label><input type="text" name="whyis" value="内容"></div>
		<div><label>images :</label><input type="file" name="images" multiple></div>
		<input type="submit" value="Submit">
	</form>
</div>
<div>
	<!-- <input type="button" value="testJson" onclick="testJson();"> -->
</div>
</body>
<script>
	$(document).ready(function() {
		$('#sel_func').change(function() {
			$('.form').hide();
			var func = $(this).val();
			$('#'+func).show();
		}).change();
	});
	
	function testJson() {
		var obj = {"pAct": "authPersonal", "mobile":"19135411631","xyWatch":JSON.stringify([{"id":"5"}, {"id":"7"}, {"id":"9"}])};
		$.post('api.html', obj).done(function(resp) {
			console.log(resp);
		});
	}
</script>
</html>
