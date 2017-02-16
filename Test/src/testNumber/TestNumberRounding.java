package testNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestNumberRounding {

	public static void main(String[] args) {
		
		float n1 = 123456.776777f;
		BigDecimal bdn1 = new BigDecimal(n1);
		bdn1 = bdn1.setScale(5, RoundingMode.HALF_EVEN);
		float r1 = bdn1.floatValue();
		System.out.println(r1);
		
		double n2 = 123456.776777;
		BigDecimal bdn2 = new BigDecimal(n1);
		bdn2 = bdn2.setScale(5, RoundingMode.HALF_EVEN);
		double r2 = bdn2.doubleValue();
		System.out.println(r2);
		
		Double n3 = 123456.776777;
		BigDecimal bdn3 = new BigDecimal(n1);
		bdn3 = bdn3.setScale(5, RoundingMode.HALF_EVEN);
		Double r3 = bdn3.doubleValue();
		System.out.println(r3);
		
		float n4 = 123456.776777f;
		float r4 = Math.round(n4 * 1000f) / 1000f;
		System.out.println(r4);
		
		double n5 = 123456.776777d;
		double r5 = Math.round(n5 * 1000d) / 1000d;
		System.out.println(r5);
		
		
	}

}
