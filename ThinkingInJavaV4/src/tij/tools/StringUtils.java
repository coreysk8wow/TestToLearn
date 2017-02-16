package tij.tools;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static Long findFirstNumber(String s) {
		Long result = null;
		char c;
		boolean prevIsNum = false;
		int startIndex = -1, 
			endIndex = -1;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c >=  '0' && c <= '9') {
				if (!prevIsNum) {
    				prevIsNum = true;
    				startIndex = endIndex = i;
				}
				else {
					endIndex++;
				}
			}
			else if (startIndex != -1) {
				prevIsNum = false;
				break;
			}
		}
		
		if (startIndex != -1)
			result = Long.parseLong(s.substring(startIndex, endIndex + 1));
		
		return result;
	}
	
	public static List<Long> findAllNumbers(String s) {
		List<Long> numList = new ArrayList<Long>();
		
		Long result = null;
		char c;
		boolean prevIsNum = false;
		int beginIndex = -1, 
			endIndex = -1;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c >=  '0' && c <= '9') {
				if (!prevIsNum) {
    				prevIsNum = true;
    				beginIndex = endIndex = i;
				}
				else {
					endIndex++;
				}
			}
			else if (prevIsNum && beginIndex != -1) {
				numList.add(Long.parseLong(s.substring(beginIndex, endIndex + 1)));
				prevIsNum = false;
			}
		}
		
		if (beginIndex != -1)
			result = Long.parseLong(s.substring(beginIndex, endIndex + 1));
		
		return numList;
	}
	
	public static void main(String[] args) {
		String s = "Consumer gets a product: Product 4 in an order of 6 products.[6/50]";
		
		//System.out.println(findFirstNumber(s));
		
		List<Long> list = findAllNumbers(s);
		for (Long l : list)
			System.out.print(l + ", ");
		
	}

}
