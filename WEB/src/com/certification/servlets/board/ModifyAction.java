package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		request.setCharacterEncoding("utf-8");
		
		BoardVo vo = new BoardVo();
		vo.setNum( Integer.parseInt(request.getParameter("num")) );
		vo.setTitle( request.getParameter("title"));
		vo.setContent( request.getParameter("content"));
		
		
		
		BoardDao dao = BoardDao.getInstance();
		boolean result = dao.update(vo);
		
		request.setAttribute("result", result);
		request.setAttribute("status", "modify");
	}

}
