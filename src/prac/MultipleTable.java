package prac;

public class MultipleTable {
	public static void main(String args[]) {
		MultipleTable mt = new MultipleTable();
		
		int initNum = 9;
		int res = 0;
		for(int i = 1; i <= initNum; i++) {
			for(int j = 1; j <=i; j++) {
				res = i*j;
				mt.printFormula(i,j,res);
			}
			System.out.println();
		}
	}
	
	public void printFormula(int i,int j,int res) {
		System.out.print(i + "*" + j + "=" + res + " ");
	}
}