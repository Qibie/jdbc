/**
 * 
 */
package cn.itcast.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.User;

/**
 * 利用Java反射技术将查询结果封装为对象
 * 
 * @author qibie
 * @createDate:2018-05-21
 * @ProjectName:jdbc
 */
public class ORMTest {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void main(String[] args) throws IllegalAccessException, Exception, SQLException {
		// User user = getUser("select id as Id, name as Name, birthday as Birthday,
		// money as Money from user where id = 1");
		// System.out.println(user);
		User user = (User) getObject(
				"select id as Id, name as Name, birthday as Birthday, money as Money from user where id = 1",
				User.class);
		System.out.println(user);
		System.out.println("===========");
		Bean bean = (Bean) getObject(
				"select id as Id, name as Name, birthday as Birthday, money as Money from user where id = 1",
				Bean.class);
		// 这个结果只会输出Bean里面的toString()方法，而且只会输出Name，因为Bean类中只有Name那一列
		System.out.println(bean);
	}

	static Object getObject(String sql, Class clazz)
			throws SQLException, Exception, IllegalArgumentException, InstantiationException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			String[] colNames = new String[count];
			// 根据数据库字段名和User对象属性名和一样进行操作
			// 前提User类需要符合Java Bean规范，即标准的POJO类
			// 如果不一样，需要使用别名来进行处理
			for (int i = 1; i <= count; i++) {
				// 获取列名数组
				colNames[i - 1] = rsmd.getColumnLabel(i);
			}
			Object object = null;
			// 获取User类对象的public方法数组
			// Method[] ms = user.getClass().getMethods();
			// 更通用的方法
			Method[] ms = clazz.getMethods();
			if (rs.next()) {
				// 硬性约定，传进来的clazz必须有一个不带参的构造方法，因为newInstance构造实例会调用不含参的构造方法
				object = clazz.newInstance();
				// user = new User();
				for (int i = 0; i < colNames.length; i++) {
					// 列名
					String colName = colNames[i];
					// 根据 set + 列名的别名 获取User类的set方法
					String methodName = "set" + colName;
					// 这一步可能会出现异常，因为类型的大小写问题，处理方法便是在sql语句将列名处理为别名
					System.out.println(methodName);
					// 遍历数组，找到需要的set方法
					for (Method m : ms) {
						if (methodName.equals(m.getName())) {
							// Method类的invoke()方法
							m.invoke(object, rs.getObject(colName));
						}
					}
				}
			}
			return object;
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}
