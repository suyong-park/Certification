package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.certification.beans.AccountDao;
import com.certification.beans.AccountVo;
import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;
import com.certification.beans.ReplyDao;

public class ReadAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1. num 파라미터 받기 
		String s = request.getParameter("num");
		int num = Integer.parseInt(s);
		
		// 2. Dao 를 통하여 DB에서 num번 게시글 정보를 받기 ( vo로 리턴받음 ) 
		BoardDao dao = BoardDao.getInstance();
		
		
		Cookie[] cookies = request.getCookies();
		boolean check = false;
		for(Cookie c : cookies) {
			if(c.getName().equals( String.valueOf(num) ) ){
				check = true;
				break;
			}
		}
		
		// 이 세션이 이 글을 처음봤니?
		if(!check) {
			Cookie cookie = new Cookie(String.valueOf(num), "");
			response.addCookie(cookie);
			dao.updateHit(num);
		}
		
		BoardVo vo = dao.select(num);
		
		// 3. writerNum ---> 작성자 닉네임 받기
		AccountDao dao2 = AccountDao.getInstance();
		AccountVo vo2 = dao2.select(vo.getWriterNum());
		vo.setWriter( vo2.getNickname() );
		
		// 4. vo 를 request 바구니에 담기 
		request.setAttribute("vo", vo);
		request.setAttribute("replyList", ReplyDao.getInstance().selectList(num));
	}
}
