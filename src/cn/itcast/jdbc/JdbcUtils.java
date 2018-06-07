package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.itcast.jdbc.datasource.MyDataSource2;

/**
 * JdbcUtil
 * 2008-12-6
 * @author <a href="mailto:liyongibm@hotmail.com">����</a>
 * 
 */
public final class JdbcUtils {
	private static String url = "jdbc:mysql://localhost:3306/jdbc?useSSL=false&generateSimpleParameterMetadata=true";
	private static String user = "root";
	private static String password = "root";
	// 使用封装类来建立连接
	private static MyDataSource2 myDataSource = null;

	private JdbcUtils() {
	}

	static {
		try {
			// 注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			myDataSource = new MyDataSource2();
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		// 没有建立连接池之前的方法 使用DriverManager
		// return DriverManager.getConnection(url, user, password);
		// 建立连接池后从封装类中建立连接
		return myDataSource.getConnection();
	}

	public static void free(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						// 释放连接
						 conn.close();
						// 换成封装类连接池中的连接
//						myDataSource.free(conn);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
}
