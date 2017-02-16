package tij.concurrency.ex15;

public class Ex15 {
	private Object syncObject1 = new Object();
	private Object syncObject2 = new Object();
	private Object syncObject3 = new Object();
	
	void printABC() {
		synchronized(syncObject1) {
    		for (char i = 65; i < 91; i++) {
    			System.out.print(i);
    		}
    		System.out.println();
		}
	}
	
	void print123() {
		synchronized(syncObject1) {
    		for (char i = 49; i < 58; i++) {
    			System.out.print(i);
    		}
    		System.out.println();
		}
	}
	
	void printabc() {
		synchronized(syncObject1) {
    		for (char i = 97; i < 123; i++) {
    			System.out.print(i);
    		}
    		System.out.println();
		}
	}

	public static void main(String[] args) {
		final Ex15 ex15 = new Ex15();
		
		new Thread() {
			@Override
			public void run() {
				ex15.printABC();
			}}.start();

		new Thread() {
			@Override
			public void run() {
				ex15.print123();
			}}.start();
		
		new Thread() {
			@Override
			public void run() {
				ex15.printabc();
			}}.start();
	}

}

