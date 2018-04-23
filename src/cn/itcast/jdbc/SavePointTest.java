/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * @author qibie
 * @createDate:2018-04-23
 * @ProjectName:jdbc
 */
public class SavePointTest {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		test();
	}

	// 预期结果id=1的钱-1，id为2，3的钱不变，部分commit
	static void test() throws SQLException {
		Connection conn = null;
		// mysql中的Innodb引擎支持事务，myism不支持
		// 开启事务
		Statement st = null;
		ResultSet rs = null;
		// 事务的保存点处理 支持部分回滚
		Savepoint sp = null;
		try {
			conn = JdbcUtils.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			String sql = "update user set money = money - 10 where id = 1";
			st.executeUpdate(sql);
			// 事务保存点
			sp = conn.setSavepoint();

			sql = "update user set money = money - 10 where id = 3";
			st.executeUpdate(sql);

			sql = "select money from user where id = 2";
			rs = st.executeQuery(sql);
			float money = 0.0f;
			if (rs.next()) {
				money = rs.getFloat("money");
			}
			if (money > 300) {
				// 处理异常时，不知道抛出什么异常，抛出一个运行时异常
				throw new RuntimeException("已经超过了最大值了！");

			}

			// 事务的原子性：两件事要么同时发生，只要一个不能发生就所有不能发生
			// 提交事务提交
			conn.commit();
		} catch (RuntimeException e) {
			if (conn != null && sp != null) {
				// 回滚事务回滚的另一种重载方法，回滚到保存点，再提交
				conn.rollback(sp);
				conn.commit();
			}
			throw e;
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
