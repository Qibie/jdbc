/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author qibie
 * @createDate:2018-05-01
 * @ProjectName:jdbc
 */
public class BatchTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// 比较执行效率，批处理与循环单条插数据
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100; i++) {
			create(i);
		}
		long end = System.currentTimeMillis();
		System.out.println("create:" + (end - start));
		
		start = System.currentTimeMillis();
		createBatch();
		end = System.currentTimeMillis();
		System.out.println("createBatch:" + (end - start));
	}

	static void create(int i) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			String sql = "insert into user(name,birthday, money) values (?, ?, ?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "no batch name" + i);
			ps.setDate(2, new Date(System.currentTimeMillis()));
			ps.setFloat(3, 100f + i);
			
			ps.executeUpdate();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	static void createBatch() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			String sql = "insert into user(name,birthday, money) values (?, ?, ?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int j = 0; j < 100; j++) {
				ps.setString(1, "batch name" + j);
				ps.setDate(2, new Date(System.currentTimeMillis()));
				ps.setFloat(3, 100f + j);
				// 批处理
				ps.addBatch();
			}
			int[] is = ps.executeBatch();
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
