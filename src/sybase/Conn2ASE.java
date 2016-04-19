import java.sql.*; 
import java.util.*;
import java.util.jar.Attributes.Name;

import javax.naming.spi.DirStateFactory.Result;

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
	public static String fs = null;
    public static ArrayList<String> arrayList = new ArrayList<String>();
    
    public Conn2ASE(String user,String passwd,String name){
    	dbUser = user;
    	dbPasswd = passwd;
    	dbName = name;
    }
	
	//写文件
	public void write(String filepath,String content){
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
	public void read(String filepath){
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
	public Connection getConn(){
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
	 * 查询表的总内容
	 * @param tableName 表名
	 * @return 字符串，表的内容
	 */
	public String getTable(String tableName){
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
	 * 查询表的特定列内容
	 * @param tableName 表名
	 * @return 字符串，表的内容
	 */
	public String getTableSpec(String tableName){
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
	 * 向table_infos插入表信息
	 * @return 返回影响的行数
	 */
	public int insertTableSpec(){
		int n=0;
		String str,sql,nameStr;
		int sid = 0;
        String dbTableName = "sysobjects";
        String[] strs = new String[2];
        fs = "/home/sybase/java_prac/src/sybase/tablesspec.txt";
        String tableStrSpec = getTableSpec(dbTableName);
        write(fs,tableStrSpec);
        read(fs);
        //写入mysql
        Conn2MySQL conn2MySQL = new Conn2MySQL("root","root","test","table_infos");
        Iterator<String> iter = arrayList.iterator();
        try {
    		Connection conn = getConn();
    		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            while(iter.hasNext()){
            	str = iter.next();
            	strs = str.split("\\t");
            	n = conn2MySQL.insertTable(strs[0], strs[1], null);
            	sql = "select name from syscolumns where id = " + strs[1];
            	ResultSet rs = stmt.executeQuery(sql);
            	ResultSet keyrs = stmt.getGeneratedKeys();
            	if(keyrs.next()){
            		sid = keyrs.getInt(1);
            	}
            	while(rs.next()){
            		nameStr = rs.getString(1);
            		conn2MySQL.insertAttr(nameStr, null, sid );
            	}
            	if(n == 0) break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return n;
	}
	
	/*
	 * 查询表的所有字段
	 * @param tableName 表名
	 * @return 表字段
	 */
	public String getTableColumns(String tableName){
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
	public String getTableComments(String tableName){
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
    	String dbTableName;
    	String fs;
        Conn2ASE conn2ase = new Conn2ASE("sa", "jipeng1008", "test");

		//得到表信息
        dbTableName = "sysobjects";
        fs = "/home/sybase/java_prac/src/sybase/tables.txt";
        String tableStr = conn2ase.getTable(dbTableName);
        conn2ase.write(fs,tableStr);
        conn2ase.read(fs);

		//得到表字段
		dbTableName = "syscolumns";
		fs = "/home/sybase/java_prac/src/sybase/tableColumns.txt";
		String tableColumns = conn2ase.getTableColumns(dbTableName);
		conn2ase.write(fs,tableColumns);
		//conn2ase.read(fs);

		//得到表字段
		dbTableName = "syscomments";
		fs = "/home/sybase/java_prac/src/sybase/tableComments.txt";
		String tableComments = conn2ase.getTableComments(dbTableName);
		conn2ase.write(fs,tableColumns);
		//conn2ase.read(fs);
		
		//插入表数据
		conn2ase.insertTableSpec();
    }
}
