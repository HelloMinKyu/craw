<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header"/>
<!--E:sub-vs-->

<script type="text/javascript">
    var message = '${message}';
    alert(message);
    document.location.href = '<c:url value="${returnUrl}"/>';
</script>