package sybase;

import java.sql.*;
import java.util.*;

public class ConnTest {
	public static void main(String[] args) {
		try {
			Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
			String url = "jdbc:sybase:Tds:192.168.100.120:5000/tempdb";// tempdb为数据库名
			Properties sysProps = System.getProperties();
			sysProps.put("user", "sa"); // 设置数据库访问用户名
			sysProps.put("password", "560128"); // 密码
			Connection conn = DriverManager.getConnection(url, sysProps);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from userInfo"; // userInfo为其中的一个表
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(2)); // 取得第二列的值
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
