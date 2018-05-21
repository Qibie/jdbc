/**
 * 
 */
package cn.itcast.jdbc;

/**
 * @author qibie
 * @createDate:2018-05-19
 * @ProjectName:jdbc
 */
public class Bean {
	private String name;
	
	public Bean() {
	}
	
	public Bean(String n) {
		this.name = n;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
