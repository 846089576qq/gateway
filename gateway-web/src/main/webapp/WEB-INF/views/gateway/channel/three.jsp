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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link href="<%=basePath%>source/default/js/qrcode/css1/reset.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>source/default/js/qrcode/css1/checkstand.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/cashier/pc.js"></script>
	<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/pay.js"></script>
  </head>
  <body>
	<form id="param">
        <input type="hidden" id ="encryptData" name="encryptData" value="${encryptData}"/><br/>
	</form>
	<script>
    var basepath = '<%=basePath %>';
    </script>
	<script>
$(function(){
	qrcode.dataInit(basepath,"");
	qrcode.createPass();
})
	</script>
	
	<div class="backdrop" style="display:none " id="feedback">
    <div class="prompt-wrap">
        <div class="promptImage">
            <p><img id="imgState" src="" alt=""/><span id="disStatus"></span></p>
            <p><span id="disTxt1"></span></p>
        </div>
        <div class="promptTxt">
            <p>通道反馈：<span id="disTxt2"></span></p>
        </div>
    </div>
</div>
  </body>
</html>
