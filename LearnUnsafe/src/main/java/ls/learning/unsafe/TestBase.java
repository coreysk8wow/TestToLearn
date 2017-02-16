package ls.learning.unsafe;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sun.misc.Unsafe;

public class TestBase {
	private void testAvoidingObjectInit() throws InstantiationException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		A a1 = new A();
		System.out.println(a1.a());
		
		A a2 = A.class.newInstance();
		System.out.println(a2.a());
		
		A a3 = (A) getUnsafe().allocateInstance(A.class);
		System.out.println(a3.a());
	}
	
	private void testOffHeapMemory() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		final byte[] input = "你好啊 Block current thread, returning when a balancing unpark occurs, 你好不安全的 or a balancing unpark has already occurred, or the thread is interrupted, or, if not absolute and time is not zero, the given time nanoseconds have elapsed, or if absolute, the given deadline in milliseconds since Epoch has passed, or spuriously (i.e., returning for no 'reason'). Note: This operation is in the Unsafe class only because unpark is, so it would be strange to place it elsewhere.".getBytes("UTF-8");
		final long[] addressArr = new long[input.length];
		try {
			for (byte b : input) {
				System.out.print(b);
			}
			System.out.println();
			
			long address = 0;
			for (int i = 0; i < input.length; i++) {
				addressArr[i] = getUnsafe().allocateMemory(1);
				getUnsafe().putByte(addressArr[i], input[i]);
			}
			
			byte[] output = new byte[input.length];
			long addr = 0;
			for (int i = 0; i < input.length; i++) {  
				output[i] = getUnsafe().getByte(addressArr[i]);
			}
			
			for (byte b : output) {
				System.out.print(b);
			}
			System.out.println();
			
			int failCount = 0;
			for (int i = 0; i < output.length; i++) {
				if (output[i] != input[i]) {
					failCount++;
				}
			}
			System.out.println("Failed num: " + failCount);
			
			System.out.println(new String(output, "UTF-8"));
		}
		finally {
			/*for (long addr : addressArr) {
				getUnsafe().freeMemory(addr);
			}*/
		}
		
	}
	
	private void exhaustRam() {
		ExecutorService exec = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			exec.submit(new Runnable() {
				@Override
				public void run() {
						while (true) {
							try {
								long addr = getUnsafe().allocateMemory(1);
								getUnsafe().putByte(addr, (byte) 99);
							}
							catch (Exception e) {
							}
						}
				}
			});
		}
	}
	
	private static Unsafe getUnsafe() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		Unsafe unsafe = (Unsafe) f.get(null);
		
		return unsafe;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, UnsupportedEncodingException {
		TestBase base = new TestBase();
//		base.testAvoidingObjectInit();
		base.testOffHeapMemory();
		
		
	}
	
}

