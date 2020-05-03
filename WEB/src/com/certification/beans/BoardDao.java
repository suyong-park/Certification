package com.certification.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class BoardDao implements Dao<BoardVo>{
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	private static BoardDao instance; 
	static private DataSource ds; 
	static { // static �ʱ�ȭ ( �ε� �� �� �ٷ� ���� )
		try {
			// javax.naming.Context
			Context context = new InitialContext(); // context.xml �� ��üȭ
			
			
			ds = (DataSource)context.lookup("java:comp/env/jdbc/oracle");
				// lookup(String �̸�) ==> �̸��� ���� ��ü ���� 
				// (���� ��ü�� DataSource ��ü (context.xml�� 'jdbc/oracle'�� ����ص���))
				// Tomcat�� ���� "java:comp/env/"�� �ٿ��� �Ѵ�.
			
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private BoardDao() {
	}
	public static BoardDao getInstance() {
		if(instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}
	
	
	@Override
	public boolean insert(BoardVo vo) {
		String sql = "INSERT INTO board "
				+ "VALUES(board_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE)";
		boolean result = false;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getTitle());
			ps.setString(2, vo.getContent());
			ps.setInt(3, vo.getWriterNum());
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	@Override
	public boolean delete(BoardVo vo) {
		String sql = "DELETE FROM board WHERE num = ?";
		boolean result = false;
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, vo.getNum());
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	@Override
	public boolean update(BoardVo vo) {
		String sql = "UPDATE board SET title = ?, content = ? WHERE num = ?";
		boolean result = false;
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getTitle());
			ps.setString(2, vo.getContent());
			ps.setInt(3, vo.getNum());
			result = ps.executeUpdate() == 1;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}
	@Override
	public BoardVo select(int num) {
		String sql = "SELECT * FROM board WHERE num = ?";
		BoardVo vo = null;
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			
			rs = ps.executeQuery();
			
			if(rs.next()) { // next() �ּ� 1�� �� ����
				vo = new BoardVo();
				vo.setNum( rs.getInt("num") );
				vo.setTitle( rs.getString("title") );
				vo.setContent( rs.getString("content") );
				vo.setWriterNum( rs.getInt("writernum") );
				vo.setHit( rs.getInt("hit") );
				vo.setDateStr( rs.getString("regdate") );
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return vo;
	}
	
	@Override
	public ArrayList<BoardVo> selectList() {
		return selectList(1);
	}
	
	public ArrayList<BoardVo> selectList(int page) {
		String sql = "select num, name, description, company, job, link from " + 
				"(select num rn, tt.* from (select * from certification.certification order by num asc) tt) " + 
				"where rn >= ? and rn <= ?";
		ArrayList<BoardVo> list = new ArrayList<>();
		
		//AccountDao dao = AccountDao.getInstance();
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, page*5 - 4);
			ps.setInt(2, page*5);
			
			rs = ps.executeQuery();
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNum(rs.getInt(1));
				vo.setName(rs.getString(2));
				vo.setDescription(rs.getString(3));
				vo.setCompany(rs.getString(4));
				vo.setJob(rs.getString(5));
				vo.setLink(rs.getString(6));
				
				list.add(vo);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}
	
	// ��ü ������ ����
	public int totalPages() {
		String sql = "SELECT COUNT(*) FROM certification.certification";
		int total = 0;
		
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				total = rs.getInt("count(*)");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		
		return (total-1)/5 + 1;
	}
	
	
	
	public void updateHit(int num) {
		// num�� ���� hit�� 1 ���� 
		String sql = "UPDATE board SET hit = hit+1 WHERE num = ?";
		try {
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, num);
			ps.execute();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
	}
}







