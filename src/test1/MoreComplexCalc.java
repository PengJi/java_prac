package test1;

public class MoreComplexCalc extends ComplexCalc{
	public void calc(){
		value -= 2;
	}
	public void calc(int multi){
		calc();
		super.calc();
		value *= multi;
	}
	public static void main(String[] args){
		MoreComplexCalc calc = new MoreComplexCalc();
		calc.calc(3);
		System.out.println("Oh it is " + calc.value);
	}
}
