/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.Account;

/**
 * 41.使用模板方法设计模式处理DAO中的查询方法
 * @author qibie
 * @createDate:2018-06-10
 * @ProjectName:jdbc
 */
public class AccountDaoImpl extends AbstractDao {

	public Account findAccount(int id) {
		String sql = "select id, name, money from account where id = ?";
		Object[] args = new Object[] { id };
		Object account = super.find(sql, args);
		return (Account) account;
	}

	@Override
	protected Object rowMapper(ResultSet rs) throws SQLException {
		Account a = new Account();
		a.setId(rs.getInt("id"));
		// ...
		return a;
	}

}
