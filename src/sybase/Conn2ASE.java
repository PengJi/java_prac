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
    public static ArrayList<String> arrayList = new ArrayList<String>();
	
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

	/*
	 * 获得连接对象
	 * @return 连接对象
	 */
	public static Connection getConn(){
		Connection conn = null;
		try {
			Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
			String url = "jdbc:sybase:Tds:192.168.101.62:5000/" + dbName;
			Properties sysProps = System.getProperties();
			sysProps.put("user", dbUser); 
			sysProps.put("password", dbPasswd);
			conn = DriverManager.getConnection(url, sysProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
	 * 查询表的内容
	 * @param tableName 表名
	 * @return 字符串，表的内容
	 */
	public static String getTable(String tableName){
		String tables = "";
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + tableName; //查询表
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData=rs.getMetaData(); 
			int columnSize = metaData.getColumnCount();//获取总的列数
			String str = "";
			while(rs.next()){
				if(rs.getString(4).trim().equals("U")){
					for(int i=1;i<=columnSize;i++){
						str = str + rs.getString(i) + "\t";
					}
					tables = tables + str + "\n";
					str = "";
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tables;
	}
	
	/*
	 * 查询表的内容
	 * @param tableName 表名
	 * @return 字符串，表的内容
	 */
	public static String getTableSpec(String tableName){
		String tables = "";
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + tableName; //查询表
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData=rs.getMetaData(); 
			int columnSize = metaData.getColumnCount();//获取总的列数
			String str = "";
			while(rs.next()){
				if(rs.getString(4).trim().equals("U")){
					str = str + rs.getString(1) + "\t" + rs.getString(2) + "\t";
					arrayList.add(str);
					tables = tables + str + "\n";
					str = "";
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tables;
	}
	
	/*
	 * 查询表的字段
	 * @param tableName 表名
	 * @return 表字段
	 */
	public static String getTableColumns(String tableName){
		String tables = "";
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + tableName; //查询表
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData=rs.getMetaData(); 
			int columnSize = metaData.getColumnCount();//获取总的列数
			String str = "";
			while(rs.next()){
				for(int i=1;i<=columnSize;i++){
					str = str + rs.getString(i) + "\t";
				}
				tables = tables + str + "\n";
				str = "";
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tables;
	}

	/*
	 * 查询表备注
	 * @param tableName 表名
	 * @return 表备注
	 */
	public static String getTableComments(String tableName){
		String tables = "";
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from " + tableName; //查询表
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData=rs.getMetaData(); 
			int columnSize = metaData.getColumnCount();//获取总的列数
			String str = "";
			while(rs.next()){
				for(int i=1;i<=columnSize;i++){
					str = str + rs.getString(i) + "\t";
				}
				tables = tables + str + "\n";
				str = "";
			}
			rs.close();
			stmt.close();
			conn.close();
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
        dbName = "test";
        String str;
        String[] strs = new String[2];

        dbTableName = "sysobjects";
        fs = "/home/sybase/java_prac/src/sybase/tables.txt";
		//得到表信息
        String tableStr = getTable(dbTableName);
        write(fs,tableStr);
        read(fs);
        
        dbTableName = "sysobjects";
        fs = "/home/sybase/java_prac/src/sybase/tablesspec.txt";
		//得到表信息
        String tableStrSpec = getTableSpec(dbTableName);
        write(fs,tableStrSpec);
        read(fs);
        //写入mysql
        Conn2MySQL conn2MySQL = new Conn2MySQL("root","root","test","table_infos");
        Iterator<String> iter = arrayList.iterator();
        while(iter.hasNext()){
        	str = iter.next();
        	strs = str.split("\\t");
			System.out.println(strs[0]);
	        conn2MySQL.insertTable(strs[0], strs[1], null);
        }

		dbTableName = "syscolumns";
		fs = "/home/sybase/java_prac/src/sybase/tableColumns.txt";
		//得到表字段
		String tableColumns = getTableColumns(dbTableName);
		write(fs,tableColumns);
		//read(fs);

		dbTableName = "syscomments";
		fs = "/home/sybase/java_prac/src/sybase/tableComments.txt";
		//得到表字段
		String tableComments = getTableComments(dbTableName);
		write(fs,tableColumns);
		//read(fs);
    }
}
