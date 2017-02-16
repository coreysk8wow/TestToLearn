package testException;

public class TestException {

	private String catchException() {
		Integer n = null;
		try {
//			n.byteValue();
			throwsException();
			System.out.println("after throwsException()");
		}
		catch (NullPointerException npe) {
			System.out.println("NullPointerException");
//			throw new NullPointerException();
		}
		finally {
			System.out.println("finally");
		}
		System.out.println("after try-catch block");
		return "return";
	}
	
	private void throwsException() throws NullPointerException {
		Integer n = null;
		n.byteValue();
		System.out.println("after n.byteValue()");
	}
	
	public static void main(String[] args) {
		System.out.println(new TestException().catchException());
	}

}
