package prac;

import java.util.*;

class Factorial2 {
	private int result,initVal;
	public static void main(String args[]) {
		Factorial2 ff = new Factorial2();
		for(int i = 0; i < 5; i++) {
			ff.setInitVal(2*(i+1));							//2¡¢4¡¢6¡¢8¡¢10
			ff.result = 1;
			for(int j = 2; j <=ff.initVal; j++) {					//¼ÆËã½×³Ë
				ff.result *= j;
				ff.print();
			}
		}
	}
	
	public void print() {
		System.out.println(initVal + "!=" + result);
	}
	
	public void setInitVal(int n) {
		initVal = n;
	}
}
