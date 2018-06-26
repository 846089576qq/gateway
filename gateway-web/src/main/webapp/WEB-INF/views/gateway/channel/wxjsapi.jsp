<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="<%=basePath%>source/default/css/reset.css">
    <link rel="stylesheet" href="<%=basePath%>source/default/css/checkstand_mobile.css">
    <style>
        /*===========支付确认页面==========*/
        .payee-show{height:2.1rem;background:#fff;text-align:center;line-height:1;border-bottom:1px solid #F2F2F2}
        .payee-show .p-name{font-size:0.26rem;color:#666666;padding-top:0.6rem}
        .payee-show .p-amount{font-size:0.5rem;color:#FF0600;padding-top:0.2rem}
        .payee-show .p-amount .unit{font-size:0.36rem}
        .payee{border-top:1px solid #FAFAFA;background:#fff;height:0.88rem;line-height:0.88rem;font-size:0.28rem;padding:0 0.2rem}
        .payee .tit{float:left;color:#999999}
        .payee #payeeName {display: inline-block;text-align: right;float: right;width: 60%;white-space: nowrap;overflow: hidden;text-overflow: ellipsis}
        .mbtns{width:7.1rem;margin:0 auto;padding:0 0.2rem;overflow:hidden}
        .mbtns .button{ background-color: #02bf02;}
    </style>
</head>
<body>
<div id="pay_view" style="display:none;">
    <div class="wrap clearfix">
        <div class="payee-show">
            <div class="p-name">${order.ordertitle}</div>
            <div class="p-amount"><span class="unit">￥</span><span class="num">${order.totalprice}</span></div>
        </div>
        <div class="payee">
            <span class="tit">收款方</span>
            <span id="payeeName">${personName}</span>
        </div>
        <div class="mbtns">
            <button type="button" class="button" id="payBtn">立即支付</button>
        </div>
    </div>
</div>
<div id="pay_msg"></div>

<form style="display: none;">
    <input type="hidden" id ="encryptData" name="encryptData" value="${encryptData}"/><br/>
</form>

<%--js--%>
<script src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/fastclick.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/common.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/jweixin-1.0.0.js" type="text/javascript" charset="UTF-8" ></script>


<script>
    var basepath = '<%=basePath %>';
    var returnUrl = '${order.returnurl}';// 返回商户地址

    //初始加载
    $(function(){
        var userAgent = navigator.userAgent.toLowerCase();
        if(userAgent.match(/MicroMessenger/i)=="micromessenger"){
            $("#pay_view").css("display","block");
            //调支付通道
            wxjsapi.createPass();
        }else{
            $("#pay_msg").html("<h6>请使用微信内置浏览器访问</h6>");
        }
        //固定页面不可滑动
        $('body').height($(window).height());
        $('body').on('touchmove', function (event) {
            event.preventDefault();
        });

        //点击支付
        $("#payBtn").click(function(){
            $(this).attr("disabled", true);
            wxjsapi.onBridgeReady($("#encryptData").val());
        });
    });

    var wxjsapi = {
        //进入通道
        createPass:function (){
            var encryptData=$("#encryptData").val();
            $.post(basepath+"payment/json",encryptData,function(_data){
                if(_data.code=="0x0000"){//如果调用成功
                    wxjsapi.sendPassencrypt(_data);
                }else{
                    $("#payBtn").attr("disabled", false);
                    alert("错误信息:"+_data.msg);
                }
            }, 'json');
        },
        //解析数据
        sendPassencrypt:function(encryptiondata){
            $.post(basepath+"cashier/decrypt",encryptiondata, function(_data){
                if(_data.code == "0x0000"){
                    $("#encryptData").val(_data.data.pay_data_wechat);
                    wxjsapi.onBridgeReady(_data.data.pay_data_wechat);
                }else{
                    $("#payBtn").attr("disabled", false);
                    alert("错误信息:"+_data.msg);
                }
            } , 'json');
        },
        //微信JSAPI调起支付
        onBridgeReady:function(data){
            WeixinJSBridge.invoke(
                    'getBrandWCPayRequest',//以下数据需替换为请求融支付得到的调起支付所需数据wxjsapiStr,注意分割符号和标点符号必须一致
                    JSON.parse(data),
                    function(res)
                    {
                        WeixinJSBridge.log(res.err_msg);
                        if(res.err_msg == "get_brand_wcpay_request:ok") {
                            alert("支付成功");
                            setTimeout(function () {
                                window.location.href = returnUrl;
                            }, 2000);
                        }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                            $("#payBtn").attr("disabled", false);
                        }else{
                            alert("支付失败!\n err_code:" + res.err_code + "\n err_desc:" + res.err_desc + "\n err_msg:" + res.err_msg);
                            $("#payBtn").attr("disabled", false);
                        }
                    }
            );
        }
    };
</script>
</body>
</html>