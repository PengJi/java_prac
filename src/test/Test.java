package test;

import org.omg.CORBA.PUBLIC_MEMBER;

public class Test {
	public static int staticVar=0;
	public int instanceVar=0;
	public Test(){
		staticVar++;
		instanceVar++;
		System.out.println("staticVar="+staticVar+",instanceVar="+instanceVar);
	}
	public static void main(String[] args) {

		// 跳出多重循环第一种方法
		ok: for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; i++) {
				System.out.println("i=" + i + ",j=" + j);
				if (j == 5)
					break ok;
			}
		}
		// 跳出多重循环第二种方法
		boolean found = false;
		for (int i = 0; i < 10 && !found; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.println("i=" + i + ",j=" + j);
				if (j == 5) {
					found = true;
					break;
				}
			}
		}
	}
}
