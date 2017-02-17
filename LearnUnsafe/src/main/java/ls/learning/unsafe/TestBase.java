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
		final byte[] input = "123456789".getBytes("UTF-8");
		final long[] addressArr = new long[input.length];
		final Unsafe unsafe = getUnsafe();
		try {
			for (byte b : input) {
				System.out.print(b);
			}
			System.out.println();
			
			for (int i = 0; i < input.length; i++) {
				addressArr[i] = getUnsafe().allocateMemory(10);
				System.out.println("----------" + i);
				long addr = addressArr[i];
				for (int j = 0; j < 10; j++) {
					System.out.println(addr);
					unsafe.setMemory(addr, 10L, (byte) 0); 
					unsafe.putByte(addr, input[i]);
					addr += normalize(8);
				}
			}
			
			byte[] output = new byte[input.length];
			for (int i = 0; i < input.length; i++) {
				long addr = addressArr[i];
				System.out.println("++++++++" + i);
				for (int j = 0; j < 10; j++) {
					System.out.println(addr);
					output[i] = unsafe.getByte(addr);
					addr += normalize(8);
				}
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
	
	private void testOffHeapMemory2() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		final String input = "123456789";
		final Unsafe unsafe = getUnsafe();
		try {
			System.out.println(input);
			
			long size = sizeOf(input);
		    long start = toAddress(input);
		    long address = getUnsafe().allocateMemory(size);
		    getUnsafe().copyMemory(start, address, size);
		    
		    String output = (String) fromAddress(address);
		    
			
			System.out.println(output);
		}
		finally {
			/*for (long addr : addressArr) {
				getUnsafe().freeMemory(addr);
			}*/
		}
		
	}
	
	private void testOffHeapMemory3() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
//		final byte[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		final String inputStr = "你好，不安全的！private void testOffHeapMemory3() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {";
		final byte[] input = inputStr.getBytes();

		System.out.println(inputStr);
		for (byte b : input) {
			System.out.print(b + ", ");
		}
		System.out.println();
		
		final Unsafe unsafe = getUnsafe();
		try {
			final long blockAddr = unsafe.allocateMemory(input.length);
			unsafe.setMemory(blockAddr, input.length, (byte) 0);
			long addr = blockAddr;
			for (int i = 0; i < input.length; i++) {
				unsafe.putByte(addr, input[i]);
				addr += normalize(1);
			}
			
			final byte[] output = new byte[input.length];
			addr = blockAddr;
			for (int i = 0; i < output.length; i++) {
				output[i] = unsafe.getByte(addr);
				addr += normalize(1);
			}
			
			for (byte b : output) {
				System.out.print(b + ", ");
			}
			System.out.println();
			System.out.println(new String(output));
			
			int failCount = 0;
			for (int i = 0; i < output.length; i++) {
				if (output[i] != input[i]) {
					failCount++;
				}
			}
			System.out.println("Failed num: " + failCount);
		}
		finally {
		}
	}
	
	/*
	 * Allocate Memory one by one
	 */
	private void testOffHeapMemory4() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
//		final byte[] input = {(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9};
		final byte[] input = "private void testOffHeapMemory4() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {"
				.getBytes();
		final long[] addressArr = new long[input.length];
		final Unsafe unsafe = getUnsafe();
		try {
			System.out.println(new String(input));
			for (byte b : input) {
				System.out.print(b + ", ");
			}
			System.out.println();
			
			for (int i = 0; i < input.length; i++) {
				addressArr[i] = getUnsafe().allocateMemory(1);
				System.out.println("-----write-----" + i);
				long addr = addressArr[i];
				System.out.println(addr);
				unsafe.setMemory(addr, 1L, (byte) 0); 
				unsafe.putByte(addr, input[i]);
			}
			
			byte[] output = new byte[input.length];
			for (int i = 0; i < input.length; i++) {
				long addr = addressArr[i];
				System.out.println("+++++read+++" + i);
				System.out.println(addr);
				output[i] = unsafe.getByte(addr);
			}
			
			System.out.println(new String(output));
			for (byte b : output) {
				System.out.print(b + ", ");
			}
			System.out.println();
			
			int failCount = 0;
			for (int i = 0; i < output.length; i++) {
				if (output[i] != input[i]) {
					failCount++;
				}
			}
			System.out.println("Failed num: " + failCount);
		}
		finally {
			/*for (long addr : addressArr) {
				getUnsafe().freeMemory(addr);
			}*/
		}
		
	}
	
	private void testOffHeapMemory_copyMemory_1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final String inputStr = "private void testOffHeapMemory_copyMemory_1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {";
		final byte[] input = inputStr.getBytes();

		System.out.println(inputStr);
		for (byte b : input) {
			System.out.print(b + ", ");
		}
		System.out.println();
		
		final Unsafe unsafe = getUnsafe();
		final long baseAddress = unsafe.allocateMemory(input.length);
		try {
			unsafe.copyMemory(input, unsafe.ARRAY_BYTE_BASE_OFFSET, null, baseAddress, input.length);
			
			final byte[] output = new byte[input.length];
			unsafe.copyMemory(null, baseAddress, output, unsafe.ARRAY_BYTE_BASE_OFFSET, output.length);
			
			for (byte b : output) {
				System.out.print(b + ", ");
			}
			System.out.println();
			System.out.println(new String(output));
			
			int failCount = 0;
			for (int i = 0; i < output.length; i++) {
				if (output[i] != input[i]) {
					failCount++;
				}
			}
			System.out.println("Failed num: " + failCount);
		}
		finally {
			unsafe.freeMemory(baseAddress);
		}
		
	}
	
	private void exhaustRam() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		final Unsafe unsafe = getUnsafe();
		final ExecutorService exec = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			exec.submit(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							long addr = unsafe.allocateMemory(1);
							unsafe.putByte(addr, (byte) 99);
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

	private static long normalize(int value) {
	    if(value >= 0) return value;
	    return (~0L >>> 32) & value;
	}
	
	public static long sizeOf(Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
	    return getUnsafe().getAddress(
	        normalize(getUnsafe().getInt(object, 4L)) + 12L);
	}
	
	static long toAddress(Object obj) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	    Object[] array = new Object[] {obj};
	    long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
	    return normalize(getUnsafe().getInt(array, baseOffset));
	}

	static Object fromAddress(long address) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	    Object[] array = new Object[] {null};
	    long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
	    getUnsafe().putLong(array, baseOffset, address);
	    return array[0];
	}


	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, UnsupportedEncodingException {
		TestBase base = new TestBase();
//		base.testAvoidingObjectInit();

//		base.testOffHeapMemory();
//		base.testOffHeapMemory2();
//		base.testOffHeapMemory3();
//		base.testOffHeapMemory4();
		
		base.testOffHeapMemory_copyMemory_1();
	}
	
}

