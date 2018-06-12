/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.domain.User;

/**
 * 46.使用支持命名参数的JdbcTemplate
 * @author qibie
 * @createDate:2018-06-12
 * @ProjectName:jdbc
 */
public class NamedJdbcTemplate {
	static NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(JdbcUtils.getDataSource());

	public static void main(String[] args) {
		User user = new User();
		user.setMoney(10.0f);
		user.setId(2);
		System.out.println(findUser1(user));
	}
	
	// 使用的增加操作用户返回主键的操作
	static void addUser(User user) {
		String sql = "insert into user(name, birthday,money) values (:name, :birthday, :money)";
		// 获取数据源操作
		SqlParameterSource ps = new BeanPropertySqlParameterSource(user);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		named.update(sql, ps, keyHolder);
		// 执行更新操作后，操作数据库后，会返回主键，回写到对象中去
		int id = keyHolder.getKey().intValue();
		user.setId(id);
		
		// 有可能主键不是int型的，可能是复合主键，通过KeyHolder.getKeys获取一个Map
		Map keys = keyHolder.getKeys();
		// 其中key是列名，value是对应的值
	}
	
	static User findUser(User user) {
		// 使用JdbcTemplate的话，每个占位符必须和传递的参数对齐
		// String sql = "select id, name, money, birthday from user where name = ? and
		// money > ? and id < ?";
		// Object[] args = new Object[] { user.getName(), user.getMoney(), user.getId()
		// };
		// 使用map来存放自己命名的参数
		String sql = "select id, name, money, birthday from user " + "where money > :m and id < :id";
		Map params = new HashMap();
		// params.put("n", user.getName());
		params.put("m", user.getMoney());
		params.put("id", user.getId());
		Object u = named.queryForObject(sql, params, new BeanPropertyRowMapper(User.class));
		return (User) u;
	}

	// 使用带参数的命名JdbcTemplate好处
	static User findUser1(User user) {
		// 限制列名必须和对象中的属性名字一样
		String sql = "select id, name, money, birthday from user " +
				"where money > :money and id < :id";
		// 获取参数源操作
		// 传入一个对象来返回一个相当于map的SqlParameterSource参数接口
		SqlParameterSource ps = new BeanPropertySqlParameterSource(user);
		Object u = named.queryForObject(sql, ps, new BeanPropertyRowMapper(User.class));
		return (User) u;
	} 
}
