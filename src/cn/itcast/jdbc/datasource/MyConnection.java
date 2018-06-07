/**
 * 
 */
package cn.itcast.jdbc.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过代理模式来保持用户关闭连接的习惯
 * 继承JDBC中的Connection，使用无差别，所有使用Connection的地方都可以使用MyConnection
 * @author qibie
 * @createDate:2018-05-22
 * @ProjectName:jdbc
 */
public class MyConnection implements Connection {
	// 使用java的Connection接口，不使用mysql的Connection，不用以后替换数据库再修改
	private Connection realConnection;
	private MyDataSource2 dataSource;
	// 连接的最大使用次数
	private int maxUseCount = 10;
	private int currentUserCount = 0;
	// 传进来某个数据库的connection才是真实可以用的connection，赋值给realConnection
	MyConnection(Connection connection, MyDataSource2 dataSource) {
		this.realConnection = connection;
		this.dataSource = dataSource;
	}

	/* 
	 * 交给realConnection的unwrap方法
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		this.realConnection.unwrap(iface);
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* 交给realConnection的prepareStatement方法来做
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.realConnection.prepareStatement(sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	@Override
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		this.realConnection.commit();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		this.realConnection.rollback();
	}

	/* 拦截connection.close()方法，重写方法，把连接重新放回连接池中
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		// close一次，增加一次计数
		this.currentUserCount++;
		// 如果当前连接数没有超过最大数，Connection使用完后还可以放入连接池中
		if (this.currentUserCount < this.maxUseCount) {
			// 这里是把MyConnection（this）重新放回池中，因为close方法已经处理了
			// 如果放入realConnection，调用方法把Jdbc连接关闭掉了
			// MyConnection.close()方法可以把close拦截下来
			// 此处把MyConnection放回池中，如果把Connection放回池中，则真正连接被关闭
			this.dataSource.connectionsPool.addLast(this);
		} 
		// 否则真正关闭连接，同时当前连接数-1
		else {
			this.realConnection.close();
			this.dataSource.currentCount--;
		}
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getMetaData()
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	@Override
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getCatalog()
	 */
	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTypeMap()
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createClob()
	 */
	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createBlob()
	 */
	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createNClob()
	 */
	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createSQLXML()
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isValid(int)
	 */
	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getClientInfo()
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSchema(java.lang.String)
	 */
	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getSchema()
	 */
	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
	 */
	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor, int)
	 */
	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getNetworkTimeout()
	 */
	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
