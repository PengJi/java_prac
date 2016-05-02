package sybase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Conn2MySQL{
	public static String dbUser = null;
    public static String dbPasswd = null;
    public static String dbName = null;
    public static String dbTableName = null;
    public static String fs = null;
    
    public Conn2MySQL(String user,String passwd,String name,String tableName){
    	dbUser = user;
    	dbPasswd = passwd;
    	dbName = name;
    	dbTableName = tableName;
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
     * 得到数据库连接
     * @return 返回连接对象
     */
    public Connection getConn(){
    	Connection conn = null;
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		String url="jdbc:mysql://192.168.101.8:3306/" + dbName;    //JDBC的URL    
    		conn = DriverManager.getConnection(url,dbUser,dbPasswd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
    }

	/*
	 * 查询表
	 * @param tableName 表名
	 * @return
	 */
	public void selectTable(String tableName){
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql = "select * from " + tableName;    //要执行的SQL
	        ResultSet rs = stmt.executeQuery(sql);//创建数据对象
	        ResultSetMetaData metaData=rs.getMetaData(); 
			int columnSize = metaData.getColumnCount();//获取总的列数
	        while (rs.next()){
	        	for(int i=1;i<=columnSize;i++){
	        		System.out.print(rs.getString(i) + "\t");
	        	}
	        	System.out.println();
	        }
	        rs.close();
	        stmt.close();
	        conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 插入表记录和列记录
	 * @param nameStr 表名
	 * @param desStr 表的描述
	 * @return 返回插入table_infos表中记录的id
	 */
	public int insertTableCol(String nameStr,String sid,String desStr){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql2 = "INSERT INTO table_infos (name,sybid,description,created_at,updated_at) "
					+ "VALUES (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
			PreparedStatement pst = conn.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1,nameStr);
			pst.setString(2,sid);
			pst.setString(3,desStr);
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if(rs.next()){
				n = rs.getInt(1);
			}
			
			pst.close();
	        stmt.close();
	        conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	
	/*
	 * 插入一条属性信息
	 * @param attrName 属性名
	 * @param desStr 属性的描述
	 * @param pid 属性属于的表id
	 * @return 影响的行数
	 */
	public int insertAttr(String attrName,String desStr,int pid){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql2 = "INSERT INTO table_attributes (name, description,tid,created_at,updated_at) "
					+ "VALUES (?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
			PreparedStatement pst = conn.prepareStatement(sql2);
			pst.setString(1,attrName);
			pst.setString(2, desStr);
			pst.setInt(3, pid);
			n = pst.executeUpdate();
			
			pst.close();
	        stmt.close();
	        conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	/*
	 * 更新表
	 * @param tid 表的id
	 * @param nameStr 表名称
	 * @param desStr 表的描述
	 */
	public int updateTable(int tid,String nameStr,String desStr){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql2 = "update table_infos set name=?,description=? where id=?";
			PreparedStatement pst = conn.prepareStatement(sql2);
			pst.setString(1,nameStr);
			pst.setString(2, desStr);
			pst.setInt(3,tid);
			n = pst.executeUpdate();
			
			pst.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	
	/*
	 * 更新表的属性
	 * @param aid 属性id
	 * @param nameStr 属性名称
	 * @param desStr 属性描述
	 * @return 影响的函数
	 */
	public int updateAttr(int aid,String nameStr,String desStr){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql2 = "update table_attributes set name=?, description=? where id=?";
			PreparedStatement pst = conn.prepareStatement(sql2);
			pst.setString(1,nameStr);
			pst.setString(2, desStr);
			pst.setInt(3, aid);			
			n = pst.executeUpdate();
			
			pst.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	/*
	 * 删除表
	 * @param tid 表id
	 * @return 影响的函数
	 */
	public int deleteTable(int tid){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql3 = "delete from table_infos where id=?";
			PreparedStatement pst = conn.prepareStatement(sql3);
			pst = conn.prepareStatement(sql3);
			pst.setInt(1,tid);
			n = pst.executeUpdate();
			
			pst.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	
	/*
	 * 删除表的属性
	 * @param aid 表属性id
	 * @return 影响的函数
	 */
	public int deleteAttr(int aid){
		int n = 0;
		try {
			Connection conn = getConn();
			Statement stmt = conn.createStatement(); //创建Statement对象
			String sql3 = "delete from table_attributes where id=?";
			PreparedStatement pst = conn.prepareStatement(sql3);
			pst = conn.prepareStatement(sql3);
			pst.setInt(1,aid);
			n = pst.executeUpdate();
			
			pst.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	public static void main(String[] args){
        dbUser = "root";
        dbPasswd = "root";
        dbName = "test";
        int n = 0;
        
        dbTableName = "table_infos";
        Conn2MySQL conn2MySQL = new Conn2MySQL(dbUser, dbPasswd, dbName, dbTableName);
        conn2MySQL.selectTable(dbTableName);
        /*
        //插入一条表记录
        insertTable("test10","jdbc测试");
        selectTable(dbTableName);
        */
        /*
        //插入一条表属性
        n = insertAttr("属性1","jdbc测试属性",36);
        System.out.println(n);
        */
        /*
        updateTable(41,"test7","测试");
        updateAttr(337,"属性2","测试");
        deleteTable(42);
        deleteAttr(336);
        */
        
	}
}
