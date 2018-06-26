<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0);
	response.setHeader("Pragma", "no-cache");
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>收银台</title>
	<link href="<%=basePath%>source/default/js/qrcode/css1/reset.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>source/default/js/qrcode/css1/checkstand.css" rel="stylesheet" type="text/css" />
</head>
<body>
<!--头部-->
<div class="header clearfix">
	<div class="w1024">
		<div class="logo fl"><a href="#">Alwaypay</a></div>
		<div class="nav fl"><span>收银台</span></div>
	</div>
</div>
	<!--主体-->
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
			<!--选择支付-->
			<form id="reform" method="post" action="">
				<div class="pay-select clearfix">
					<div class="s-wrap">
						<h2>选择您的支付方式</h2>
						<div class="s-list clearfix">
							<ul id="slist">
								<c:if test="${code=='0x0000'}">
									<c:forEach items="${channelList}" var="channel">
										<li>
											<label>
												<input type="radio" id="channel_${channel.gatewayid}" name="payway" value="${channel.gatewayid}" >
												<span class="radioIcon"></span>
												<input type="hidden" id="url_${channel.gatewayid}" name="gatewayURL" value="${channel.url}"/>
												<span class="pay_label">
													<span class="imgicon"><img src="${channel.imagepath}" /></span>
													<span class="payname">${channel.cashiername}</span>
												</span>
											</label>
										</li>
									</c:forEach>
								</c:if>
								<c:if test="${code!='0x0000'}">
									<p style="font-size: 19px; font-weight: bold">${msg}</p>
								</c:if>
								<input type="hidden" name="gateway_id" id="gatewayId" value="" />
				<%-- 				<c:forEach items="${paramMap}" var="map">
									<input type="hidden" name="${map.key}" value="${map.value}"/><br/>
								</c:forEach> --%>
								<input type="hidden" name="encryptData" value='${encryptData}'/><br/>
							</ul>
						</div>
						<div class="s-btn">
							<button type="button" class="payBtn button disabled" id="payBtn">
								<span>立即支付</span>
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<jsp:include   page="/bottom.jsp" flush="true"/>
	<script type="text/javascript" src="<%=basePath%>source/default/js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript">
		var paymentPage = {
				initEvent : function(){
					//激活支付按钮
					$("#slist li label").on("click",function(){
						$("#payBtn").removeClass("disabled");
					});
					$("#payBtn").on("click",function(){
						$("#payBtn").removeClass("disabled");
						var gatewayId = $("#slist input[name='payway']:checked").val();
						if(gatewayId == null || gatewayId == ""){
							alert('请选择您的支付方式!');
							return;
						}
						$('#gatewayId').val(gatewayId);
						var url = $("#url_"+gatewayId).val();
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
		});
	</script>
</body>
</html>
