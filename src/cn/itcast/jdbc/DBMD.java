/**
 * 
 */
package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author qibie
 * @createDate:2018-05-09
 * @ProjectName:jdbc
 */
public class DBMD {

	/** 数据库的元数据信息
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		// 打印数据库软件名称
		System.out.println("db name: " + dbmd.getDatabaseProductName());
		// 是否支持事务
		System.out.println("tx: " + dbmd.supportsTransactions());
		// 释放资源
		conn.close();
	}

}
