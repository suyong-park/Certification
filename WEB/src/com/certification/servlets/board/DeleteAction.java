package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;

public class DeleteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int num = Integer.parseInt( request.getParameter("num"));
		BoardVo vo = new BoardVo();
		vo.setNum(num);
		
		BoardDao dao = BoardDao.getInstance();
		boolean result = dao.delete(vo);
		
		request.setAttribute("result", result);
		request.setAttribute("status", "delete");
	}

}
