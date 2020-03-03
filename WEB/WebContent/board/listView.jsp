<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/layout/header.jsp">
	<jsp:param name = "title" value = "Page"/>
</jsp:include>

<%-- 로그인 상태라면 '글쓰기' 버튼 노출 --%>
<c:if test = "${sessionScope.num != null }">
	<button onclick="location.href='/certification/board/write.brd'">글쓰기</button>
</c:if>

<%-- 글목록  --%>
<table border = "1" style="width:80%">
	<tr>
		<th>글번호</th>
		<th style="width:55%">제목</th>
		<th>설명</th>
		<th>회사</th>
		<th>직업</th>
		<th>링크</th>		
	</tr>
	
	<c:forEach var = "vo" items="${requestScope.list }">
		<tr>
			<td>${vo.num }</td>
			<td>${vo.name }</td>
			<td>${vo.description }</td>
			<td>${vo.company }</td>
			<td>${vo.job }</td>
			<td>${vo.link }</td>
		</tr>
	</c:forEach>
</table>
<!-- 페이지 숫자 띄우기 -->
<c:set var ="page" value="${param.page != null ? param.page : 1}"/>

<c:if test = "${page > 3 }">
	<a href="list.brd?page=${page-3 }">[이전]</a>
</c:if>
<c:if test = "${page > 2 }">
	<a href="list.brd?page=${page-2 }">[${page-2 }]</a>
</c:if>

<c:if test = "${page > 1 }">
	<a href="list.brd?page=${page-1 }">[${page-1 }]</a>
</c:if>
${page }

<c:if test = "${page < lastPage }">
	<a href="list.brd?page=${page+1 }">[${page+1 }]</a>
</c:if>

<c:if test = "${page+1 < lastPage }">
	<a href="list.brd?page=${page+2 }">[${page+2 }]</a>
</c:if>

<c:if test = "${page+2 < lastPage }">
	<a href="list.brd?page=${page+3 }">[다음]</a>
</c:if>

<jsp:include page="/layout/footer.jsp"/>







