<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<html lang="ch">
<head>
<meta charset="UTF-8">
<title>收银台</title>
	<link href="<%=basePath%>source/wg/css/reset.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>source/wg/css/checkstand.css" rel="stylesheet" type="text/css" />
<%-- 	<link href="<%=basePath%>source/js/twoma/css1/reset.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>source/js/twoma/css1/checkstand2.css" rel="stylesheet" type="text/css" />
 --%></head>
<body>
	<!--头部-->
	<div class="header clearfix" <c:if test="${is_show_zitopay_icon == '1' }">style="border-bottom: 0px; box-shadow: 0 0px 0px;"</c:if>>
	<c:if test="${is_show_zitopay_icon == '1' }">
		<div class="w1024">
			<div class="nav fl">
				<span>Alwaypay收银台</span>
			</div>
		</div>
	</c:if>
	</div>
	<!--主体-->
	<div class="main clearfix">
		<div class="check-wrap w1024 clearfix">
			<!--订单详情-->
			<div class="check-detail">
				<div class="check-msg">
					<h1>订单提交成功，请您尽快付款！订单号：${order.orderidinf}</h1>
					<p>
						请您在<span class="time">30分钟</span>内付清款项，否则订单会被自动取消
					</p>
				</div>
				<div class="check-cash">
					应付金额<span class="amount">${order.totalprice}</span>元
				</div>
			</div>
			<!--选择支付-->
			<form id="reform" method="post" action="">
				<div class="pay-select clearfix">
					<div class="s-wrap">
						<h2>选择您的支付方式</h2>
						<div class="pay-main clearfix">
							<div class="pay-tabs">
	                            <ul>
	                                <li class="active"><a href="#">借记卡</a></li>
	                                <li><a href="#">贷记卡</a></li>
	                            </ul>
	                        </div>
	                        
							<c:choose>
								<c:when test="${order.state != 0}">
									<div id="slist0" class="s-list clearfix">
										<p id="errorMsg" style="font-size: 19px; font-weight: bold">该订单已请求，请重新发起请求</p>
									</div>
								</c:when>
								<c:otherwise>
									<div id="slist0" class="s-list clearfix">
										<p id="errorMsg" style="font-size: 19px; font-weight: bold"></p>
										<ul class="slist">
										</ul>
										<div class="viewmore"><img src="<%=basePath%>source/wg/images/viewmore.jpg" width="200" height="33" alt="展开更多银行"></div>
									</div>
									
									<div id="slist1" class="s-list clearfix" style="display: none">
				                        <ul class="slist">
				                        </ul>
				                        <div class="viewmore"><img src="<%=basePath%>source/wg/images/viewmore.jpg" width="200" height="33" alt="展开更多银行"></div>
				                    </div>
								</c:otherwise>
							</c:choose>
							<input type="hidden" name="gateway_id" id="gatewayId" value="${param.gateway_id}" />
							<input type="hidden" name="encryptData" value='<%=URLEncoder.encode(request.getParameter("encryptData"), "UTF-8") %>'/><br/>
							<input type="hidden" name="orderId" value="${order.orderid}" />
							<input type="hidden" name="bank_code" id="bankCode" value="" />
							<input type="hidden" name="card_type" id="cardType" value="1" />
						</div>
						<div class="s-btn">
							<button type="button" class="payBtn button disabled" id="payBtn" disabled="true">
								<span>立即支付</span>
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<c:if test="${is_show_zitopay_icon == '1' }"><jsp:include   page="/bottom.jsp" flush="true"/></c:if>
	<script type="text/javascript" src="<%=basePath%>source/default/js/jquery-1.11.0.min.js"></script>
	<script type="text/javascript">
	
	$.post("<%=basePath%>payment/json","${encryptData}",
		function(data){
		if(data.code=="0x0000"){//如果调用成功
			$.post("<%=basePath%>cashier/decrypt",data,
         			function(_data){
         		 		 if(_data.code == "0x0000"){
         		 			 var html = "";
         		 			 for(var debit in _data.data.debitMap) {
         		 				html += "<li>" +
	        						"<label>" +
	        							"<input type='radio' id='bank_" + debit + "' name='payway' value='" + debit + "' >" +
	        							"<span class='radioIcon'></span>" +
	        							"<span class='pay_label'>" +
	        								"<span class='imgicon'><img src='<%=basePath%>source/wg/images/logo/" + debit + ".jpg' alt='"+_data.data.debitMap[debit]+"' /></span>" +
	        							"</span>" +
	        						"</label>" +
	        					"</li>";
         		 			 }
        					$("#slist0 .slist").append(html);
        					
        					var html = "";
        		 			 for(var credit in _data.data.creditMap) {
        		 				html += "<li>" +
	        						"<label>" +
	        							"<input type='radio' id='bank_" + credit + "' name='payway' value='" + credit + "' >" +
	        							"<span class='radioIcon'></span>" +
	        							"<span class='pay_label'>" +
	        								"<span class='imgicon'><img src='<%=basePath%>source/wg/images/logo/" + credit + ".jpg'  alt='"+_data.data.debitMap[credit]+"' /></span>" +
	        							"</span>" +
	        						"</label>" +
	        					"</li>";
        		 			 }
       					$("#slist1 .slist").append(html);
       					paymentPage.initEvent();
         		 		}else{
         		 			$("#errorMsg").append(_data.msg);
         		        	$(".viewmore").hide();
         		 		}	
              		}
         	 , 'json');
        }else{
        	$("#errorMsg").append(data.msg);
        	$(".viewmore").hide();
        }
		}
 	 , 'json');
	
	
	$(".pay-tabs li").on("click",function () {
        var index = $(this).index();
        $("#cardType").val(index + 1);
        $(this).addClass('active').siblings().removeClass('active');
        $("#slist0,#slist1").hide();
        $("#slist"+index).show();
    });
	
	//展开更多银行
    $(".viewmore").on("click",function () {
        $(this).hide().prev('.slist').css("height","auto");
    })
    
//		//获取表单提交按钮
		var btnSubmit = document.getElementById("payBtn");
		var paymentPage = {
				initEvent : function(){
					//激活支付按钮
					$("#reform li label").on("click",function(){
						btnSubmit.disabled = false;
						$("#payBtn").removeClass("disabled");
					});
					$("#payBtn").on("click",function(){
						var bankCode = $("#reform input[name='payway']:checked").val();
						if(bankCode == null || bankCode == ""){
							alert('请选择您的支付银行!');
							return;
						}
						$('#bankCode').val(bankCode);
						$("#reform").attr("action", "<%=basePath%>cashier/channel/three");
						$("#reform").submit();
					});
				}
			}

	</script>
</body>
</html>
