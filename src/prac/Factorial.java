package prac;

import java.util.*;

public class Factorial {
	public static void main(String[] args) {
		Factorial ff = new Factorial();
		for(int i = 0; i<5; i++)
		{
			ff.setInitVal(2*(i+1));
			ff.result = Factorial(ff.initVal);
			ff.print();
		}
	}
	
	public static int Factorial(int n) {					//µÝ¹é·½·¨
		if(n == 0) return 1;
		return n * Factorial(n-1);
	}
	
	public void print() {
		System.out.println(initVal + "!=" + result);
	}
	
	public void setInitVal(int n) {
		initVal = n;
	}
	
	private int result,initVal;
}