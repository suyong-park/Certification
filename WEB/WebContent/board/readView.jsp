<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/layout/header.jsp">
	<jsp:param name = "title" value = "Read-${vo.title }"/>
</jsp:include>

<c:set var = "nickname">${ AccountDao.getInstance().select( sessionScope.num ).nickname }</c:set>

<div align = "center" style="width:650px; min-height: 400px;">
	<c:if test = "${vo.writerNum == sessionScope.num }">
		<div align = "right">
			<button onclick="location.href='delete.brd?num=${vo.num}'">삭제</button>
			<button onclick="location.href='modify.brd?num=${vo.num}'">수정</button>
		</div>
	</c:if>
	
	<table border = "1">
		<tr>
			<th>제목</th>
			<td colspan = "3">${vo.title }</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${vo.writer }</td>
			<th>조회수</th>
			<td>${vo.hit }</td>
		</tr>
		<tr>
			<th>작성시간</th>
			<td>${vo.dateStr }</td>
		</tr>
		<tr>
			<td colspan="4" style="min-height: 100%">
			${vo.content }
			</td>
		</tr>
		<tr>
			<td colspan = "4">
				<button onclick = "location.href='list.brd'">뒤로</button>
			</td>
		</tr>
	</table>
	
	<p>댓글리스트</p>
	<c:if test = "${replyList != null }">
		<script>
			function showForm(name) {
				  var x = document.getElementById(name);
				  if (x.style.display === "none") {
				    x.style.display = "block";
				  } else {
				    x.style.display = "none";
				  }
			}		
		</script>
		<c:forEach var = "reVo" items = "${replyList }">
		<div style = "margin-left: ${reVo.depth * 50 }px;width:600px;border:1px solid black;text-align:left">
			ㄴ>${reVo.writer } : ${reVo.content } (${reVo.regdate }) 
			<c:if test = "${nickname != null}"> <!-- 로그인 중이면 답글 달기 가능 -->
				<input type = "button" onclick = "showForm('myForm_${reVo.num }')" value = "답글달기" class = "button" style = "width:100px">
				<div id = "myForm_${reVo.num }" style="display: None;">
					<form action = "writeReply.brd" method = "post">
						<table style = 'width:600px'>
							<tr style="height:100px">
								<th>${nickname }</th>
								<td><textarea style="width:100%;height:100px" name = "content" placeholder="악플 ㄴㄴ" required="required"></textarea>
								<td><input type = "submit" value = "저장" style = "width:100%;"></td>
							</tr>
						</table>
						<!-- 이 댓글이 부모 댓글이 될 것임 -->
						<input type = "hidden" name = "parentNum" value = "${ reVo.num }">
						<!-- 이 댓글의 깊이 (현재 댓글의 대댓글이므로 깊이 +1-->
						<input type = "hidden" name = "depth" value = "${reVo.depth + 1 }">
						<!-- 이 댓글의 게시글 번호 -->
						<input type = "hidden" name = "boardNum" value = "${reVo.boardNum }">
						
					</form>
				</div>
			</c:if>
		</div>
		</c:forEach>	
		
	</c:if>
	<form action = "/myhome/board/writeReply.brd" method = "post">
		<!-- 부모 댓글은 없음 -->	
		<input type = "hidden" name = "parentNum" value = "0">
		<!-- 이 댓글의 깊이 없음-->
		<input type = "hidden" name = "depth" value = "0">
		<!-- 이 댓글의 게시글 번호 -->
		<input type = "hidden" name = "boardNum" value = "${vo.num }">
		<table style = 'width:600px'>
			<tr style="height:100px">
				<th>${nickname }</th>
				<td><textarea style="width:100%;height:100px" name = "content" placeholder="악플 ㄴㄴ" required="required"></textarea>
				<td><input type = "submit" value = "저장" style = "width:100%;"></td>
			</tr>
		</table>			
	</form>
</div>

<jsp:include page="/layout/footer.jsp"/>


