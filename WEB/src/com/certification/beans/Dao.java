package com.certification.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public interface Dao <T extends Vo>{
	
	public static final String DB_USER = "certification";
	public static final String DB_PASSWORD = "12151215";
	public static final String DB_URL = "jdbc:oracle:thin:@//certification.ch3ulmd2qtg7.ap-northeast-2.rds.amazonaws.com:1521/certifi";
	public static final String DB_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	
	// Connection �����ϴ� �޼��� 
	/**
	 * DB�� url, user, password�� ����Ͽ� DB Ŀ�ؼ� ����
	 * @return Connection ��ü
	 * @throws Exception
	 */
	default Connection openConnection() throws Exception {
		Class.forName(DB_DRIVER_CLASS);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
	
	/**
	 * ���ڷ� ���� Connection, PreparedStatement, ResultSet ��ü�� close()
	 * @param con (close�� Connection)
	 * @param ps (close�� PreparedStatement)
	 * @param rs (close�� ResultSet)
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
