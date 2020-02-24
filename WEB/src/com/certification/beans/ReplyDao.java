package com.certification.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ReplyDao implements Dao<ReplyVo> {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	private DataSource ds;
	private static ReplyDao instance;
	
	
	
	@Override
	public boolean delete(ReplyVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(ReplyVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReplyVo select(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	// 기본 생성자
	private ReplyDao() {
		try {
			Context ctx = new InitialContext();
			ds =(DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		} catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	// 객체를 리턴하는 메서드
	public static ReplyDao getInstance() {
		if(instance == null) {
			instance = new ReplyDao();
		}
		return instance;
	}
	
	public ArrayList<ReplyVo> selectList(){ return null; }
	
	public ArrayList<ReplyVo> selectList(int boardNum){
		ArrayList<ReplyVo> list = new ArrayList<>();
		String sql = "SELECT * " + 
				" FROM (SELECT * FROM reply WHERE boardNum=?)" + 
				" START WITH parentnum = 0" + 
				" CONNECT BY PRIOR  num = parentnum";
		try {
			con = ds.getConnection(); 
			ps = con.prepareStatement(sql); 
			ps.setInt(1, boardNum);
			rs = ps.executeQuery();
			while(rs.next()) {
				ReplyVo vo = new ReplyVo();
				
				vo.setNum(rs.getInt("num"));
				vo.setContent(rs.getString("content"));
				vo.setWriterNum(rs.getInt("writernum"));
				vo.setWriter(AccountDao.getInstance().select(vo.getWriterNum()).getNickname());
				vo.setBoardNum(rs.getInt("boardnum"));
				vo.setParentNum(rs.getInt("parentnum"));
				vo.setDepth(rs.getInt("depth"));
				vo.setRegdate(rs.getString("regdate"));
				list.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return list.isEmpty() ? null : list;
	}
	
	public boolean insert(ReplyVo vo) {
		boolean result = false;
		String sql;
		try {
			con = ds.getConnection();
			
			
			sql = "INSERT INTO reply VALUES(" + 
				"re_seq.NEXTVAL," + 
				"?," + // 게시글 번호
				"?," + // 댓글 내용
				"?," + // 댓글쓴이 번호
				"?," + // 이 댓글의 깊이 
				"?," + // 부모 댓글 번호 
				"SYSDATE)";
			con = ds.getConnection(); 
			ps = con.prepareStatement(sql); 
			ps.setInt(1, vo.getBoardNum());
			ps.setString(2, vo.getContent());
			ps.setInt(3, vo.getWriterNum());
			ps.setInt(4, vo.getDepth());
			ps.setInt(5, vo.getParentNum());
			result = ps.executeUpdate() == 1;
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
}





