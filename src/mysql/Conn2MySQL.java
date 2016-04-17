import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.sql.*;

public class Conn2MySQL{
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

	//查询表
	public static void selectTable(String tableName){
		
	}

	//插入记录
	public static void insertRec(){
	}

	//更新表
	public static void updateTable(){
	}

	//删除表	
	public static void deleteRec(){
	}

	public static void main(String[] args){
	}
}
