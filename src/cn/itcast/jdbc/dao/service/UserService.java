package cn.itcast.jdbc.dao.service;

import cn.itcast.jdbc.dao.DaoException;
import cn.itcast.jdbc.dao.UserDao;
import cn.itcast.jdbc.domain.User;

/**
 * @author qibie
 * @createDate:2018-04-22
 * @ProjectName:jdbc
 */
public class UserService {
	private UserDao userDao;

	public void regist(User user) {
		try {
			this.userDao.addUser(user);
		} catch (DaoException e) {
			// TODO: handle exception
		}
	}
	//senMail.send(user);
}
