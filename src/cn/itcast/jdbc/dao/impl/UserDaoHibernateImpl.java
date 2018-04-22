/**
 * 
 */
package cn.itcast.jdbc.dao.impl;

import cn.itcast.jdbc.dao.UserDao;
import cn.itcast.jdbc.domain.User;

/**
 * @author qibie
 * @createDate:2018-04-22
 * @ProjectName:jdbc
 */
public class UserDaoHibernateImpl implements UserDao {

	/* (non-Javadoc)
	 * @see cn.itcast.jdbc.dao.UserDao#addUser(cn.itcast.jdbc.domain.User)
	 */
	@Override
	public void addUser(User User) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cn.itcast.jdbc.dao.UserDao#getUser(int)
	 */
	@Override
	public User getUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.itcast.jdbc.dao.UserDao#findUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User findUser(String loginName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cn.itcast.jdbc.dao.UserDao#update(cn.itcast.jdbc.domain.User)
	 */
	@Override
	public void update(User user) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cn.itcast.jdbc.dao.UserDao#delete(cn.itcast.jdbc.domain.User)
	 */
	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub

	}

}
