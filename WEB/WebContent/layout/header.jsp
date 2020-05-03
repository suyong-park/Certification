<%@page import="com.certification.beans.AccountDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${param.title }</title>

<link rel="stylesheet" href="/certification/resources/main_style.css"/>

</head>
<body>
<!-- 
	로그인 했을 경우 : [닉네임]  LOGOUT  |  MYPAGE  |  BOARD  |  DOWNLOADS
		안했을 경우: LOGIN | JOIN | BOARD | DOWNLOADS
 -->
<div id = "header">

	<!-- 로그인 여부 상관없음 -->
	<a href="/certification/board/list.brd">BOARD</a>
</div>
<div id = "main">







