import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class UpdateDeleteDemo {
	public static void main(String[] args)throws Exception{
	try{
		//调用Class.forName()方法加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		String url="jdbc:mysql://localhost:3306/employees";    //JDBC的URL    
		Connection conn;
		conn = DriverManager.getConnection(url,"root","root");
		Statement stmt = conn.createStatement(); //创建Statement对象
		System.out.println("成功连接到数据库！");

		//查询数据的代码
		String sql = "select * from departments";    //要执行的SQL
		ResultSet rs = stmt.executeQuery(sql);//创建数据对象
		System.out.println("编号"+"\t"+"姓名"+"\t"+"年龄");
		while (rs.next()){
			System.out.print(rs.getInt(1) + "\t");
			System.out.print(rs.getString(2) + "\t");
			System.out.print(rs.getInt(3) + "\t");
			System.out.println();
		}
            
		//修改数据的代码
		String sql2 = "update stu set name=? where number=?";
		PreparedStatement pst = conn.prepareStatement(sql2);
		pst.setString(1,"8888");
		pst.setInt(2,198);
		pst.executeUpdate();
            
		//删除数据的代码
		String sql3 = "delete from stu where number=?";
		pst = conn.prepareStatement(sql3);
		pst.setInt(1,701);
		pst.executeUpdate();

		ResultSet rs2 = stmt.executeQuery(sql);//创建数据对象
		System.out.println("编号"+"\t"+"姓名"+"\t"+"年龄");
		while (rs.next()){
			System.out.print(rs2.getInt(1) + "\t");
			System.out.print(rs2.getString(2) + "\t");
			System.out.print(rs2.getInt(3) + "\t");
			System.out.println();
		}
            
		rs.close();
		stmt.close();
		conn.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}
