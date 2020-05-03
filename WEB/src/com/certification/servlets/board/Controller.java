package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.brd")
// /myhome/~~~~~~~~.brd 로 끝나는 모든 요청

/*
 * < 게시판 >
 * 1. 글쓰기 
 *  /myhome/board/write.brd
 * 
 * 2. 글목록보기 
 *  /myhome/board/list.brd
 * 
 * 3. 글삭제
 *  /myhome/board/delete.brd?num=삭제할글번호
 * 
 * 4. 글수정
 *  /myhome/board/modify.brd?num=수정할글번호
 *  
 *  
 * 5. 본문보기
 *  /myhome/board/read.brd?num=읽을글번호
 * 
 */




public class Controller extends HttpServlet{
	
	// service : post, get 등의 모든 요청 상관없이 실행
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("컨트롤러의 service() 실행!!");
		
		String uri = ((HttpServletRequest)request).getRequestURI();
		System.out.println("요청된 주소 : " + uri);
		
		
		// 요청 키워드 추출 (write, list, read, delete, modify)
		String keyword = uri.replaceAll("/certification/board/|.brd", "");
		System.out.println(keyword);
		
		
		// (밑에 나열된 키워드(list, write, delete..)과 다른 요청이 들어올 경우 
		// "/myhome" 인덱스 페이지로 리다이렉트할 수 있도록 기본경로 세팅
		NextAction next = new NextAction("/", true);
		
		if("list".equals(keyword)) {
			new ListAction().execute(request, response); // <--- DB 조회
			next.setNextPath("/board/listView.jsp"); // <--- 바로 다음에 갈 경로
			next.setRedirect(false); // 포워드
		}
		else if("write".equals(keyword)) {
			next.setNextPath("/board/writeView.jsp");
			next.setRedirect(true);
		}
		else if("writeAction".equals(keyword)) {
			request.setCharacterEncoding("utf-8");
			new WriteAction().execute(request, response);
			next.setNextPath("/board/resultView.jsp");
			next.setRedirect(false);
		}
		else if("delete".equals(keyword)) {
			new DeleteAction().execute(request, response);
			next.setNextPath("/board/resultView.jsp");
			next.setRedirect(false);
		}
		
		////////////// 수정 기능 ////////////// 
		else if("modify".equals(keyword)) {
			new ReadAction().execute(request, response); // 본문글을 가져오기 위함
			next.setNextPath("/board/modifyView.jsp");  // 수정 form으로
			next.setRedirect(false);
		}
		else if("modifyAction".equals(keyword)) {
			new ModifyAction().execute(request, response); // 수정 action 실행
			next.setNextPath("/board/resultView.jsp");  // 결과 페이지로
			next.setRedirect(false);
		}
		//////////////수정 기능 끝 ////////////// 
		
		
		else if("read".equals(keyword)) {
			new ReadAction().execute(request, response);
			next.setNextPath("/board/readView.jsp");
			next.setRedirect(false);
		}
		else if("writeReply".equals(keyword)) {
			new WriteReplyAction().execute(request, response);
			next.setNextPath("/board/read.brd?num=" + request.getParameter("boardNum"));
			next.setRedirect(false);
		}
		
		if(next.isRedirect()) { // 리다이렉트?
			response.sendRedirect( "/certification" + next.getNextPath() );
		}
		else { // 포워드?
			request.getRequestDispatcher( next.getNextPath() )
					.forward(request, response);
		}
	}
}








