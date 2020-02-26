package com.certification.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public interface Dao <T extends Vo>{
	
	public static final String DB_USER = "certifi";
	public static final String DB_PASSWORD = "certifi";
	public static final String DB_URL = "jdbc:oracle:thin:@//certification.ch3ulmd2qtg7.ap-northeast-2.rds.amazonaws.com:1521/certifi";
	public static final String DB_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	
	// Connection 생성하는 메서드 
	/**
	 * DB의 url, user, password를 사용하여 DB 커넥션 생성
	 * @return Connection 객체
	 * @throws Exception
	 */
	default Connection openConnection() throws Exception {
		Class.forName(DB_DRIVER_CLASS);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
	
	/**
	 * 인자로 들어온 Connection, PreparedStatement, ResultSet 객체를 close()
	 * @param con (close할 Connection)
	 * @param ps (close할 PreparedStatement)
	 * @param rs (close할 ResultSet)
	 */
	default void closeConnection(Connection con, PreparedStatement ps, ResultSet rs) {
		try {
			if(con != null) { con.close(); }
			if(ps != null) { ps.close(); }
			if(rs != null) { rs.close(); }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean insert( T vo );
	boolean delete( T vo );
	boolean update( T vo );
	T select( int num );	
	ArrayList<T> selectList();
}
