package testException;

public class Test01 {

	public void number () throws ArithmeticException {
		int result;
		try {
			result = 1/0; // Exception will be catched.
		} catch (ArithmeticException e) {
			e.printStackTrace();
			System.out.println("@@@@@");
			result = 2/0; // Exception will be thrown.
		}
		
	}
	
	public static void main(String[] args) {
		new Test01().number();
	}
}
