package sybase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.sybase.jdbcx.SybDriver;

public class T1 {

	/**
	 * T1.java
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			SybDriver sybDriver = (SybDriver) Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance();
			sybDriver.setVersion(com.sybase.jdbcx.SybDriver.VERSION_5);
			DriverManager.registerDriver(sybDriver);
			// Class.forName("com.sybase.jdbcx.SybDriver").newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		//String url = " jdbc:sybase:Tds:192.168.100.120:5000/master";
		String url = " jdbc:sybase:Tds:192.168.100.120:5000/master";
		Properties sysProps = System.getProperties();
		sysProps.put("user", "sa");
		sysProps.put("password", "560128");
		try {
			//Connection conn = DriverManager.getConnection(url, sysProps);
			Connection conn = DriverManager.getConnection(url, "sa","560128");
			Statement stmt = null;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from spt_datatype_info");
			System.out.println("<-");

			while (rs.next())
				System.out.println(rs.getString(1) + "-" + rs.getString(2));
			System.out.println("->");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
