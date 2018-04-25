/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author qibie
 * @createDate:2018-04-23
 * @ProjectName:jdbc
 */
public class TxTest {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		test();
	}

	static void test() throws SQLException {
		Connection conn = null;
		// mysql中的Innodb引擎支持事务，myism不支持
		// 开启事务
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			conn.setAutoCommit(false);
			// 设置事务隔离级别level 选取Connection接口中静态常量
			conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
			st = conn.createStatement();
			String sql = "update user set money = money - 10 where id = 1";
			st.executeUpdate(sql);

			sql = "select money from user where id = 2";
			rs = st.executeQuery(sql);
			float money = 0.0f;
			if (rs.next()) {
				money = rs.getFloat(1);
			}
			if (money > 400) {
				// 处理异常时，不知道抛出什么异常，抛出一个运行时异常
				throw new RuntimeException("已经超过了最大值了！");
			}
			sql = "update user set money = money + 10 where id = 2";
			st.executeUpdate(sql);
			// 事务的原子性：两件事要么同时发生，只要一个不能发生就所有不能发生
			// 提交事务提交
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				// 回滚事务回滚
				conn.rollback();
			}
			throw e;
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}
}
