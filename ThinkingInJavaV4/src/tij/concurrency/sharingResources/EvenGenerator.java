package tij.concurrency.sharingResources;

public class EvenGenerator extends IntGenerator {
	private int currEvenValue = 0; 

	@Override
	public int next() {
		currEvenValue++;
		currEvenValue++;
		return currEvenValue;
	}

	public static void main(String[] args) {
		EvenChecker.check(new EvenGenerator());
	}
	
}
