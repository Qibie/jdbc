package cn.itcast.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.dao.DaoException;
import cn.itcast.jdbc.dao.UserDao;
import cn.itcast.jdbc.domain.User;

/**
 * @author qibie
 * @createDate:2018-04-22
 * @ProjectName:jdbc
 */
public class UserDaoJdbcImpl implements UserDao {
	// 结合Service层讲解Dao层的异常处理
	// 运行时异常(此处创建一个运行处理子类来抛出)、编译时异常（必须处理）
	// jdbc异常处理，接口一般不抛出异常，而是try/catch处理掉
	// 为了保证在数据访问层替换实现方式（jdbc->Hibernate）时，改动不大而且不出错，因为Hibernate不抛出异常
	@Override
	public void addUser(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into user(name, birthday, money) values (?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			// 此处是java.sql包中的Date
			ps.setDate(2, new Date(user.getBirthday().getTime()));
			ps.setFloat(3, user.getMoney());
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				user.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	@Override
	public User getUser(int userId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select id, name, money, birthday  from user where id=?";
			ps = conn.prepareStatement(sql);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = mappingUser(rs);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
		return user;
	}

	@Override
	public User findUser(String loginName, String password) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select id, name, money, birthday from user where name=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, loginName);
			rs = ps.executeQuery();
			while (rs.next()) {
				user = mappingUser(rs);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
		return user;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private User mappingUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setMoney(rs.getFloat("money"));
		user.setBirthday(rs.getDate("birthday"));
		return user;
	}

	@Override
	public void update(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "update user set name=?, birthday=?, money=? where id=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setDate(2, new Date(user.getBirthday().getTime()));
			ps.setFloat(3, user.getMoney());
			ps.setInt(4, user.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	@Override
	public void delete(User user) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			st = conn.createStatement();
			String sql = "delete from user where id=" + user.getId();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}

}
