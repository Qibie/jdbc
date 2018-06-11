package cn.itcast.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * JdbcUtil 2008-12-6
 * 
 * @author <a href="mailto:liyongibm@hotmail.com">����</a>
 * 
 */
public final class JdbcUtils {
	private static String url = "jdbc:mysql://localhost:3306/jdbc?useSSL=false&generateSimpleParameterMetadata=true";
	private static String user = "root";
	private static String password = "root";
	// 使用封装类来建立连接
	private static DataSource myDataSource = null;

	private JdbcUtils() {
	}

	static {
		try {
			// 注册驱动
			Class.forName("com.mysql.jdbc.Driver");
			// myDataSource = new MyDataSource2();
			Properties prop = new Properties();
			// 此处不使用写死的配置，因为修改配置文件比修改代码效率高，因为修改代码后还需要重新编译打包；
			// prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
			// prop.setProperty("user", "user");
			InputStream is = JdbcUtils.class.getClassLoader().
					getResourceAsStream("dbcpconfig.properties");
			prop.load(is);
			myDataSource = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static DataSource getDataSource() {
		return myDataSource;
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
						// myDataSource.free(conn);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
}
