package tij.concurrency.sharingResources;

public abstract class IntGenerator {
	private volatile boolean canceled = false;
	
	public void cancel() {
		canceled = true;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public abstract int next();

}
