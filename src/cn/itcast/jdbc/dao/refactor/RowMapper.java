/**
 * Copyright © 2018 qibie Tech Ltd. All rights reserved.
 */
package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行映射接口
 * @author qibie
 * @createDate:2018-06-11
 * @ProjectName:jdbc
 */
public interface RowMapper {
	public Object mapRow(ResultSet rs) throws SQLException;
}
