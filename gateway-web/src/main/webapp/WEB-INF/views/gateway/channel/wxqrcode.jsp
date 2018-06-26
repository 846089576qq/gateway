<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>微信支付</title>
    <link href="<%=basePath%>source/default/js/qrcode/css1/reset.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>source/default/js/qrcode/css1/checkstand.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/erweima.js"></script>
<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/cashier/pc.js"></script>
<script type="text/javascript" src="<%=basePath%>source/default/js/qrcode/pay.js"></script>
<script>
    var basepath = '<%=basePath %>';
    var url = basepath+"cashier/payresult/${order.orderid}";
</script>
    <script>
        //初始加载
        $(function(){
        	qrcode.dataInit(basepath,url);//参数初始化
        	qrcode.createQrCodeMain();//调用微信二维码接口
        	qrcode.initEvent();//页面初始化
        })
    </script>


</head>
<body id="weixin_body_id" >
<form id="qrcode_form_id" style="display: none;">
        <input type="hidden" id ="encryptData" name="encryptData" value="${encryptData}"/><br/>
</form>

<div class="header clearfix">
    <div class="w1024">
        <div class="logo fl"><a href="#">融智付</a></div>
        <div class="nav fl"><span>收银台</span></div>
    </div>
</div>

<div class="main clearfix">
    <div class="check-wrap w1024 clearfix">
        <!--订单详情-->
        <div class="check-detail">
            <div class="check-msg">
                <h1>订单提交成功，请您尽快付款！订单号：${order.orderidinf}</h1>
                <p>请您在<span class="time">30分钟</span>内付清款项，否则订单会被自动取消</p>
            </div>
            <div class="check-cash">应付金额<span class="amount">${order.totalprice}</span>元</div>
        </div>
        <!--微信支付-->
        <div class="pay-select clearfix">
            <div class="pay-wrap">
                <div class="pay-left">
                    <h2>微信支付</h2>
                    <p class="txt" id="leaveTxt">过期后请重新获取二维码</p>
                    <div class="scanCode" id="qr_code">

                    </div>
                    <div class="usetext">
                        <p>请使用微信扫一扫</p>
                        <p>扫描二维码支付</p>
                    </div>
                </div>
                <div class="pay-right">
                    <img src="<%=basePath%>source/default/js/qrcode/images/pay_wechat.png" width="367" height="424" alt="微信支付"/>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="backdrop" style="display:none " id="feedback">
    <div class="prompt-wrap">
        <div class="promptImage">
            <p><img id="imgState" src="" alt=""/><span id="disStatus"></span></p>
            <p><span id="disTxt1"></span></p>
        </div>
        <div class="promptTxt">
            <p>通道反馈：<span id="disTxt2"></span></p>
        </div>
        <button id="confirmBtn" class="confirmBtn button">确认</button>
    </div>
</div>

    <jsp:include   page="/bottom.jsp" flush="true"/>
</body>
</html>