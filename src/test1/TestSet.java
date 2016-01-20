package test1;
import java.util.*;

public class TestSet {
	enum Num{ONE,THREE,TWO}
	public static void main(String[] args) {
		Collection coll = new LinkedList();
		coll.add(Num.THREE);
		coll.add(Num.ONE);
		coll.add(Num.THREE);
		coll.add(Num.TWO);
		coll.add(Num.TWO);
		Set set = new HashSet(coll);
		System.out.println(set);
	}
}