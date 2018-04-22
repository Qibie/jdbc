/**
 * 
 */
package cn.itcast.jdbc.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author qibie
 * @createDate:2018-04-22
 * @ProjectName:jdbc
 */
// 工厂模式一般采用单例
public class DaoFactory {
	private UserDao userDao = null;
	private static DaoFactory instance = new DaoFactory();
	
	private DaoFactory(){
		try {
			Properties prop = new Properties();
			// 相对路径是哪个相对路径，这是一种写死的方式，若更换实现方法采用另一种读取bin目录下的classpath文件
//			InputStream inStream = new FileInputStream(new File("src/daoconfig.properties"));
			// 这里是读取bin目录是在classpath路径下的，路径更加灵活
			InputStream inStream = DaoFactory.class.getClassLoader().getResourceAsStream("daoconfig.properties");
			prop.load(inStream);
			String userDaoClass = prop.getProperty("userDaoClass");
			// 反射，图纸与实例 
			// String.class 也是 图纸Class new出来的实例
			// 嵌套关系缕一缕
//			userDao = (UserDao) Class.forName(userDaoClass).newInstance();
			Class clazz = Class.forName(userDaoClass);
			userDao = (UserDao) clazz.newInstance();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static DaoFactory getInstance() {
		return instance;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
}
