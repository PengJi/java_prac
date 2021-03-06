package prac;

public class NumSort {
	public static void main(String[] args) {
		int[] a= new int[args.length];
		for(int i = 0; i<args.length; i++) {
			a[i] = Integer.parseInt(args[i]);               //对数组的输入
		}
		print(a);
		selectionSort(a);
		print(a);
	}
	
	private static void selectionSort(int[] a) {
		for(int i = 0; i<a.length; i++) {
			for(int j = i+1; j<a.length; j++) {
				if(a[i]>a[j]) {
					int tmp = a[i];
					a[i] = a[j];
					a[j] = tmp;
				}
			}
		}
	}
	
	private static void selectionSort1(int[] a) {
		int k,j,temp;
		for(int i = 0; i<a.length; i++) {
			k = i;
			for(j = k+1; j<a.length; j++) {
				if(a[j]<a[k]) {
					k = j;
				}
			}
			if(k != i) {
				temp = a[i];
				a[i] = a[j];
				a[j] = temp;
			}
		}
	}
	
	private static void print(int[] a) {
		for(int i = 0; i<a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}
}
