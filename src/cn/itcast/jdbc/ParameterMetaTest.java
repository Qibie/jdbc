/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 29参数的元数据信息
 * @author qibie
 * @createDate:2018-05-18
 * @ProjectName:jdbc
 */
public class ParameterMetaTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Object[] params = new Object[] {"lisi", new Date(System.currentTimeMillis()), 100f};
		read("select * from user where name = ? and birthday < ? and money > ?", params);
	}
	
	static void read(String sql, Object[] params) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs= null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// 3.创建语句
			ps = conn.prepareStatement(sql);
			// 获取元数据信息
			ParameterMetaData pmd = ps.getParameterMetaData();
			int count = pmd.getParameterCount();
			// JDBC里面很多数组索引都是从1开始的
//			for (int i = 1; i <= count; i++) {
			// 此处取sql语句中？占位符的数量
			for (int i = 1; i < params.length; i++) {
				// 获取列名
//				System.out.print(pmd.getParameterClassName(i) + "\t");
				// 获取类型
//				System.out.print(pmd.getParameterType(i) + "\t");
				// 获取类型名字
//				System.out.println(pmd.getParameterTypeName(i));
				// 替代占位符 Object数组的索引从0开始，所以要减1
				ps.setObject(i, params[i-1]);
			}
			// 4.执行语句
			rs = ps.executeQuery();
			// 5.处理结果
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t"
						+ rs.getString("name") + "\t" + rs.getDate("birthday")
						+ "\t" + rs.getFloat("money"));
			}
		} finally {
			// 6.释放资源
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
