/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.domain.User;

/**
 * 43.使用JdbcTemplate工具类简化对象查询
 * 44.JdbcTemplate类中的其他各个查询方法
 * @author qibie
 * @createDate:2018-06-11
 * @ProjectName:jdbc
 */
public class JdbcTemplateTest {
	static JdbcTemplate jdbc = new JdbcTemplate(JdbcUtils.getDataSource());

	public static void main(String[] args) {
		User user = findUser("zhangsan");
		System.out.println("user" + user);
		System.out.println("=============");
		System.out.println("users:" + findUsers(3));
		System.out.println("=============");
		System.out.println("user count:" + getUserCount());
		System.out.println("=============");
		System.out.println("user name:" + getUserName(1));
		System.out.println("=============");
		System.out.println("dataMap:" + getData(1));
	}

	// queryForMap
	static Map getData(int id) {
		String sql = "select id as userId, name, money, birthday from user where id=" + id;
		return jdbc.queryForMap(sql);
	}
	
	// 拿到返回的String稍微不同
	static String getUserName(int id) {
		String sql = "select name from user where id=" + id;
		Object name = jdbc.queryForObject(sql, String.class);
		return (String) name;
	}

	// 利用JdbcTemplate封装的queryForInt方法来实现一个技术方法
	static int getUserCount() {
		String sql = "select count(*) from user";
		return jdbc.queryForInt(sql);
	}

	// 通过反射和结果集中的metadata来返回一个List
	static List findUsers(int id) {
		JdbcTemplate jdbc = new JdbcTemplate(JdbcUtils.getDataSource());
		// sql的处理方法，只能比类中的少，不能比类中的多，列名不一致的话，可以使用别名的方式处理
		String sql = "select id, name, money, birthday from user where id < ?";
		Object[] args = new Object[] { id };
		List users = jdbc.query(sql, args, new BeanPropertyRowMapper(User.class));
		return users;
	}

	// 通过反射和结果集中的metadata来返回一个对象
	static User findUser(String name) {
		// sql的处理方法，只能比类中的少，不能比类中的多，列名不一致的话，可以使用别名的方式处理
		String sql = "select id, name, money, birthday from user where name = ?";
		Object[] args = new Object[] { name };
		Object user = jdbc.queryForObject(sql, args, new BeanPropertyRowMapper(User.class));
		return (User) user;
	} 

	static User findUser1(String name) {
		String sql = "select id, name, money, birthday from user where name = ?";
		Object[] args = new Object[] { name };
		Object user = jdbc.queryForObject(sql, args, new RowMapper() {

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setMoney(rs.getFloat("money"));
				user.setBirthday(rs.getDate("birthday"));
				return user;
			}
		});
		return (User) user;
	}
}
