<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>融智付</title>
    <script src="<%=basePath%>source/default/js/jquery-1.7.2.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://res.wx.qq.com/open/libs/weui/0.4.1/weui.css" />
    <script type="text/javascript" >
        $(function(){
            //固定页面不可滑动
            $('body').height($(window).height());
            $('body').on('touchmove', function (event) {
                event.preventDefault();
            });
        });
    </script>
</head>
<body>
<div class="weui_msg">
    <div class="weui_icon_area">
        <i class="weui_icon_info weui_icon_msg"></i>
    </div>
    <div class="weui_text_area">
        <h4 class="weui_msg_desc">code：${code}</h4>
        <h4 class="weui_msg_desc">msg：${msg}</h4>
        <h4 class="weui_msg_desc">desc：${description}</h4>
    </div>
</div>
</body>
</html>