package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.User;

/**
 * 重构抽象后的超类Dao的继承类
 * @author qibie
 * @createDate:2018-06-10
 * @ProjectName:jdbc
 */
public class UserDaoImpl extends AbstractDao {
	public User findUser(String loginName, String password) {
		String sql = "select id, name, money, birthday from user where name = ?";
		Object[] args = new Object[] { loginName };
		Object user = super.find(sql, args);
		return (User) user;
	}

	public String findUseName(int id) {
		String sql = "select name from user where id = ?";
		Object[] args = new Object[] { id };
		Object user = super.find(sql, args);
		return ((User) user).getName();
	}
	
	protected Object rowMapper1(ResultSet rs) throws SQLException {
		return rs.getString("namee");
	}

	protected Object rowMapper(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setMoney(rs.getFloat("money"));
		user.setBirthday(rs.getDate("birthday"));
		return user;
	}

	public void delete(User user) {
		String sql = "delete from user where id =?";
		Object[] args = new Object[] { user.getId() };
		super.update(sql, args);
	}

	public void update(User user) {
		String sql = "update user set name = ?, birthday = ?, money = ? where id = ?";
		Object[] args = new Object[] { user.getName(), user.getBirthday(), user.getMoney(), user.getId() };
		super.update(sql, args);
	}

}
