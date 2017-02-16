package classInitializationOrder;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException {
//		Class.forName("classInitializationOrder.B");
		new B();
	}

}
