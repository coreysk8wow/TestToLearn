/**
 * This Producer-Consumer example is good for single producer and single consumer, 
 * once multiple producers or consumers join the market, the program can work correctly and safely.
 * but the efficient is very low, because all the producer threads and the consumer threads blocks on 
 * the monitor of object market. One producer has to wait when another producer is producing.
 * In Ex24c.java, I will fix this low efficient issue.
 * 
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

class Product {
	private static long count;
	private final long id = ++count;
	
	public static Product getProduct() {
		return new Product();
	}
	
	public String toString() {
		return "Product " + id;
	}
}

/*
 * Single consumer
 */
class Consumer implements Runnable {
	Market market;
	final Random random = new Random();
	
	Consumer(Market m) {
		market = m;
	}
	
	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (market) {
					while (market.isWarehouseEmpty())
						market.wait();
				}
				synchronized (market) {
					int needingNum = random.nextInt(Market.MAX_PRODUCT_NUM_IN_ONE_ORDER) + 1;
					int j = needingNum < market.getWarehouseSize()
							? needingNum : market.getWarehouseSize();
					for (int i = 0; i < j; i++) {
						/*System.out.println("Consumer gets a product: " + market.removeFromWarehouse() + 
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
						TimeUnit.MILLISECONDS.sleep(5);
					}
					if (market.getWarehouseSize() - Market.WAREHOUSE_CAPACITY * 0.5 < 1e-6)
						market.notifyAll();
				}
//				TimeUnit.MILLISECONDS.sleep(100);
				TimeUnit.MILLISECONDS.sleep(20);
			}
		}
		catch (InterruptedException ie) {
//			System.out.println("Exit via Interrupted Exception.");
//			IO.appendText2File("Exit via Interrupted Exception.", Ex24c.OUTPUT_FILE, true);
			IO.println("Consumer is exiting via Interrupted Exception.");
		}
		finally {
//			System.out.println("Consumer: Bye!");
//			IO.appendText2File("Consumer: Bye!", Ex24c.OUTPUT_FILE, true);
			IO.println("Consumer: Bye!");
			synchronized (market) {
				market.notifyAll();
			}
		}
	}
	
}

/*
 * Single producer
 */
class Producer implements Runnable {
	// The producer could not ever produce more than this num of products.
	static final long MAX_NUM_PRODUCTS = 1000L; // Use 1000 in normal
	Market market;
	final Random random = new Random();
	
	
	Producer(Market m) {
		market = m;
	}
	
	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (market) {
					while (market.getWarehouseSize() - Market.WAREHOUSE_CAPACITY * 0.7 > 1e-6)
						market.wait();
				}
				if (market.getCount() > MAX_NUM_PRODUCTS) {
					/*System.out.println("\nOut of material, producing stopped, " +
							"consumer can get the remaining products in the warehouse of market.");*/
					/*IO.appendText2File("\nOut of material, producing stopped, " +
							"consumer can get the remaining products in the warehouse of market.", Ex24c.OUTPUT_FILE, true);*/
					
					if (!market.exec.isShutdown()) {
    					synchronized (Producer.class) {
        					if (!market.exec.isShutdown()) {
        						IO.println("\nOut of material, producing stopped, " +
        								"consumer can get the remaining products in the warehouse of market.");
        						
                				synchronized (market) {
                					while (!market.isWarehouseEmpty())
                						market.wait();
                				}
                				
                				market.exec.shutdownNow();
                				synchronized (market.getShutdownLock()) {
                					market.getShutdownLock().notify();
                				}
        					}
    					}
					}
					return;
				}
//				System.out.println("Producer: order up!");
//				IO.appendText2File("Producer: order up!", Ex24c.OUTPUT_FILE, true);
				IO.println("Producer: order up!");
				synchronized (market) {
					int i = 0;
					for (int j = random.nextInt(Market.MAX_PRODUCT_NUM_IN_ONE_PRODUCING) + 1; 	
							!market.isWarehouseFull() && i < j && market.getCount() <= MAX_NUM_PRODUCTS; i++) {
						market.offerToWarehouse(Product.getProduct());
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
//					IO.appendText2File(sb.toString(), Ex24c.OUTPUT_FILE, true);
					IO.println(sb.toString());		
					market.notifyAll();
				}
//				TimeUnit.MILLISECONDS.sleep(100);
				TimeUnit.MILLISECONDS.sleep(10);
			}
		}
		catch (InterruptedException ie) {
//			System.out.println("Exit via Interrupted Exception");
//			IO.appendText2File("Exit via Interrupted Exception", Ex24c.OUTPUT_FILE, true);
			IO.println("Producer is exiting via Interrupted Exception");
		}
		finally {
//			System.out.println("Producer: Bye!");
//			IO.appendText2File("Producer: Bye!", Ex24c.OUTPUT_FILE, true);
			IO.println("Producer: Bye!");
			synchronized (market) {
				market.notifyAll();
			}
		}
	}
	
}

class Market implements Runnable, WarehouseAccess {
	static final File OUTPUT_FILE = new File("K:/ex24.txt");
	static final int WAREHOUSE_CAPACITY = 50;
	static final int MAX_PRODUCT_NUM_IN_ONE_ORDER = (int) Math.floor(WAREHOUSE_CAPACITY * 0.2); 
	static final int MAX_PRODUCT_NUM_IN_ONE_PRODUCING = (int) Math.floor(WAREHOUSE_CAPACITY * 0.4); 
	long count;
	
