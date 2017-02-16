package testInteger;

public class TestInteger {

	
	public static void main(String[] args) {
		Integer n = 15;
		System.out.println(Integer.toString(n, 10));
		System.out.println(Integer.toString(n, 16));
		System.out.println(Integer.toString(n, 8));
		System.out.println(Integer.toString(n, 2));
		
		System.out.println(Integer.toString(n));
		System.out.println(Integer.toHexString(n));
		System.out.println(Integer.toOctalString(n));
		System.out.println(Integer.toBinaryString(n));
	}

}
