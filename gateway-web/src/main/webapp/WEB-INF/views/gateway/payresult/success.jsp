<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>付款成功</title>
		<link href="<%=basePath%>source/default/js/qrcode/css1/reset.css" rel="stylesheet" type="text/css"/>
		<link href="<%=basePath %>source/default/js/qrcode/css1/swiper.min.css" rel="stylesheet" type="text/css"/>
		<link href="<%=basePath%>source/default/js/qrcode/css1/new_checkstand.css" rel="stylesheet" type="text/css"/>
		<style>
			.remind {background-color:#fff6f0;padding:10px;font-size:14px;color:#333;}
			.remind h4 {line-height:18px;}
			.remind h4 img {margin-right:5px;}
			.remind p {text-align:justify;padding-top:5px;}
			.recommend h4 {padding-top:35px;text-align:center;font-size:20px;color:#00b4ff;font-weight:400;}
			.recommend h4 span {display:inline-block;height:1px;width:150px;margin:0 20px;background-color:#f2f2f2;vertical-align: middle;}
			.recommend p {padding-bottom:40px;text-align:center;line-height:40px;font-size:14px;color:#666;}
			.swiper-slide a {display:block;text-align: center;}
			.swiper-slide a img {width:310px;}
			.swiper-button-next {background-image:url(<%=basePath %>source/default/js/qrcode/images1/next_icon.png?v=20171030);background-size:12px;right:0;background-position-x:14px;}
			.swiper-button-prev {background-image:url(<%=basePath %>source/default/js/qrcode/images1/prev_icon.png?v=20171030);background-size:12px;left:0;background-position-x:0;}
		</style>
	</head>
	<body>
		<!--头部-->
		<div class="header clearfix">
			<div class="w1024">
				<div class="nav fl">
					<span><!-- Alwaypay收银台 --></span>
				</div>
			</div>
		</div>
		<!--主体-->
		<div class="main clearfix">
			<div class="hint-wrap w1024 clearfix">
				<p class="htitle">
					<!-- 以下支付方式由Alwaypay提供 -->
				</p>
				<div class="hint">
					<div class="h-text">
						<img src="<%=basePath %>source/default/js/qrcode/images1/checkstand_success.jpg" width="37" height="37" alt="支付成功" />
							恭喜！支付成功！
					</div>
					<div class="h-btn">
						<a href="${order.returnurl}" class="backBtn" onclick="submit_ok">立即跳转</a>
					</div>
				</div>
			</div>
			<div class="loans w1024 clearfix">
				<div class="remindbox">
					<div class="remind">
						<h4><img src="<%=basePath %>source/default/js/qrcode/images1/icon.png" alt="">安全提醒</h4>
						<p>所有产品均有第三方提供，点击“去申请”将去往第三方平台官网，申请该产品前，请您审慎阅读该产品的用户协议及相关授权协议，确保个人隐私和数据安全</p>
					</div>
				</div>
				<%-- <div class="recommend">
					<h4><span></span>为你推荐<span></span></h4>
					<p>超过半数用户选择以下优质贷款</p>
				</div>
				<div class="swiper-container clearfix" style="max-height:180px">
    				<div class="swiper-wrapper">
        				<div class="swiper-slide">
							<a target="_blank" href="https://onecard.9fbank.com/wkCubeNew/#/register?proId=rt4ab31a1221e7712b2b6b9cf3b5e132c10"><img src="<%=basePath %>source/default/js/qrcode/images1/loans_01.png" alt="玖富万卡"></a>
        				</div>
        				<div class="swiper-slide">
							<a target="_blank" href="https://cube.doraemoney.com/newCube/index.html?proId=a4d4be007e0e7dfd833ecae846ef336a"><img src="<%=basePath %>source/default/js/qrcode/images1/loans_02.png" alt="玖富叮当"></a>
        				</div>
    				</div>  
    				<!-- 如果需要导航按钮 -->
    				<div class="swiper-button-prev"></div>
    				<div class="swiper-button-next"></div>
				</div> --%>
			</div>
		</div>
		<jsp:include page="/bottom.jsp" flush="true"/>
		<link href="<%=basePath %>source/default/js/qrcode/css1/checkstand_media_phone.css" rel="stylesheet" type="text/css" />
		<script src="<%=basePath %>source/default/js/jquery-1.11.0.min.js"></script>
		<script src="<%=basePath %>source/default/js/qrcode/cashier/swiper-3.4.2.jquery.min.js"></script>
		<script>
			// var oLeave = document.getElementById("leave");
			//提交
			function submit_ok() {
				document.getElementById("myinfo").submit();
			}
			function browserRedirect() {
				// 您的浏览设备
			    var sUserAgent = navigator.userAgent.toLowerCase();
			    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
			    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
			    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
			    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
			    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
			    var bIsAndroid = sUserAgent.match(/android/i) == "android";
			    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
			    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
			    if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
			        return ("phone");
			    } else {
			        return  ("pc");
			    }
			}
			
			$(function(){
				// 设置容器高度
				var imgHeight = $('.swiper-slide a img').height();
				// console.log('imgHeight',imgHeight)
				$('.swiper-container').css({
					maxHeight:imgHeight
				})
				
				// 判断浏览设备
				var equipment = browserRedirect();
				console.log("您的浏览设备为："+browserRedirect());

				if(equipment == 'phone'){
				    // 移动设备则将所有图片放到partone中
				    $('.parttwo li').appendTo($('.partone'));
				    $('.parttwo').hide();
				}
				
				// Swiper触摸滑动
				var mySwiper = new Swiper ('.swiper-container', {
  					slidesPerView: equipment == 'phone' ? 1 : 3, //显示个数
				    autoplay : 3000,
  			  		loop: true,
  			  		roundLengths:true,//计算尺寸取整
  			  		// 如果需要前进后退按钮
  			  		nextButton: '.swiper-button-next',
  			  		prevButton: '.swiper-button-prev',
  				})   
			});
			
		</script>
	</body>
</html>