<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/layout/header.jsp">
	<jsp:param name = "title" value = "Page"/>
</jsp:include>

<c:set var = "message">
	<c:choose>
		<c:when test = "${status == 'write' }">글쓰기</c:when>
		<c:when test = "${status == 'modify' }">글수정</c:when>
		<c:when test = "${status == 'delete' }">글삭제</c:when>
	</c:choose>
</c:set>
	

<script>
	alert("${message}${result ? '(을)를 완료하였습니다.' : '에 실패하였습니다.'}");
	location.href="/certification/board/list.brd";
</script>


<jsp:include page="/layout/footer.jsp"/>


