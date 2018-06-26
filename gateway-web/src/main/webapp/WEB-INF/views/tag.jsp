<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%@taglib prefix="s" uri="/svm-tags" %>--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    request.getSession().setAttribute("path",  path);
    request.getSession().setAttribute("basePath",  basePath);
//	System.out.println(path+"---"+basePath);
%>
<link rel="shortcut icon" href="${basePath}source/common/imgs/favicon.ico" type="image/x-icon" />
