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
    <title>微信公众号支付测试页面</title>
</head>
<body>

<div style="text-align:center;padding-top:50px;">
    <h5 style="text-align:left;">将请求融智付非收银台方法获取到的微信支付数据复制下面文本框，并点击支付按钮。</h5>
    <h6 style="text-align:left;">示例值：<br/>{"appId":"wx2421b1c4370ec43b","timeStamp":"1395712654","nonceStr":"e61463f8efa94090b1f366cccfbbb444","package":"prepay_id=u802345jgfjsdfgsdg888","signType":"MD5","paySign":"70EA570631E4BB79628FBCA90534C63FF7FADD89"}</h6>
    <textarea id="wxjsapiStr" rows="10" cols="40"></textarea>
    <div style="padding-top:10px;">
        <button id="subBtn" style="margin-top:100px;">支付</button>
    </div>
</div>
</body>
<script src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/jweixin-1.0.0.js" type="text/javascript" charset="UTF-8" ></script>
<script type="text/javascript">

    // 点击支付
    $("#subBtn").click(function(){
        onBridgeReady($("#wxjsapiStr").val());
    });

    function onBridgeReady(wxjsapiStr)
    {
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest',
                JSON.parse(wxjsapiStr),
                function(res)
                {
                    WeixinJSBridge.log(res.err_msg);
                    if(res.err_msg == "get_brand_wcpay_request:ok") {
                        alert("支付成功");
                    }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                        alert("用户取消支付!");
                    }else{
                        alert("支付失败!\n err_code:" + res.err_code + "\n err_desc:" + res.err_desc + "\n err_msg:" + res.err_msg);
                    }
                    // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                }
        );
    }

</script>
</html>
