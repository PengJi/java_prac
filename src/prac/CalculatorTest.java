package prac;

class Calculator {
	public static double calculatorAverage(int[] numbers) {
		int sum = 0;
		for(int i = 0 ; i<numbers.length; i++) {
		sum += numbers[i];
		}
		return sum/(double)numbers.length;
	}
	
	public static int findMax(int[] numbers) {						//去掉static,则对于下面的System函数，无法从静态上下文中引用非静态方法
		int max = numbers[0];
		for(int i = 0; i < numbers.length; i++) {
			if(numbers[i] > max)
			max = numbers[i];
		}
		return max;
	}
}
	
public class CalculatorTest {
		public static void main(String[] args) {									//内部类不能有静态方法
			int numbers[] = {23,56,76,56,12,85,13,84,99};
			System.out.println("The average is" + Calculator.calculatorAverage(numbers));
			System.out.println("The maximum is" + Calculator.findMax(numbers));
	}
}