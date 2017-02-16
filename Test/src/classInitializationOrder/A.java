package classInitializationOrder;

public class A {
	
	static {
		Counter.add("A.staticBlock");
	}
	
	public static int staticCount = Counter.add("A.staticCount");
	
	public static int a = getN();
	
	public static int getN() {
		return 1;
	}
			
	public int id = Counter.add("A.id");
	
	{
		Counter.add("A.instanceBlock");
	}
	
	public A() {
		Counter.add("A.constructor");
	}
	
}
