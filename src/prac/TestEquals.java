package prac;

public class TestEquals {
	public static void main(String arg[]) {
		Cat1 c1 = new Cat1(1,2,3);
		Cat1 c2 = new Cat1(1,2,3);
		System.out.println(c1 == c2);
		System.out.println(c1.equals(c2));					//�Ƚ����������Ƿ����
		
		String s1 = new String("hello");         
		String s2 = new String("hello");
		System.out.println(s1.equals(s2));					//�Ƚ������ַ����Ƿ����
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
	
	public boolean equals(Object obj) {						//�Ƚ����������Ƿ����
		if(obj == null) return false;
		else {
			if(obj instanceof Cat1) {
				Cat1 c = (Cat1)obj;
				if(c.color == this.color && c.height == this.height && c.weight == this.weight)      //�е㲻���ף�         
					return true;
			}
		}
		return false;
	}	
}
