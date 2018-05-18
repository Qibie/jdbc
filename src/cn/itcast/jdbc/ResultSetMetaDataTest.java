/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利用结果集元数据将查询结果封装为map
 * @author qibie
 * @createDate:2018-05-19
 * @ProjectName:jdbc
 */
public class ResultSetMetaDataTest {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		List<Map<String, Object>> datas = read("select id, name as n from user where id < 5");
		System.out.println(datas);
	}
	
	static List<Map<String, Object>> read(String sql) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// 结果集元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			// 存放列名的字符串数组
			String[] colNames = new String[count]; 
			for (int i = 1; i <= count; i++) {
				// 获取该列的java类型
				System.out.print(rsmd.getColumnClassName(i) + "\t");
				// 获取列名
				System.out.print(rsmd.getColumnName(i) + "\t");
				// 获取列名的别名
				System.out.println(rsmd.getColumnLabel(i));
				// 列名取别名 索引越界 从0开始 减1
				colNames[i - 1] = rsmd.getColumnLabel(i);
			}
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> data = new HashMap<String, Object>();
				for (int i = 0; i < colNames.length; i++) {
					data.put(colNames[i], rs.getObject(colNames[i]));
				}
				datas.add(data);
			}
			return datas;
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
