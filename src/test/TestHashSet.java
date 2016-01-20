package test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestHashSet {

	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		//Set<String> set = new HashSet<String>();
		set.add("one");
		set.add("two");
		set.add("three");

		Iterator it = set.iterator();

		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	//InputStream
	//outputStream
	//InputStreamReader
	//OutputStreamWriter

}
