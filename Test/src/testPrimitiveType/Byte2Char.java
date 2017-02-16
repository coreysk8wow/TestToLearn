package testPrimitiveType;

public class Byte2Char {
    
    private void testChinese(String s) {
	char[] chinese = s.toCharArray();
	for(char c : chinese) {
	    System.out.print(Integer.toHexString((int)c) + " ");
	}
	System.out.println();
	for(char c : chinese) {
	    System.out.print(Integer.toHexString((byte)c) + " ");
	}
	System.out.println();
    }
    
    private void testEnglish(String s) {
	char[] english = s.toCharArray();
	for(char c : english) {
	    System.out.print(Integer.toHexString((int)c) + " ");
	}
	System.out.println();
	for(char c : english) {
	    System.out.print(Integer.toHexString((byte)c) + " ");
	}
	
    }
    public static void main(String[] args) {
	Byte2Char b2c = new Byte2Char();
	b2c.testChinese("大家好");
	b2c.testEnglish("Hello");
    }
}
