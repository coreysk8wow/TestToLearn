package testThread;

public class TestThread extends Thread {

    Runnable target;
    
    public TestThread() {
    }
    
    public TestThread(Runnable target) {
	this.target = target;
    }
    
    @Override
    public void run() {
	System.out.println("In TestThread");
    }
    
}
