package test;

public class Test1 {
	public static void main(String[] args) {
		System.out.println(new Test1().test());
	}
	
	static int test(){
		int x=1;
		try{
			//++x;
			return x;
		}finally{
			++x;
		}
	}
}
