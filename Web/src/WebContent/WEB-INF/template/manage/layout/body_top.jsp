<%@ page contentType="text/html;charset=utf-8" %>

<body class="page-header-fixed page-quick-sidebar-over-content">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
  <!-- BEGIN HEADER INNER -->
  <div class="page-header-inner">
    <!-- BEGIN LOGO -->
    <div class="page-logo">
      <a class="logo" href="personal_account.html">
        <%-- <img src="${C_ASSETS_PATH}/admin/layout/img/logo.png" alt="logo" class="logo-default"/> --%>
        <span>诚全后台</span>
      </a>
      <div class="menu-toggler sidebar-toggler hide">
      </div>
    </div>
    <!-- END LOGO -->
    <!-- BEGIN RESPONSIVE MENU TOGGLER -->
    <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse"
       data-target=".navbar-collapse">
    </a>
    <!-- END RESPONSIVE MENU TOGGLER -->
    <!-- BEGIN TOP NAVIGATION MENU -->
    <div class="top-menu">
      <ul class="nav navbar-nav pull-right">
        <!-- BEGIN USER LOGIN DROPDOWN -->
        <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
        <li class="dropdown">
            <a href="notice.html" class="dropdown-toggle">
                <i class="icon-bell" style="padding-right: 30px"></i><span id="notice_cnt" class="badge badge-danger" style="right: 3px;"></span>
            </a>
        </li>
        <li class="dropdown dropdown-user">
          <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
             data-close-others="true">
            <%-- <img alt="" class="img-circle" src="${C_ASSETS_PATH}/admin/layout/img/avatar3_small.jpg"/> --%>
					<span class="username username-hide-on-mobile">
					${user_info.realname} </span>
            <i class="fa fa-angle-down"></i>
          </a>
          <ul class="dropdown-menu dropdown-menu-default">
            <!--<li>-->
            <!--<a href="extra_profile.html">-->
            <!--<i class="icon-user"></i> My Profile </a>-->
            <!--</li>-->
            <li>
              <a href="login.html?pAct=logoutDo">
                <i class="icon-key"></i> 退出 </a>
            </li>
          </ul>
        </li>
        <!-- END USER LOGIN DROPDOWN -->
        <!-- BEGIN QUICK SIDEBAR TOGGLER -->
        <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
        <li class="dropdown dropdown-quick-sidebar-toggler">
          <a href="login.html?pAct=logoutDo" class="dropdown-toggle">
            <i class="icon-logout"></i>
          </a>
        </li>
        <!-- END QUICK SIDEBAR TOGGLER -->
      </ul>
    </div>
    <!-- END TOP NAVIGATION MENU -->
  </div>
  <!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
  <!-- BEGIN SIDEBAR -->
  <div class="page-sidebar-wrapper">
    <div class="page-sidebar navbar-collapse collapse">
      <!-- BEGIN SIDEBAR MENU -->
      <ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
        <li class="sidebar-toggler-wrapper">
          <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
          <div class="sidebar-toggler">
          </div>
          <!-- END SIDEBAR TOGGLER BUTTON -->
        </li>
        <li class="heading">
          <h3 class="uppercase"></h3>
        </li>
        
        <li class="<c:if test="${cur_page == 'personal_account.html' || cur_page == 'enterprise_account.html'}">active</c:if>">
		  <a href="javascript:;">
			<i class="icon-users"></i>
			<span class="title">用户管理</span>
			<span class="arrow open"></span>
		  </a>
		  <ul class="sub-menu">
			<li class="<c:if test="${cur_page == 'personal_account.html'}">active</c:if>">
			  <a href="personal_account.html">
				<i class="icon-user"></i>个人账号
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'enterprise_account.html'}">active</c:if>">
			  <a href="enterprise_account.html">
				<i class="icon-flag"></i>企业账号
			  </a>
			</li>
		  </ul>
		</li>
		<li class="<c:if test="${cur_page == 'product.html'}">active</c:if>">
		  <a href="product.html">
			<i class="icon-handbag"></i>
			<span class="title">产品管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'item.html'}">active</c:if>">
		  <a href="item.html">
			<i class="icon-wallet"></i>
			<span class="title">项目管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'service'}">active</c:if>">
		  <a href="service.html">
			<i class="icon-tag"></i>
			<span class="title">服务管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'carousel.html'}">active</c:if>">
		  <a href="carousel.html">
			<i class="icon-rocket"></i>
			<span class="title">轮播图管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'video.html'}">active</c:if>">
		  <a href="video.html">
			<i class="icon-puzzle"></i>
			<span class="title">视频管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'hots.html'}">active</c:if>">
		  <a href="hots.html">
			<i class="icon-globe"></i>
			<span class="title">热点管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'estimate.html'}">active</c:if>">
		  <a href="estimate.html">
			<i class="icon-speech"></i>
			<span class="title">评价管理</span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'error.html'}">active</c:if>">
		  <a href="error.html">
			<i class="icon-eye"></i>
			<span class="title">纠错管理 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'link_statis.html' || cur_page == 'item_statis.html' || cur_page == 'etc_statis.html' || cur_page == 'buy_statis.html' || cur_page == 'request_statis.html'}">active</c:if>">
		  <a href="javascript:;">
			<i class="icon-bar-chart"></i>
			<span class="title">数据统计</span>
			<span class="arrow"></span>
		  </a>
		  <ul class="sub-menu">
			<li class="<c:if test="${cur_page == 'link_statis.html'}">active</c:if>">
			  <a href="link_statis.html">
				<i class="icon-user"></i>联系我
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'item_statis.html'}">active</c:if>">
			  <a href="item_statis.html">
				<i class="icon-link"></i>产品/项目分享
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'etc_statis.html'}">active</c:if>">
			  <a href="etc_statis.html">
				<i class="icon-bell"></i>其他分享
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'buy_statis.html'}">active</c:if>">
			  <a href="buy_statis.html">
				<i class="icon-basket"></i>立即购买
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'request_statis.html'}">active</c:if>">
			  <a href="request_statis.html">
				<i class="icon-user-following"></i>邀请好友
			  </a>
			</li>
			</ul>
		</li>
		<li class="<c:if test="${cur_page == 'fenlei.html' || cur_page == 'pleixing.html' || cur_page == 'xyleixing.html'}">active</c:if>">
		  <a href="javascript:;">
			<i class="icon-bar-chart"></i>
			<span class="title">配置管理</span>
			<span class="arrow"></span>
		  </a>
		  <ul class="sub-menu">
			<li class="<c:if test="${cur_page == 'fenlei.html'}">active</c:if>">
			  <a href="fenlei.html">
				<i class="icon-user"></i>分类管理
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'pleixing.html'}">active</c:if>">
			  <a href="pleixing.html">
				<i class="icon-link"></i>产品类型
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'xyleixing.html'}">active</c:if>">
			  <a href="xyleixing.html">
				<i class="icon-bell"></i>行业类型
			  </a>
			</li>
		  </ul>
		</li>
		<li class="<c:if test="${cur_page == 'opinion.html'}">active</c:if>">
		  <a href="opinion.html">
			<i class="icon-eye"></i>
			<span class="title">意见反馈 </span>
		  </a>
		</li>
		<li class="<c:if test="${cur_page == 'user.html' || cur_page == 'role.html'}">active</c:if>">
		  <a href="javascript:;">
			<i class="icon-bar-chart"></i>
			<span class="title">系统管理</span>
			<span class="arrow"></span>
		  </a>
		  <ul class="sub-menu">
			<li class="<c:if test="${cur_page == 'user.html'}">active</c:if>">
			  <a href="user.html">
				<i class="icon-user"></i>人员管理
			  </a>
			</li>
			<li class="<c:if test="${cur_page == 'role.html'}">active</c:if>">
			  <a href="role.html">
				<i class="icon-link"></i>角色管理
			  </a>
			</li>
		  </ul>
		</li>
		<li>
		  <a href="profile.html">
			<i class="icon-eye"></i>
			<span class="title">修改密码 </span>
		  </a>
		</li>
      </ul>
      <!-- END SIDEBAR MENU -->
    </div>
  </div>
  <!-- END SIDEBAR -->
  <div class="page-content-wrapper">
    <div class="page-content">
      <!-- BEGIN PAGE HEADER-->
      <h3 class="page-title">
        ${title[0]}
        <small>${title[1]}</small>
      </h3>
      <div class="page-bar">
        <ul class="page-breadcrumb">
          <li>
            <i class="fa fa-home"></i>
            <a href="">首页</a>
          </li>
          <c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="i">
          	<li>
              <i class="fa fa-angle-right"></i>
              <a href="${breadcrumb.value}">${breadcrumb.key}</a>
            </li>
          </c:forEach>
        </ul>
      </div>
      <!-- END PAGE HEADER-->
      <div id="content-div" class="row">
