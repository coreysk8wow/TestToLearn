package testThread;

public class Task implements Runnable {

    @Override
    public void run() {
	System.out.println("In Task");
    }

}
