package classInitializationOrder;

public class B extends A {
	public static int staticCount = Counter.add("B.staticCount");
	
	static {
		Counter.add("B.staticBlock");
	}
	
	public int id = Counter.add("B.id");
	
	{
		Counter.add("B.instanceBlock");
	}
	
	public B() {
		Counter.add("B.constructor");
	}
	
}
