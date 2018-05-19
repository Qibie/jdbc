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
	public Bean(String n) {
		this.name = n;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
