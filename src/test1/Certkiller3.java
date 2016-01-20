package test1;

public class Certkiller3 implements Runnable{
	public void run(){
		System.out.print("running ");
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new Certkiller3());
		t.run();
		t.run();
		t.start();
	}
}
