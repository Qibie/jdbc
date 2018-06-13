package cn.itcast.jdbc.dao;

import cn.itcast.jdbc.domain.User;

/**
 * 
 * @author HASEE
 *
 */
public interface UserDao {
	public abstract void addUser(User user);

	User getUser(int userId);

	User findUser(String loginName, String password);

	void update(User user);

	void delete(User user);
}
