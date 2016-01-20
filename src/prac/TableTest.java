package prac;

public class TableTest {
	public static void main(String args[]) {
		int myTable[][]  = {{23,63,84,24,74,65,23},
										 {23,63,73,74,34,74,23},
										 {56,13,73,74,24,85,34},
										{65,34,95,45,45,86,24}};
		int sum,max,maxRow = 0;
		max = 0;							//assume all numbers are positive
		for(int row = 0; row < 4; row++) {					//每一行分别比较
			sum = 0;
			for(int col = 0; col < 7; col++)
			sum += myTable[row][col];
			if(sum > max) {
				max = sum;
				maxRow = row;
			}
		}
		System.out.println("Row" + maxRow + "has the hight sum" + max);
	}
}
