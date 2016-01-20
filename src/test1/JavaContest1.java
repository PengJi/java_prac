package test1;

public class JavaContest1 {
	public static void fun(short n){
		System.out.println("short");
	}
	public static void fun(Short n){
		System.out.println("SHORT");
	}
	public static void fun(Long n){
		System.out.println("LONG");
	}
	public static void main(String[] args) {
		Short y = 0;
		int z=y;
		fun(y);
		//fun(z);
	}
}
