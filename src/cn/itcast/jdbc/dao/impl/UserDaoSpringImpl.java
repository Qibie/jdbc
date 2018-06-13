/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.dao.impl;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.dao.UserDao;
import cn.itcast.jdbc.domain.User;

/**
 * 48.使用JdbcTemplate实现DAO和用工厂灵活切换实现
 * @author qibie
 * @createDate:2018-06-13
 * @ProjectName:jdbc
 */
public class UserDaoSpringImpl implements UserDao {
	private SimpleJdbcTemplate simpleJdbcTemplate = new SimpleJdbcTemplate(JdbcUtils.getDataSource());

	// 使用NamedParameterJdbcTemplate类的update操作
	// 传入对象是BeanPropertySqlParameterSource类
	@Override
	public void addUser(User user) {
		String sql = "insert into user (name, money, birthday) values (:name, :money, :birthday)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.simpleJdbcTemplate.getNamedParameterJdbcOperations().update(sql, param, keyHolder);
		user.setId(keyHolder.getKey().intValue());
	}

	@Override
	public User getUser(int userId) {
		String sql = "select id, name, money, birthday from user where id = ?";
		return this.simpleJdbcTemplate.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class),
				userId);
	}

	// ParameterizedBeanPropertyRowMapper.newInstance(User.class)行映射器
	@Override
	public User findUser(String loginName, String password) {
		String sql = "select id, name, money, birthday from user where name = ?";
		return this.simpleJdbcTemplate.queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class),
				loginName);
	}

	@Override
	public void update(User user) {
		// 使用占位符的操作
		String sql = "update user set name = ?, birthday = ?, money = ? where id = ?";
		this.simpleJdbcTemplate.update(sql, user.getName(), user.getBirthday(), user.getMoney(), user.getId());
	
		// 使用Bean属性参数源的方法
		sql = "update user set name = :name, birthday = :birthday, money = ?:money where id = :id";
		this.simpleJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(user));
	}

	// 使用SimpleJdbcTemplate类update(sql, args...)可变长度的特性执行删除操作
	@Override
	public void delete(User user) {
		String sql = "delete from user where id = ?";
		this.simpleJdbcTemplate.update(sql, user.getId());
	}

}
