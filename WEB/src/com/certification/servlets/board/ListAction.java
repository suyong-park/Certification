package com.certification.servlets.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;

public class ListAction implements Action{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = request.getParameter("page");
		int page;
		if(param == null) {
			page = 1;
		}
		else {
			page = Integer.parseInt(param);
		}
		BoardDao dao = BoardDao.getInstance();
		ArrayList<BoardVo> list = dao.selectList(page); 
		
		request.setAttribute("list", list);
		request.setAttribute("lastPage", dao.totalPages());
		
	}
}


















