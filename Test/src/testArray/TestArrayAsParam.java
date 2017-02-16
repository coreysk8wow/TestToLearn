package testArray;

public class TestArrayAsParam {

	public void read(byte[] data) {
		System.out.println(data[0]);
		data[0] = 0;
		System.out.println(data[0]);
	}
	
	public static void main(String[] args) {
		byte[] data = new byte[]{1,2,3};
		new TestArrayAsParam().read(data);
		System.out.println(data[0]);
		
	}
/*
 * Conclusion:
 * In Java, pass an array as parameter, it's passing by reference, not passing by value.
 * This means, you can modify the the original variable -- the argument, through the parameter.
 */
	
}
