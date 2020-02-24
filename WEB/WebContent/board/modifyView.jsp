<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/layout/header.jsp">
	<jsp:param name = "title" value = "Page"/>
</jsp:include>

<form action = "modifyAction.brd?num=${vo.num }" method = "post">
	
	<table border = "1" style = "width:80%;">
		<tr>
			<th style="width:20%">제목</th>
			<td>
				<input type = "text" name = "title" style = "width:95%" 
						 value="${ vo.title}" required>
			</td>
		</tr>
		
		<tr>
			<td colspan = "2">
				<textarea style="width:100%; height:400px" name="content"
						required>${ vo.content}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan = "2">
				<input type = "submit" value = "수정!">
			</th>
		</tr>
		
	</table>
</form>

<jsp:include page="/layout/footer.jsp"/>


