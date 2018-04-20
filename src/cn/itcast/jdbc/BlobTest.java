package cn.itcast.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Blob;

/**
 * 
 * @author HASEE
 *
 */
public class BlobTest {

	public static void main(String[] args) throws SQLException, IOException {
//		create();
		read();
	}
	static void read() throws SQLException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			st = conn.createStatement();

			// 4.执行语句
			rs = st.executeQuery("select big_bit  from blob_test");
			while(rs.next()) {
//				Blob blob = rs.getBlob(1);
//				InputStream in = blob.getBinaryStream();
				//此处的1是第几列，也可以用列名替代"big_bit"
//				InputStream in = rs.getBinaryStream(1);
				InputStream in = rs.getBinaryStream("big_bit");
				File file = new File("IMG_0002_bak.jpg");
				OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				byte[] buff = new byte[1024];
				for(int i = 0; (i = in.read(buff)) > 0;) {
					out.write(buff, 0, i);
				}
				out.close();
				in.close();
			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}
	
	static void create() throws SQLException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// 3.创建语句
			String sql = "insert into blob_test(big_bit) values (?)";
			ps = conn.prepareStatement(sql);
			File file = new File("IMG_0002.jpg");
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			// 此处的1是上面？的占位符
			ps.setBinaryStream(1, in, (int) file.length());
			// 4.执行语句
			int i = ps.executeUpdate();
			// IO流关闭资源
			in.close();
			System.out.println("i=" + i);
		} finally {
			// 5.释放资源
			JdbcUtils.free(rs, ps, conn);
		}
	}
}