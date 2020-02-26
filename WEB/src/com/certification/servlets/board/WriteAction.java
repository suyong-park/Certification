package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;

public class WriteAction implements Action{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		BoardVo vo = new BoardVo();
		vo.setContent( request.getParameter("content") );
		vo.setTitle( request.getParameter("title") );
		
		HttpSession session = request.getSession();
		int num = (int)session.getAttribute("num");  // 로그인한 회원의 번호
		vo.setWriterNum(num);
		
		BoardDao dao = BoardDao.getInstance();
		boolean result = dao.insert(vo);
		
		request.setAttribute("result", result);
		request.setAttribute("status", "write");
	}
}













