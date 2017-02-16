/**
 * Exercise 35: 
 * Modify BankTellerSimulation.java so that it represents Web clients making requests of a fixed number of servers. 
 * The goal is to determine the load that the group of servers can handle.
 * 
 * @author shun
 * Apr 9, 2014
 *
 */

package tij.concurrency.ex35.serverLoad;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Time {
	final int time;
	final TimeUnit timeUnit;
	
	Time(int time, TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
	}
	
	void sleep() throws InterruptedException {
		timeUnit.sleep(time);
	}
	
	@Override
	public String toString()	 {
		String timeShortName = null;
		if (timeUnit == TimeUnit.SECONDS)
			timeShortName = "s";
		else if (timeUnit == TimeUnit.MILLISECONDS)
			timeShortName = "ms";
		else
			timeShortName = timeUnit.toString();
		
		return time + " " + timeShortName;
	}
}

final class TimeConfig {
}

class Client {
	private final Time serviceTime;

	Client(Time serviceTime) {
		this.serviceTime = serviceTime;
	}

	Time getServiceTime() {
		return serviceTime;
	}
	
	@Override
	public String toString() {
		return serviceTime.toString();
	}
}

class ClientQueue extends LinkedBlockingQueue<Client> {
	@Override
	public String toString() {
		if (size() == 0) {
			return "[Empty]";
		}
		StringBuilder result = new StringBuilder();
		for (Client c : this) {
			result.append(c.toString());
		}
		
		return result.toString();
	}
}

class ClientGenerator implements Runnable {
	private ClientQueue clientQ;
	private final Random rand = new Random();
	
	ClientGenerator(ClientQueue clientQ) {
		this.clientQ = clientQ;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(10);
				clientQ.put(new Client(new Time(rand.nextInt(1000), TimeUnit.MILLISECONDS)));
			}
		}
		catch (InterruptedException ie) {
			System.out.println(this + " interrupted.");
		}
		System.out.println(this + " terminating.");
	}

	@Override
	public String toString() {
		return "ClientGenerator";
	}
}




public class Ex35 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
