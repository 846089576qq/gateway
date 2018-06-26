<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<style>
    /*尾部*/
    .w1024{margin-left:auto;margin-right:auto;width:1024px}
    .footer{width:100%;min-height:300px;/*margin-top:38px;*/border-top:1px solid #F2F2F2;background:#fff}
    .footer-body .footer-body-tit{color:#666;font-size:17px;height:98px;line-height:98px}
    .footer-body .footer-body-image ul{overflow:hidden;margin-bottom:14px}
    .footer-body .footer-body-image ul li{float:left;height:38px}
    .footer-body .footer-body-image ul li + li{margin-left:47px}
    .footer-body .footer-copyright{position: relative;color: #999999;padding-top:28px;font-size: 15px;}
    .footer-body .footer-copyright .support{float: left;width:50%;}
    .footer-body .footer-copyright .copyright{float: left;width:50%;text-align: right}
</style>
<!--尾部-->
<div class="footer clearfix">
    <div class="footer-body w1024">
        <div class="footer-body-tit">合作伙伴</div>
        <div class="footer-body-image">
            <ul>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/1-wechat.png" alt="微信支付"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/2-alipay.png" alt="支付宝"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/3-jd.png" alt="京东"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/4-applePay.png" alt="applePay"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/5-msBank.png" alt="中国民生银行"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/6-baiduPay.png" alt="百度网关"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/7-PayPal.png" alt="PayPal"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/8-masterCard.png" alt="masterCard"/></li>
            </ul>
            <ul>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/9-visa.png" alt="VISA"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/10-fuyou.png" alt="富友集团"/></li>
                <li><img src="<%=basePath%>source/default/js/qrcode/images1/partner/11-ysb.png" alt="银生宝"/></li>
            </ul>
        </div>
        <div class="footer-copyright">
            <p class="support"><!-- Alwaypay提供技术支持 --></p>
            <p class="copyright"><!-- copyright&copy;Alwaypay  --><span><!-- 版权所有 --></span>    <span></span></p>
        </div>
    </div>
</div>
<script>
    //百度统计
    /* var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?29ebc52cae05940a10692dbe7e00801b";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })(); */
</script>