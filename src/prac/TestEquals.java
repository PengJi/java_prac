package prac;

public class TestEquals {
	public static void main(String arg[]) {
		Cat1 c1 = new Cat1(1,2,3);
		Cat1 c2 = new Cat1(1,2,3);
		System.out.println(c1 == c2);
		System.out.println(c1.equals(c2));					//比较两个对象是否相等
		
		String s1 = new String("hello");         
		String s2 = new String("hello");
		System.out.println(s1.equals(s2));					//比较两个字符串是否相等
	} 
}

class Cat1 {
	int color;
	int height;
	int weight;
	
	public Cat1(int color,int  height,int  weight) {
		this.color = color;
		this.height = height;
		this.weight = weight;
	}
	
	public boolean equals(Object obj) {						//比较两个对象是否相等
		if(obj == null) return false;
		else {
			if(obj instanceof Cat1) {
				Cat1 c = (Cat1)obj;
				if(c.color == this.color && c.height == this.height && c.weight == this.weight)      //有点不明白？         
					return true;
			}
		}
		return false;
	}	
}
