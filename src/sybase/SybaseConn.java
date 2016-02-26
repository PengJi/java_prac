import java.sql.*; // JDBC
import com.sybase.jdbc.*; // Sybase jConnect
import java.util.Properties; // Properties
import sybase.sql.*; // Sybase utilities
import asademo.*; // Example classes

/*
 * http://infocenter.sybase.com/archive/index.jsp?topic=/com.sybase.infocenter.dc00170.1260/html/iqapg/iqapg917.htm
 */

public class SybaseConn {
	private static Connection conn;

	public static void main(String args[]) {

		conn = null;
		String machineName;
		if (args.length != 1) {
			machineName = "localhost";
		} else {
			machineName = new String(args[0]);
		}

		ASAConnect("sa", "560128", "192.168.100.120");
		if (conn != null) {
			System.out.println("Connection successful");
		} else {
			System.out.println("Connection failed");
		}

		try {
			serializeVariable();
			serializeColumn();
			serializeColumnCastClass();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void ASAConnect(String UserID, String Password, String Machinename) {
		// uses global Connection variable

		String _coninfo = new String(Machinename);

		Properties _props = new Properties();
		_props.put("user", UserID);
		_props.put("password", Password);

		// Load the Sybase Driver
		try {
			Class.forName("com.sybase.jdbc.SybDriver").newInstance();

			StringBuffer temp = new StringBuffer();
			// Use the Sybase jConnect driver...
			temp.append("jdbc:sybase:Tds:");
			// to connect to the supplied machine name...
			temp.append(_coninfo);
			// on the default port number for ASA...
			temp.append(":2638");
			// and connect.
			System.out.println(temp.toString());
			conn = DriverManager.getConnection(temp.toString(), _props);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
