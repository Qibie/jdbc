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
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws SQLException, InterruptedException {
		read();
		//		int id = create();
//		System.out.println("id:" + id);
	}
	
	static void read() throws SQLException, InterruptedException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// 3.创建语句
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 4.执行语句
			rs = st.executeQuery("select id, name, money, birthday from user where id < 5");
			// 5.处理结果
			while (rs.next()) {
				int id = rs.getInt("id");
				System.out.println("show " + id + "...");
				// 10秒
				Thread.sleep(100000L);
				System.out.println(id + "\t" + rs.getObject("name") + "\t"
						+ rs.getObject("birthday") + "\t"
						+ rs.getObject("money"));
			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
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
