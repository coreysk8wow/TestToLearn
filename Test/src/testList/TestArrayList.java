package testList;

import java.util.ArrayList;
import java.util.List;

public class TestArrayList {

	public static void main(String[] args) {
		/*List list = new ArrayList();
		list.add(null);
		list.add(null);
//		list.add("2");
		if (!list.isEmpty() && list.get(0) == null) {
			System.out.println("index 0 is null");
		}*/
		
		/*byte[] data = new byte[]{1, 2, 3};
		List<ByteBuffer> list = new ArrayList<ByteBuffer>();
		list.add(null);
		list.add(ByteBuffer.wrap(data));
		if (!list.isEmpty() && list.get(0) == null) {
			System.out.println("index 0 is null");
		}
		
		for (ByteBuffer buffer : list) {
			System.out.println(buffer.toString());
		}*/
		
		List list = new ArrayList();
		list.add(null);
		list.add(null);
		list.add("2");
		list.add(null);
		list.remove(null);
		list.remove(null);
		list.remove(null);
		list.remove("2");
		System.out.println(list.isEmpty());
		if (list.get(0) == null) {
			System.out.println("ldldl");
		}
	}

}
