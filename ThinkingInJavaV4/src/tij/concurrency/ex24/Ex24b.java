/**
 * In this case, to be thread-safe, Consumer2 and Producer2 should both 
 * synchronized on the same Market2 object, otherwise, as writen in this file, 
 * it's not thread-safe.
 * 
 * To test it, run it, generate a.txt file in d:\, 
 * use FindingError.java to find the first wrong line.
 */

package tij.concurrency.ex24;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import tij.tools.IO;

class Product2 {
	private static long count;
	private final long id = ++count;
	
	public static Product2 getProduct() {
		return new Product2();
	}
	
	public String toString() {
		return "Product " + id;
	}
}

/*
 * Single consumer
 */
class Consumer2 implements Runnable {
	Market2 market;
	final Random random = new Random();
	
	Consumer2(Market2 m) {
		market = m;
	}
	
	@Override
	public void run() {
		market.setConsumerThread(Thread.currentThread());
		StringBuilder sb = new StringBuilder();
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (this) {
					while (market.isWarehouseEmpty())
						wait();
				}
				synchronized (market.getProducer()) {
					int needingNum = random.nextInt(Market2.MAX_PRODUCT_NUM_IN_ONE_ORDER) + 1; 
					int j =  needingNum < market.getWarehouseSize()
							? needingNum : market.getWarehouseSize();
					for (int i = 0; i < j; i++) {
/*						System.out.println("Consumer gets a product: " + market.removeFromWarehouse() + 
								" in an order of " + j + " products." +
										"[" + market.getWarehouseSize() + "/" + market.WAREHOUSE_CAPACITY + "]");*/
						sb.delete(0, sb.length());
						sb.append("Consumer gets a product: ")
								.append(market.removeFromWarehouse())
								.append(" in an order of ").append(j)
								.append(" products. [")
								.append(market.getWarehouseSize()).append("/")
								.append(market.WAREHOUSE_CAPACITY).append("]");
						IO.println(sb.toString());
						
//						TimeUnit.MILLISECONDS.sleep(50);
						TimeUnit.MILLISECONDS.sleep(2);
					}
					if (market.getWarehouseSize() - Market2.WAREHOUSE_CAPACITY * 0.5 < 1e-6)
						market.getProducer().notifyAll();
				}
//				TimeUnit.MILLISECONDS.sleep(100);
				TimeUnit.MILLISECONDS.sleep(10);
			}
		}
		catch (InterruptedException ie) {
//			System.out.println("Exit via Interrupted Exception.");
//			IO.appendText2File("Exit via Interrupted Exception.", Market2.OUTPUT_FILE, true);
			IO.println("Consumer is exiting via Interrupted Exception.");
		}
		finally {
//			System.out.println("Consumer: Bye!");
//			IO.appendText2File("Consumer: Bye!", Market2.OUTPUT_FILE, true);
			IO.println("Consumer: Bye!");
			synchronized (market.getProducer()) {
				market.getProducer().notifyAll();
			}
		}
	}
	
}

/*
 * Single producer
 */
class Producer2 implements Runnable {
	// The producer could not ever produce more than this number of products.
	static final long MAX_NUM_PRODUCTS = 1000;
	Market2 market;
	final Random random = new Random();
	
	
	Producer2(Market2 m) {
		market = m;
	}
	
