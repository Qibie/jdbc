/**
 * 
 */
package cn.itcast.jdbc.dao;

import java.util.Date;

import cn.itcast.jdbc.dao.impl.UserDaoJdbcImpl;
import cn.itcast.jdbc.domain.User;

/**
 * @author qibie
 * @createDate:2018-04-22
 * @ProjectName:jdbc
 */
public class UserDaoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 只是实现了jdbc的接口
//		UserDao userDao = new UserDaoJdbcImpl();
		
		// 采用工厂设计模式，不用导包
		UserDao userDao = DaoFactory.getInstance().getUserDao();
		// System.out.println(userDao);
		// 插入操作
		User user = new User();
		user.setBirthday(new Date());
		user.setName("dao name2");
		user.setMoney(1000.0f);
//		userDao.addUser(user); 
//		System.out.println(user.getId());
//
//
//		 userDao.addUser(user);
		// 查找
//		 User u = userDao.findUser(user.getName(), null);
//		 System.out.println(u.getId());
		
		// 更新
//		User u = userDao.getUser(1);
//		u.setMoney(20002.1f);
//		userDao.update(u);
		
		// 删除
//		 User u1 = userDao.getUser(5);
//		 userDao.delete(u1);
	}

}
