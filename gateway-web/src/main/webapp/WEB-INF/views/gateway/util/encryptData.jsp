<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <title>管理平台 | Alwaypay一站式支付聚合平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<%--<meta http-equiv="Content-Type" content="text/html; charset=gbk" />  --%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
	 	//提交
	 	function submit_ok(){
	 		document.getElementById("myinfo").submit();
	 	}
	 
	 </script>

  </head>
  <!-- style="display:none;" onload="submit_ok();" -->
  <body style="display:none;" onload="submit_ok();">
	<form id="myinfo" name="form2" action="cashier/${type}" method="post">${encryptData}
		<input type="text" name="encryptData" value='<c:out value="${encryptData}"></c:out>'/>
	    <input type="submit" value="确认付款，到融支付支付去啦"/>
	 </form>
  </body>
</html>