	final Object shutdownLock = new Object();
	
	private Producer producer = new Producer(this);
	private Consumer consumer = new Consumer(this);
	
	// The warehouse is used to store products, its capacity is limited.
	private FixedQueue<Product> warehouse = new FixedQueue<Product>(WAREHOUSE_CAPACITY);
	
	ExecutorService exec = Executors.newCachedThreadPool();
	
	Object getShutdownLock() {
		return shutdownLock;
	}
	
	synchronized long getCount() {
		return count;
	}
	synchronized void incrementCount() {
		count++;
	}
	
	@Override
//	synchronized 
	public boolean isWarehouseEmpty() {
		if (warehouse.isEmpty())
			return true;
		else
			return false;
	}
	
	@Override
//	synchronized 
	public boolean isWarehouseFull() {
		return warehouse.isFull();
	}
	
	@Override
//	synchronized 
	public int getWarehouseSize() {
		return warehouse.size();
	}
	
	@Override
//	synchronized 
	public Product removeFromWarehouse() {
		return warehouse.remove();
	}
	
	@Override
//	synchronized 
	public boolean offerToWarehouse(Product p) {
		return warehouse.offer(p);
	}
	
	private class FixedQueue<E> extends LinkedList<E> {
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

	@Override
	public void run() {
		try {
    		IO.setupOutput(Market.OUTPUT_FILE, true);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
		
		final Thread currentThread = Thread.currentThread();
		new Thread() {
			@Override
			public void run() {
				try {
					currentThread.join();
				}
				catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					IO.closeOutput();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		synchronized (shutdownLock) {
			/*
			 * This program works correctly and safely for multiple
			 * producers and consumers. 
			 */
    		exec.execute(producer);
    		exec.execute(consumer);
    		exec.execute(consumer);
//    		exec.execute(producer);
    		exec.execute(consumer);
    		try {
    			shutdownLock.wait();
    			while (!exec.isTerminated());
    		}
    		catch (InterruptedException e) {
    			e.printStackTrace();
    		}
		}
		IO.println("Market thread has stopped.");
	}
}

interface WarehouseAccess {
	boolean isWarehouseEmpty();
	
	boolean isWarehouseFull();
	
	int getWarehouseSize();
	
	Product removeFromWarehouse();
	
	boolean offerToWarehouse(Product p);
}

public class Ex24 {
	public static void main(String[] args) {
		Calendar  c = new GregorianCalendar();
		IO.appendText2File(c.getTime().toString(), Market.OUTPUT_FILE, false);
		new Thread(new Market()).start();
		
	}
}
