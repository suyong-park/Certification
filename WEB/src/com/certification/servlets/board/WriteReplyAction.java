package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.certification.beans.ReplyDao;
import com.certification.beans.ReplyVo;


public class WriteReplyAction implements Action	{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		ReplyVo vo = new ReplyVo();
		vo.setContent(request.getParameter("content"));
		vo.setWriterNum((int)request.getSession().getAttribute("num"));
		vo.setBoardNum(Integer.parseInt(request.getParameter("boardNum")));
		vo.setParentNum(Integer.parseInt(request.getParameter("parentNum")));
		vo.setDepth(Integer.parseInt(request.getParameter("depth")));
		
		ReplyDao.getInstance().insert(vo);
	}
}