	@Override
	public void run() {
		market.setProducerThread(Thread.currentThread());
		StringBuilder sb = new StringBuilder();
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (this) {
					while (market.getWarehouseSize() - Market2.WAREHOUSE_CAPACITY * 0.7 > 1e-6)
						wait();
				}
				if (market.getCount() > MAX_NUM_PRODUCTS) {
					/*System.out.println("\nOut of material, producing stopped, " +
							"consumer can get the remaining products in the warehouse of market.");*/
					/*IO.appendText2File("\nOut of material, producing stopped, " +
							"consumer can get the remaining products in the warehouse of market.", Market2.OUTPUT_FILE, true);*/
					IO.println("\nOut of material, producing stopped, " +
							"consumer can get the remaining products in the warehouse of market.");
					synchronized (market.getProducer()) {
						while (!market.isWarehouseEmpty()) {
							market.getProducer().wait();
						}
					}
					new Thread() {
						@Override
						public void run() {
							try {
								market.getProducerThread().join();
								market.getConsumerThread().join();
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							try {
								IO.closeOutput();
							}
							catch (IOException e) {
								e.printStackTrace();
							}
						}
					}.start();
					
					market.exec.shutdownNow();
					return;
				}
//				System.out.println("Producer: order up!");
//				IO.appendText2File("Producer: order up!", Market2.OUTPUT_FILE, true);
				IO.println("Producer: order up!");
				synchronized (market.getConsumer()) {
					int i = 0;
					for (int j = random.nextInt(Market2.MAX_PRODUCT_NUM_IN_ONE_PRODUCING) + 1; 
							!market.isWarehouseFull() && i < j && market.getCount() <= MAX_NUM_PRODUCTS; i++) {
						market.offerToWarehouse(Product2.getProduct());
						market.incrementCount();
//						TimeUnit.MILLISECONDS.sleep(50);
						TimeUnit.MILLISECONDS.sleep(2);
					}
					/*System.out.println("Producer: produced " + i + " products. [" + 
							market.getWarehouseSize() + "/" + market.WAREHOUSE_CAPACITY + "]");*/
					sb.delete(0, sb.length());
					sb.append("Producer: produced ").append(i)
							.append(" products. [")
							.append(market.getWarehouseSize()).append("/")
							.append(market.WAREHOUSE_CAPACITY).append("]");
//					IO.appendText2File(sb.toString(), Market2.OUTPUT_FILE, true);
					IO.println(sb.toString());
					market.getConsumer().notifyAll();
				}
//				TimeUnit.MILLISECONDS.sleep(100);
				TimeUnit.MILLISECONDS.sleep(10);
			}
		}
		catch (InterruptedException ie) {
//			System.out.println("Exit via Interrupted Exception");
//			IO.appendText2File("Exit via Interrupted Exception", Market2.OUTPUT_FILE, true);
			IO.println("Producer is exiting via Interrupted Exception");
		}
		finally {
//			System.out.println("Producer: Bye!");
//			IO.appendText2File("Producer: Bye!", Market2.OUTPUT_FILE, true);
			IO.println("Producer: Bye!");
			synchronized (market.getConsumer()) {
				market.getConsumer().notifyAll();
			}
		}
	}
	
}

class Market2 {
	static final File OUTPUT_FILE = new File("K:/ex24b.txt");
	static final int WAREHOUSE_CAPACITY = 50;
	static final int MAX_PRODUCT_NUM_IN_ONE_ORDER = (int) Math.floor(WAREHOUSE_CAPACITY * 0.2); 
	static final int MAX_PRODUCT_NUM_IN_ONE_PRODUCING = (int) Math.floor(WAREHOUSE_CAPACITY * 0.3); 
	long count;
	
	private Producer2 producer = new Producer2(this);
	private Consumer2 consumer = new Consumer2(this);
	
	// The warehouse is used to store products, its capacity is limited.
	private FixedQueue<Product2> warehouse = new FixedQueue<Product2>(WAREHOUSE_CAPACITY);
	
	ExecutorService exec = Executors.newCachedThreadPool();

	private Thread consumerThread;
	private Thread producerThread;
	
	{
    	try {
    		IO.setupOutput(Market2.OUTPUT_FILE, true);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	Market2() {
		exec.execute(producer);
		exec.execute(consumer);
	}
	
	synchronized Thread getConsumerThread() {
		return consumerThread;
	}
	synchronized Thread getProducerThread() {
		return producerThread;
	}
	synchronized void setConsumerThread(Thread t) {
		consumerThread = t;
	}
	synchronized void setProducerThread(Thread t) {
		producerThread = t;
	}
	
	synchronized long getCount() {
		return count;
	}
	synchronized void incrementCount() {
		count++;
	}
	
	Producer2 getProducer() {
		return producer;
	}
	
	Consumer2 getConsumer() {
		return consumer;
	}
	
//	synchronized 
	boolean isWarehouseEmpty() {
		if (warehouse.isEmpty())
			return true;
		else
			return false;
	}
	
//	synchronized 
	boolean isWarehouseFull() {
		return warehouse.isFull();
	}
	
//	synchronized 
	int getWarehouseSize() {
		return warehouse.size();
	}
	
//	synchronized 
	Product2 removeFromWarehouse() {
		return warehouse.remove();
	}
	
//	synchronized 
	boolean offerToWarehouse(Product2 p) {
		return warehouse.offer(p);
	}
	
	class FixedQueue<E> extends LinkedList<E> {
		private int capacity;
		
		private FixedQueue(int capacity) {
			this.capacity = capacity;
		}
		
		@Override
		public boolean offer(E p) {
			if (size() < capacity)
				return super.offer(p);
			return false;
		}
		
		public boolean isFull() {
			if (size() == capacity)
				return true;
			else
				return false;
		}
	}
	
}

public class Ex24b {
	public static void main(String[] args) {
		Calendar  c = new GregorianCalendar();
		IO.appendText2File(c.getTime().toString(), Market2.OUTPUT_FILE, true);
		new Market2();
	}

}
