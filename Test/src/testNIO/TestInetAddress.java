package testNIO;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestInetAddress {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
	    byte[] ip = address.getAddress();
	    for ( byte b : ip) {
	    	System.out.print(b + ".");
	    }
// ------------------------------------------------------------------------------
		
//		InetAddress address = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
		
	}

}
