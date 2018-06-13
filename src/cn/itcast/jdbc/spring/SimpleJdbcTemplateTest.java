/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.spring;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.domain.User;

/**
 * 使用SimpleJdbcTemplate和泛型技术简化代码
 * 
 * @author qibie
 * @createDate:2018-06-13
 * @ProjectName:jdbc
 */
public class SimpleJdbcTemplateTest {
	static SimpleJdbcTemplate simple = new SimpleJdbcTemplate(JdbcUtils.getDataSource());

	public static void main(String[] args) {
		find("", User.class);
	}

	//使用泛型方法查找，可以省却向下转型的操作
	static <T> T find(String name, Class<T> clazz) {
		String sql = "select id, name, money, birthday from user where name = ? and money = ?";
		T user = simple.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(clazz), name,
				100f);
		return (T) user;
	}
	
	static User findUser(String name) {
		String sql = "select id, name, money, birthday from user where name = ? and money = ?";
//		Object user = simple.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class), name,
//				100f);
		User user = simple.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class), name,
				100f);
//		simple.getNamedParameterJdbcOperations().update(sql, keyHolder);
		simple.getJdbcOperations();
		return user;
	}

}
