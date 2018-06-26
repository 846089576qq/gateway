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
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title>支付宝服务窗支付测试页面</title>
</head>
<body>

<div style="text-align:center;padding-top:50px;">
    <h5 style="text-align:left;">将请求融智付非收银台方法获取到的支付宝单号复制到下面文本框，并点击支付按钮。</h5>
    <h6 style="text-align:left;">示例值：2017122721001004940299447844<br/></h6>
    <textarea id="alipayChannelNo" rows="10" cols="40"></textarea>
    <div style="padding-top:10px;">
        <button id="subBtn" style="margin-top:100px;">支付</button>
    </div>
</div>
</body>
<script src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>source/test/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath %>source/default/js/h5sdk/utils/antbridge.min.js" charset="UTF-8" type="text/javascript" ></script>
<script type="text/javascript">

    // 点击支付
    $("#subBtn").click(function(){
        aliPay($("#alipayChannelNo").val());
    });

    function ready(callback) { // 由于js的载入是异步的，所以可以通过该方法，当AlipayJSBridgeReady事件发生后，再执行callback方法
        if (window.AlipayJSBridge) {
            callback && callback();
        } else {
            document.addEventListener('AlipayJSBridgeReady', callback, false);
        }
    }
    function aliPay(tradeNO) {
        ready(function(){
            AlipayJSBridge.call("tradePay", {
                tradeNO:tradeNO
            }, function (data) {
                if ("9000" == data.resultCode) {
                    alert("支付成功");
                }
            });
        });
    }

</script>
</html>
