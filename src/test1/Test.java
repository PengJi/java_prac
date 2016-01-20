package test1;

import java.awt.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Test {
	public Test(){
		System.out.println("test");
	}
	public Test(String val){
		this();
		System.out.println("test with " + val);
	}
	
	void waitForSignal() throws InterruptedException{
		Object obj = new Object();
		synchronized (Thread.currentThread()) {
			obj.wait();
			obj.notify();
		}
	}
	
	public static void main(String[] args) {
		Test test = new Test("wow");
		
		Map<String, ?extends Collection<Integer>> map;
		map=new HashMap<>();
		//map=new HashMap<String,List<Integer>>();
		map=new HashMap<String,LinkedList<Integer>>();
		//map=new LinkedHashMap<Object,List<Integer>>();
		
		String[] elements = {"for","tea","too"};
		String first = (elements.length>0)?elements[0]:null;
		System.out.println(first);
		
		int i=1;
		while(i!=5){
			switch (i++ % 3) {
			case 0:
				System.out.print("A");
				break;
			case 1:
				System.out.print("B");
				break;
			case 2:
				System.out.print("C");
				break;
			}
		}
		
		System.out.println();
		
		String text = "Welcome to java contest";
		String[] words = text.split(" ");
		System.out.println(words.length);
		
		int m=3,n;
		outer:while(m>0){
			n=3;
			inner:while(n>0){
				if(n<2) break outer;
				System.out.println(n+ "and" + m);
				n--;
			}
			m--;
		}
		
		long n1=12_3_45__789;
		//long n2=__123_45_678_9;
		//int n3=0xFC_aB_C3_353;
		double n4=0b11001_001_0_0_11;
		//float n5=1.4_142_13;
		float n6=0_1_2_3;
	}//main
}
