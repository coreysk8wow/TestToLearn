package classInitializationOrder;

public class Counter {
	public static int seq = 0;
	
	public static int add(String from) {
		System.out.println(from + ":" + seq++);
		return seq;
	}
}
