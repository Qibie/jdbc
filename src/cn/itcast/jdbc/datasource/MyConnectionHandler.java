/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Objects;

/**
 * 37.Java的动态代理及使用该技术完善连接代理
 * 
 * @author qibie
 * @createDate:2018-06-09
 * @ProjectName:jdbc
 */
class MyConnectionHandler implements InvocationHandler {
	private Connection realConnection;
	private Connection warpedConnection;
	private MyDataSource2 dataSource;
	
	private int maxUseCount = 5;
	private int currentUserCount = 0;

	MyConnectionHandler(MyDataSource2 dataSource) {
		this.dataSource = dataSource;
	}

	Connection bind(Connection realConn) {
		// 把Connection.class这个给通过调用处理器传递给this(MyConnectionHandler)
		this.realConnection = realConn;
		this.warpedConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { Connection.class }, this);
		return warpedConnection;
	}

	// 所有对Connection的操作都会传递到invoke这个方法来，但此处只处理close方法，拦截下来，connection不关闭只是重新放入连接池中
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Objects.equals("equals", method.getName())) {
			// 包了一层的connection就是拦截close方法
			this.currentUserCount++;
			if (this.currentUserCount < this.maxUseCount) {
				this.dataSource.connectionsPool.addLast(this.warpedConnection);
			} else {
				this.realConnection.close();
				this.dataSource.currentCount--;
			}
		}
		return method.invoke(this.realConnection, args);
	}

}
