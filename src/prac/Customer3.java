package prac;

class Customer3 {
	public static void main(String[] args) {
		Customer3 customer = new Customer3();
		{
			String name = "Tom";
			customer.name = name;
			System.out.println("The customer'name:" + customer.name);
		}
		String name = "John";
		customer.name =name;
		System.out.println("The customer'name:" + customer.name);
	}
	private String name;
}
