package prac;

import java.util.*;

public class MyVector extends Vector {
	public MyVector() {
		super(1,1);
	}
	
	public void addInt(int i) {
		addElement(new Integer(i));
	}
	
	public void addFloat(float f) {
		addElement(new Float(f));
	}
	
	public void addString(String s) {
		addElement(s);
	}
	
	public void addChartArray(char a[]) {
		addElement(a);
	}
	
	public void printVector() {
		Object o;
		int length = size();
		
		System.out.println("Number of vector is "+ length + "and they are:");
		for(int i= 0; i < length; i++) {
			o = elementAt(i);
			if(o instanceof char[]) {
				//System.out.println(o); ²»ºÃ
				System.out.println(String.copyValueOf((char[]) o));
			}
			else
			System.out.println(o.toString());
		}
	}
	
	public static void main(String args[]) {
		MyVector v= new MyVector();
		int digit = 5;
		float real = 3.14f;
		char letters[] = {'a','b','c','d'};
		String s = new String ("Hi there!");
		
		v.addInt(digit);
		v.addFloat(real);
		v.addString(s);
		v.addChartArray(letters);
		
		v.printVector();
	}
}
