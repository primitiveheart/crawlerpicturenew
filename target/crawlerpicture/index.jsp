<%@ page language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
</head>
<body>
<script language="JavaScript">
    window.location.replace("login.html");
</script>
</body>
</html>
