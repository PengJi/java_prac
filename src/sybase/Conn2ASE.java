import java.sql.*; 
import java.util.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;

public class Conn2ASE {
	public static String dbUser = null;
	public static String dbPasswd = null;
	public static String dbName = null;
	public static String dbTableName = null;
	public static String fs = null;
	
	//写文件
	public static void write(String filepath,String content){
		FileOutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		File file = new File(filepath);
		
		try {
			outputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			bufferedOutputStream.write(content.getBytes());			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//读文件
	public static void read(String filepath){
		BufferedInputStream bufferedInputStream = null;
		StringBuffer stringBuffer = new StringBuffer();
		File file = new File(filepath);
		
		try {
			int len = 512;
			byte[] bytes = new byte[len];
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			while((len = bufferedInputStream.read(bytes,0,len)) != -1){
				if(len < 512){
					byte[] tmpBuf = new byte[len];
					System.arraycopy(bytes, 0, tmpBuf, 0, len);
					stringBuffer.append(new String(tmpBuf));
					tmpBuf = null;
				}else{
					stringBuffer.append(new String(bytes));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				bufferedInputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		System.out.println(stringBuffer);
	}
	
	//得到表
	public static String getTable(String user,String passwd,String dbString,String tableName){
		String tables = "";
		try {
			Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
			String url = "jdbc:sybase:Tds:192.168.101.62:5000/" + dbString;
			Properties sysProps = System.getProperties();
			sysProps.put("user", user); 
			sysProps.put("password", passwd);
			Connection conn = DriverManager.getConnection(url, sysProps);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + tableName; //查询表
			ResultSet rs = stmt.executeQuery(sql);
			String str = "";
			int i = 1;
			while (rs.next()) {
				while(rs.getString(i) != null){
					str = str + rs.getString(i) + " ";
					i++;
				}
				//tables = tables + "oject_id:" + rs.getString(1) + ",oject_name:" + rs.getString(2) + "\n";
				tables = tables + str + "\n";
				str = "";
				i=1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tables;
	}
	
    public static void main(String[] args) {
    	/*
        try {
            Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
            String url = "jdbc:sybase:Tds:192.168.101.62:5000/master";// 数据库名
            Properties sysProps = System.getProperties();
            sysProps.put("user", "sa"); // 设置数据库访问用户名
            sysProps.put("password", "jipeng1008"); // 密码
            Connection conn = DriverManager.getConnection(url, sysProps);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql = "select id,name,crdate from dbo.sysobjects where type='U'"; // 表
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
				System.out.println("oject_id:"+rs.getString(1)+",oject_name:"+rs.getString(2)); // 取得第二列的值
            }
			rs.close();
			stmt.close();
			conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        */
        
        dbUser = "sa";
        dbPasswd = "jipeng1008";
        dbName = "sybsystemdb";
        dbTableName = "sysobjects";
        fs = "/home/sybase/test.txt";
        String tableStr = getTable(dbUser, dbPasswd, dbName, dbTableName);
	//System.out.println(tableStr);
        write(fs,tableStr);
        read(fs);
    }
}
