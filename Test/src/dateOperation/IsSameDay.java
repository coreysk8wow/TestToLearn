package dateOperation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsSameDay {

	private boolean isSameDay(long you, long me) {
		
		
		return false;
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2015-04-21");
		long longD = date.getTime();
		System.out.println(longD / 24 / 60 / 60 /1000);
	}

}
