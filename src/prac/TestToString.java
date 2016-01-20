package prac;

public class TestToString {
	public static void main(String[] args) {
		Dog2 d = new Dog2();
		System.out.println("d=" + d.toString());
	}
}

class Dog2 {
	public String toString() {
		return "I am a cool dog";
	}
}
