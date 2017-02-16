package testThread;

public class Control {

    /**
     * @param args
     */
    public static void main(String[] args) {
	/*TestThread testThread = new TestThread();
	testThread.start();*/
	
	/*Task task = new Task();
	new Thread(task).start();*/
    
	Task task = new Task();
	TestThread testThread = new TestThread(task);
	testThread.start();
	
    }
}
