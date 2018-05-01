/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author qibie
 * @createDate:2018-05-01
 * @ProjectName:jdbc
 */
public class OtherApi {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		int id = create();
		System.out.println("id:" + id);
	}
	
	static int create() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			String sql = "insert into user(name,birthday, money) values ('name2 gk', '1987-01-01', 400) ";
			// prepareStatement的一个重载方法，返回主键
			// mysql后面的参数可以拿回来，不写也行，只要符合jdbc3.0规范
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			int id = 0;
			if(rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
