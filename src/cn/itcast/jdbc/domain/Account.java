/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.domain;

/**
 * 41.使用模板方法设计模式处理DAO中的查询方法
 * @author qibie
 * @createDate:2018-06-10
 * @ProjectName:jdbc
 */
public class Account {
	private int id;
	private String name;
	private float money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}
}
