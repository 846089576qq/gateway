<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>ZitoPay Payment Test Page</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>source/test/css/style-bootstrap.css">
</head>
<body>
<div class="bootstrap-frm">
	<label>
		<span>Location :</span>
		<input id="Payment Location" type="text" name="paymentEnv" placeholder="the payment environment"
			   value="http://58.247.43.134:8087/gateway-web/cashier/pc/encryptData"/>
	</label>
	<form id="payForm" action="" method="post">
		<h1>
			Zito Payment-2.0 Test<span>Please fill all the texts in the fields.</span>
		</h1>
		<label> <span>mer_id :</span> <input id="mer_id" type="text" name="mer_id" placeholder="mer_id" /></label>
		<label> <span>mer_app_id :</span> <input id="mer_app_id" type="text" name="mer_app_id" placeholder="mer_app_id" /></label>
		<label> <span>gateway_id :</span> <input id="gateway_id" type="text" name="gateway_id" placeholder="gateway_id"/></label>
		<label> <span>oper_type :</span> <input id="oper_type" type="text" name="oper_type" placeholder="oper_type"/></label>
		<label> <span>order_title :</span> <input id="order_title" type="text" name="order_title" placeholder="order_title"/></label>
		<label> <span>order_time :</span> <input id="order_time" type="text" name="order_time" placeholder="order_time"/></label>
		<label> <span>order_amt :</span> <textarea id="order_amt" name="order_amt" placeholder="order_amt"></textarea></label>
		<label> <span>mer_order_id :</span> <input id="mer_order_id" type="text" name="mer_order_id" placeholder="mer_order_id"/></label>
		<label> <span>mer_async_notify :</span> <input id="mer_async_notify" type="text" name="mer_async_notify" placeholder="mer_async_notify"/></label>
		<label> <span>mer_url :</span> <input id="mer_url" type="text" name="mer_url" placeholder="mer_url"/></label>
		<label> <span>open_id :</span> <input id="open_id" type="text" name="open_id" placeholder="open_id"/></label>
		<input id="sign" type="hidden" name="sign" style="width: 0px; height: 0px;"/>

		<label> <span>&nbsp;</span> <input name="pay" type="button" class="button" value="pay" />
		</label>
	</form>
</div>

</body>
<script type="text/javascript" src="<%=basePath%>source/test/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>source/test/js/md5.js"></script>
<script type="text/javascript" src="<%=basePath%>source/test/js/payment.js"></script>
<script type="text/javascript">
	var payment = {
		initValue : function() {
			$("[name=mer_id]").val('RZF958857');
			$("[name=mer_app_id]").val('RZF958386');
			$("[name=gateway_id]").val('103');
			$("[name=oper_type]").val('pay');
			$("[name=order_title]").val('QQ钱包扫码');
			$("[name=order_time]").val('20170616155301026');
			$("[name=order_amt]").val(0.01);
			$("[name=mer_order_id]").val('T'+new Date().format('yyyyMMddHHmmss'));
			$("[name=mer_async_notify]").val('https://www.baidu.com');
			$("[name=mer_url]").val('https://www.baidu.com/');
			$("[name=open_id]").val('');
			$("#payForm").attr("action", $("[name=paymentEnv]").val());
		},
		initEvent : function() {
			$("[name=pay]").click(
					function() {
						$("#payForm").attr("action",$("[name=paymentEnv]").val());
						$("#payForm").submit();
					});
		}
	}
	$(document).ready(function() {
		payment.initValue();
		payment.initEvent();
	});
</script>
</html>
