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
<head>
<%@ include file="/WEB-INF/template/manage/layout/head.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<%@ include file="/WEB-INF/template/manage/layout/body_top.jsp" %>

<!-- BEGIN CONTENT -->
<div class="col-md-12">
	<div class="row">
	    <div class="col-md-6">
	        <div class="portlet box blue-hoki">
	            <div class="portlet-title">
	                <div class="caption">
	                    <i class="fa fa-list"></i>产品类型目录
	                </div>
	                <div class="tools">
	                    <a href="javascript:;" class="collapse"></a>
	                    <a href="javascript:;" class="reload" onclick="javascript:loadCategories();"></a>
	                </div>
	                <div class="actions">
	                    <a class="btn btn-default btn-sm" href="javascript:addCategory();"><i class="fa fa-plus"></i> 新增</a>
	                </div>
	            </div>
	            <div class="portlet-body">
	                <div id="tree_category" class="tree-demo scroller" style="height:450px;" data-rail-visible="1"></div>
	            </div>
	        </div>
	    </div>
	    <div id="action_category" class="col-md-6">
	    </div>
	</div>
</div>
<!-- END CONTENT -->

<%@ include file="/WEB-INF/template/manage/layout/body_bottom.jsp" %>
</body>
<!-- BEGIN PAGE LEVEL SCRIPT -->
<script>
function loadCategories() {
    $('#tree_category').jstree('refresh');
}

function addCategory() {
    $('#action_category').load("${cur_page}?pAct=edit");
}

function hideCategory() {
    $('#action_category').empty();
}

$(document).ready(function() {
    $('#tree_category').jstree({
        "core" : {
            "themes" : {
                "responsive": false
            },
            "data" : {
                "url" : function (node) {
                    return "${cur_page}?pAct=treeList";
                }
            }
        },
        "types" : {
            "default" : {
                "icon" : "fa fa-folder icon-state-warning"
            },
            "disuse" : {
                "icon" : "fa fa-folder icon-state-danger"
            }
        },
        "plugins": ["types"]
    });

    $('#tree_category').on('select_node.jstree', function(e, data) {
        $('#action_category').load('${cur_page}?pAct=edit&id=' + new String(data.selected).replace('cat_', ''));
    });
});

</script>
<!-- END PAGE LEVEL SCRIPT -->
<!-- END BODY -->
</html>
