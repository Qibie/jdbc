/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.dao.refactor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.dao.DaoException;
import cn.itcast.jdbc.domain.User;

/**
 * 42.使用策略模式对模板方法设计模式进行改进
 * @author qibie
 * @createDate:2018-06-10
 * @ProjectName:jdbc
 */
public class MyDaoTemplate {
	public Object find(String sql, Object[] args, RowMapper rowMapper) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			Object obj = null;
			if (rs.next()) {
				obj = rowMapper.mapRow(rs);
			}
			return obj;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}