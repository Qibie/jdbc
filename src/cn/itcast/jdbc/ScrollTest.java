package cn.itcast.jdbc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.itcast.jdbc.JdbcUtils;

/**
 * 
 */

/**
 * @author qibie
 * @createDate:2018-05-01
 * @ProjectName:jdbc可滚动的结果集
 */
public class ScrollTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		scroll();
	}

	
	static void scroll() throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// 4.ִ执行语句
			rs = st.executeQuery("select id, name, money, birthday  from user");

			// 5.处理结果
			while (rs.next()) {
				System.out.println(rs.getObject("id") + "\t" 
						+ rs.getObject("name") + "\t" 
						+ rs.getObject("birthday")
						+ "\t" + rs.getObject("money"));
			}
			System.out.println("--------------------");
			// 从结果集第几行的前一行开始取
			rs.absolute(5);
			// 分页 若关系型数据库不支持分页，可以使用可滚动结果集来实现
			int i = 0;
			while (rs.next() && i < 10) {
				i++;
				System.out.println(rs.getObject("id") + "\t" 
						+ rs.getObject("name") + "\t" 
						+ rs.getObject("birthday")
						+ "\t" + rs.getObject("money"));
			}
//			if(rs.previous()) {
//				System.out.println(rs.getObject("id") + "\t"
//					+ rs.getObject("name") + "\t"
//					+ rs.getObject("birthday") + "\t"
//					+ rs.getObject("money"));
//			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}
}
