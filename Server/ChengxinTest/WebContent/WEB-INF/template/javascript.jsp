<%@page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" type="text/css" href="${C_ASSETS_PATH}/global/plugins/bootstrap-toastr/toastr.min.css" />

<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/jquery.min.js" ></script>
<script type="text/javascript" src="${C_ASSETS_PATH}/global/plugins/bootstrap-toastr/toastr.min.js" ></script>
<script language="Javascript">
jQuery(document).ready(function () {
	${SCRIPTS}
});

</script>

${INPUT_HIDDEN_ARRAY}
