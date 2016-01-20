package test;

import java.lang.Throwable;
import java.util.StringTokenizer;

public class Outer {
	int out_x=0;
	public void method(){
		Inner1 inner1 = new Inner1();
		class Inner2{//在方法体内部定义的内部类
			public void method(){
				out_x=3;
			}
		}
		Inner2 inner2 = new Inner2();
	}
	public class Inner1{//在方法体外部定义的内部类
	}
	
	//实现线程
	public void start(){
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					}
						
				}
				).start();
	}
	
	//synchronized
	//Thread
	//Runnable
	
	public static void main(String[] args) {
		Outer outer=new Outer();
		Outer.Inner1 inner1 = outer.new Inner1();
		
		//创建一个StringBuffer对象
		StringBuffer sbf = new StringBuffer();
		for(int i=0;i<100;i++){
			sbf.append(i);
		}
		//创建了101个对象
		String str = new String();
		for(int i=0;i<100;i++){
			str = str+i;
		}
		
		String orgString = "Hello,World";
		String[] resultStrings = orgString.split(",");
	}
	
	//ArrayStoreException、BufferOverflowException、SystemException
}
