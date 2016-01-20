package prac;

public class PrintTriag {
	public static void main(String args[]) {
		int initLine = 10;
		int initNum = 10;
		PrintTriag pt = new PrintTriag();
		for(int i = 0; i < initLine ; i++) {
			for(int j = 0; j<initNum-i; j++) {
				pt.printAstar();
			}
			System.out.println();
		}
	}
	
	public void printAstar() {
		System.out.print("*");
	}
}
