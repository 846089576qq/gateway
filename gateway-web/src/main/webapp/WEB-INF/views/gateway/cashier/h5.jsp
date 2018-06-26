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
    <title>收银台</title>
    <link rel="stylesheet" href="<%=basePath%>source/default/css/reset.css">
    <link rel="stylesheet" href="<%=basePath%>source/default/css/checkstand_mobile.css">
</head>
<body>
    <c:if test="${code == '0x0000'}">
        <form id="reform" method="post" action="">
            <div class="wrap clearfix">
                <div class="paytime">
                    <p>支付剩余时间</p>
                    <p class="time"><span id="m"></span>:<span id="s"></span></p>
                    <p >请在<span class="yellow">30分钟</span>内付清款项，否则订单会被自动取消</p>
                </div>
                <div class="payAmount">
                    <span class="tit">应付金额</span>
                    <span id="amount">￥${order.totalprice}</span>
                </div>
                <div class="payselect clearfix">
                    <div class="paytitle"><p>选择你的支付方式</p></div>
                    <ul>
                        <%-- //微信公众号收银台展示取决于浏览器是不是微信 //TODO --%>
                        <c:forEach items="${channelList}" var="channel">
                            <li data-id="${channel.gatewayid}" data-url="${channel.url}">
                                <span class="payicon"><img src="${channel.imagepath}" width="50" height="50"/></span>
                                <span class="paytxt">${channel.cashiername}</span>
                                <span class="paycheck"></span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <button type="button" class="button" id="subBtn">去支付</button>
            </div>
            <input type="hidden" name="gateway_id" id="gatewayId" value="" />
            <input type="hidden" name="encryptData" value='${encryptData}'/><br/>
        </form>
    </c:if>
    <c:if test="${code != '0x0000'}">
        <div class="wrap">
            <div class="pay-status">
                <img src="<%=basePath%>source/default/images/mobile/pay_fail.png" width="130" height="152"/>
            </div>
            <div class="pay-status-txt">${msg}</div>
            <div class="back-page">
                <a class="button backBtn" href="javascript:;" onclick="history.back(); ">返回</a>
            </div>
        </div>
    </c:if>

<script src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/fastclick.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/common.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/checkstand_mobile.js"></script>
<script src="<%=basePath%>source/default/js/h5sdk/utils/jweixin-1.0.0.js" type="text/javascript" charset="UTF-8" ></script>
<script>
    var paymentPage = {
        initEvent : function(){
            //默认第一项被选中
            $(".payselect li").eq(0).addClass("selected");
            //选择支付方式
            $(".payselect li").on("click",function(){
                $(this).addClass("selected").siblings().removeClass("selected");
            });
            $("#subBtn").on("click",function(){
                $(this).addClass('dis').attr("disabled", true).html('正在支付...');
                var gatewayId = $(".selected").attr("data-id");//获取选中的支付方式
                if(gatewayId == null || gatewayId == ""){
                    alert('请选择您的支付方式!');
                    return;
                }
                $('#gatewayId').val(gatewayId);
                var url = $(".selected").attr("data-url");
                paymentPage.forwardPayment(url);
            });
        },
        forwardPayment : function(_url){
            $("#reform").attr("action", "<%=basePath%>cashier/channel/" + _url);
            $("#reform").submit();
        }
    };

    $(document).ready(function() {
        paymentPage.initEvent();
        //固定页面不可下拉
        $('body').height($(window).height());
        $('body').on('touchmove', function (event) {
            event.preventDefault();
        });
    });
</script>
</body>
</html>