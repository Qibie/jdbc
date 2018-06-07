/**
 * 
 */
package cn.itcast.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 编写一个基本连接池来实现连接的复用（连接池处理方式使用了先入先出算法，从头部拿，从尾部入）
 * 因为MyConnection实现了Connection接口，所以池中放入MyConnection，这是一段中间代码
 * @author qibie
 * @createDate:2018-05-22
 * @ProjectName:jdbc
 */
public class MyDataSource3 {
	private static String url = "jdbc:mysql://localhost:3306/jdbc?useSSL=false&generateSimpleParameterMetadata=true";
	private static String user = "root";
	private static String password = "root";
	// 这里把MyConnection放入池中，同时修改创建连接的方法createConnection（）
	LinkedList<MyConnection> connectionsPool = new LinkedList<MyConnection>();

	// 初始连接数
	private static int initCount = 5;
	// 基于保护机制，最大连接数
	private static int maxCount = 10;
	// 当前连接数
	private int currentCount = 0;
	
	// 建立连接池
	public MyDataSource3() {
		try {
			for (int i = 0; i < initCount; i++) {
				this.connectionsPool.addLast(this.createConnection());;
				this.currentCount++;
			}
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	
	public Connection getConnection() throws SQLException {
		// 加入同步块，防止getConnection()方法不会被两个线程同时拿走，只能被一个拿走
		// 同步块相当于加一个锁，若果两个线程同时来拿，先到的线程会让其他线程等，然后再进来
		// 这样保证多个线程拿的不会是同一个连接
		synchronized (connectionsPool) {
			// 而且需要判断线程池中是否还有连接，如果没有了可以建立新连接，但不能一直建立新连接
			if (connectionsPool.size() > 0) {
				// 使用了先入先出算法，从头部拿，从尾部入
				return this.connectionsPool.removeFirst();
			}
			// 如果当前连接数小于最大连接数，可以创建连接
			if (currentCount < maxCount) {
				this.currentCount++;
				return this.createConnection();
			}
			// 池中已经没有连接，而且超出最大连接数，抛出一个异常
			throw new SQLException("已没有连接");
		}
	}
	
	// 池是一个容器，连接从容器里拿出来以后，放入池中，再进行释放
	public void free(Connection conn) {
		// 如果connection是MyConnection的实例，就重新放入池中
		if (conn instanceof MyConnection) {
			this.connectionsPool.addLast((MyConnection) conn);
		}
	}
	
	// 建立连接，通过DriverManager.getConnection()方法
	private MyConnection createConnection() throws SQLException {
		// 通过DriverManager拿到真实的Connection
		Connection realConn = DriverManager.getConnection(url, user, password);
		// 通过MyConnection的构造器来获取MyConnection对象
		MyConnection myConnection = new MyConnection(realConn, new MyDataSource2());
		return myConnection;
	}
}
