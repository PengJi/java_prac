package test1;

public class HowToSync {
	private int x;
	public void puzzle(){
		//x=((x==1)?1:0);
		
		int current=x;
		x=current+1;
		System.out.print(x+"-");
	}
	public void go(){
		for(int i=0;i<5;i++){
			new Thread(){
				public void run(){
					puzzle();
				}
			}.start();
		}
	}
	public static void main(String[] args) {
		new HowToSync().go();
	}

}
