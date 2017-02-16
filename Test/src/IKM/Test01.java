package IKM;

import java.util.*;

public class Test01 {

	public static void main(String[] args) {
		int i = 0, j = 0;

		cnt:
		for (i = 0; i < 10; i++) {
			for (j = 0; j < 5; j++)
				continue cnt;
		}
		System.out.println("i=" + i + ", j=" + j);

		
		
		/*Long i = new Long(10);
		Integer j = new Integer(10);
		if( i.equals(j)) {
			System.out.println("Equal");
		}
		else {
			System.out.println("Not Equal");
		}*/

	}
 }
