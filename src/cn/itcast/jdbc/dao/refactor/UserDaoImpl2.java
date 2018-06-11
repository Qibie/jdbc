package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.User;

/**
 * 使用策略模式对模板方法设计模式进行改进
 * 
 * @author qibie
 * @createDate:2018-06-11
 * @ProjectName:jdbc
 */
public class UserDaoImpl2 {
	MyDaoTemplate template = new MyDaoTemplate();

	public User findUser(String loginName, String password) {
		String sql = "select id, name, money, birthday from user where name = ?";
		Object[] args = new Object[] { loginName };
		// Object user = super.find(sql, args);\
		// 与之前不同，不用abstract超类，而是通过template来实现
		// 需要三个参数查询，sql，参数和行映射器
		RowMapper mapper = new UserRowMapper();
		Object user = this.template.find(sql, args, mapper);
		return (User) user;
	}

	public String findUseName(int id) {
		String sql = "select name from user where id = ?";
		Object[] args = new Object[] { id };
		// 策略模式通过内部匿名类来实现行映射器
		Object name = this.template.find(sql, args, new RowMapper() {
			
			public Object mapRow(ResultSet rs) throws SQLException {
				return rs.getString("name");
			}
		});
		return (String) name;
	}
}


	// 行映射器
class UserRowMapper implements RowMapper {
	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setMoney(rs.getFloat("money"));
		user.setBirthday(rs.getDate("birthday"));
		return user;
	}
}