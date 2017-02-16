package tij.concurrency.bankTellerSimulation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Customer {
	private final int serviceTime;
	
	Customer(int serviceTime) {
		this.serviceTime = serviceTime;
	}

	int getServiceTime() {
		return serviceTime;
	}

	@Override
	public String toString() {
		return "[" + getServiceTime() + "]";
	}

}

class CustomerLine extends ArrayBlockingQueue<Customer> {
	CustomerLine(int maxLineSize) {
		super(maxLineSize);
	}

	@Override
	public String toString() {
		if (size() == 0)
			return ("[Empty]");
		StringBuilder result = new StringBuilder();
		for (Customer customer : this) {
			result.append(customer.toString());
		}
		return result.toString();
	}

}

/*
 * In this case, there is only one customer generator.
 */
class CustomerGenerator implements Runnable {
	private CustomerLine customerLine;
	private static Random rand = new Random(47);

	CustomerGenerator(CustomerLine customerLine) {
		this.customerLine = customerLine;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
				customerLine.put(new Customer(rand.nextInt(1000)));
			}
		}
		catch (InterruptedException e) {
			System.out.println("CustomerGenerator interrupted.");
		}
		System.out.println("CustomerGenerator terminating.");
	}
	
}

/*
 * One Teller instance, one thread.
 * Don't use one Teller instance for multiple threads.		
 */
class Teller implements Runnable, Comparable<Teller> {
	private static int counter = 0;
	private final int id = counter++;
	
	// Customers served during this shift
	private int customersServed = 0;
	
	private CustomerLine customerLine;
	
	private boolean servingCustomerLine = true;
	
	Teller(CustomerLine customerLine) {
		this.customerLine = customerLine;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
    			Customer customer = customerLine.take();
    			TimeUnit.MILLISECONDS.sleep(customer.getServiceTime());
    			synchronized (this) {
    				customersServed++;
    				while (!servingCustomerLine) {
    					wait();
    				}
    			}
			}
		}
		catch (InterruptedException ie) {
			System.out.println(this + "interrupted.");
		}
		System.out.println(this + "terminating");
	}
	
	 synchronized void doSomethingElse() {
		 customersServed = 0;
		 servingCustomerLine = false;
	}
	 
	 synchronized void serveCustomerLine() {
		 servingCustomerLine = true;
		 notifyAll();
	 }

	 @Override
	public String toString() {
		return "Teller " + id + " ";
	}
	
	public String shortString() {
		return "T" + id;
	}

	@Override
	public int compareTo(Teller other) {
		return customersServed < other.customersServed ? -1
				: (customersServed == other.customersServed ? 0 : 1);
	}
	
}

class TellerManager implements Runnable {
	private ExecutorService exec;
	
	private CustomerLine customerLine;
	
	private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();
	
	private Queue<Teller> tellersDoingOtherThings = new LinkedList<Teller>();
	
	private int adjustmentPeriod;
	
	TellerManager(ExecutorService exec, CustomerLine customerLine, int adjustmentPeriod) {
		this.exec = exec;
		this.customerLine = customerLine;
		this.adjustmentPeriod = adjustmentPeriod;
		
		// Start a single teller
		Teller teller = new Teller(customerLine);
		exec.execute(teller);
		workingTellers.add(teller);
	}
	
	void adjustTellerNumber() {
		/*
		 * This is actually a control system. By adjusting the numbers, 
		 * you can reveal stability issues in the control mechanism.
		 * If line is too long, add another teller:
		 */
		if (customerLine.size() / workingTellers.size() > 2) {
			// If tellers are on break or doing another job, bring on back:
			if (tellersDoingOtherThings.size() > 0) {
				Teller teller = tellersDoingOtherThings.remove();
				teller.serveCustomerLine();
				workingTellers.offer(teller);
				return;
			}
			// else create (hire) a new teller
			Teller teller = new Teller(customerLine);
			exec.execute(teller);
			workingTellers.add(teller);
			return;
		}
		// If line is short enough, remove a teller
		if (workingTellers.size() > 1 && customerLine.size() / workingTellers.size() < 2) {
			reassignOneTeller();
		}
		if (customerLine.size() == 0) {
			while (workingTellers.size() > 1) {
				reassignOneTeller();
			}
		}
	}

	private void reassignOneTeller() {
		Teller teller = workingTellers.poll();
		teller.doSomethingElse();
		tellersDoingOtherThings.offer(teller);
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
				adjustTellerNumber();
				System.out.print(customerLine + " { ");
				for (Teller teller : workingTellers) {
					System.out.print(teller.shortString()  + " ");
				}
				System.out.println("}");
			}
		}
		catch (InterruptedException ie) {
			System.out.println(this + "interrupted.");
		}
		System.out.println(this + "terminating.");
	}
	
	@Override
	public String toString() {
		return "TellerManager ";
	}
}

public class BankTellerSimulation {
	static final int MAX_LINE_SIZE = 50;
	static final int ADJUSTMENT_PERIOD = 1000;
	
	public static void main(String[] args) throws NumberFormatException,
			InterruptedException, IOException {
		ExecutorService exec = Executors.newCachedThreadPool();
		// If line is too long, customers will leave: 
		CustomerLine customerLine = new CustomerLine(MAX_LINE_SIZE);
		exec.execute(new CustomerGenerator(customerLine));
		//Manager will add and remove tellers as necessary:
		exec.execute(new TellerManager(exec, customerLine, ADJUSTMENT_PERIOD));
		if (args.length > 0) {
			TimeUnit.SECONDS.sleep(new Integer(args[0]));
		}
		else {
			System.out.println("Press 'Enter' to quit");
			System.in.read();
		}
		exec.shutdownNow();
		
	}

}