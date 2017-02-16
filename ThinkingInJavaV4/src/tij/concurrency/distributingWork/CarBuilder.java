package tij.concurrency.distributingWork;

import static java.lang.System.out;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/*
 * You¡¯ll notice that Car has all of its methods synchronized. 
 * As it turns out, in this example this is redundant, 
 * because within the factory the Cars move through the queues 
 * and only one task can work on a car at a time. 
 * Basically, the queues force serialized access to the Cars. 
 * But this is exactly the kind of trap you can fall into¡ªyou can say 
 * "Let¡¯s try to optimize by not synchronizing the Car class 
 * because it doesn¡¯t look like it needs it here." 
 * But later, when this system is connected to another 
 * which does need the Car to be synchronized, it breaks.
 * 
 * 
 * Brian Goetz comments:
 * 
 * It¡¯s much easier to say, 
 * "Car might be used from multiple threads, so let¡¯s make it thread-safe in the obvious way." 
 * The way I characterize this approach is: 
 * At public parks, you will find guard rails where there is a steep drop, 
 * and you may find signs that say, "Don¡¯t lean on the guard rail." 
 * Of course, the real purpose of this rule is not to prevent you from leaning on the rail
 * ¡ªit is to prevent you from falling off the cliff. 
 * But "Don¡¯t lean on the rail" is a much easier rule to follow than "Don¡¯t fall off the cliff" 
 */
class Car {
	private final int id;
	private boolean engine = false;
	private boolean driveTrain = false;
	private boolean wheels = false;
	private boolean exhaustSystem = false;
	private boolean body = false;
	private boolean fenders = false;

	Car(int id) {
		this.id = id;
	}
	
	synchronized int getId() {
		return id;
	}
	
	synchronized void addEngine() {
		engine = true;
	}
	
	synchronized void addDriveTrain() {
		driveTrain = true;
	}
	
	synchronized void addWheels() {
		wheels = true;
	}
	
	synchronized void addExhaustSystem() {
		exhaustSystem = true;
	}
	
	synchronized void addBody() {
		body = true;
	}
	
	synchronized void addFenders() {
		fenders = true;
	}
	
	public synchronized String toString() {
		return "Car " + id + " [" + " engine: " + engine + " driveTrain: " + driveTrain + " wheels: " + wheels 
				+ " exhaustSystem: " + exhaustSystem + " body: " + body + " fenders: " + fenders + " ]";
	}
	
}


class CarQueue extends LinkedBlockingQueue<Car> {}


class ChassisBuilder implements Runnable {
	private CarQueue carQueue;
	private int counter = 0;
	
	ChassisBuilder(CarQueue carQueue) {
		this.carQueue = carQueue;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(500);
				// Make chassis
				Car car = new Car(++counter);
				out.println("ChassisBuilder created " + car);
				carQueue.put(car);
			}
			
		} catch(InterruptedException ie) {
			out.println("Interrupted: ChassisBuilder");
		}
		out.println("ChassisBuilder off");
	}
	
}


class Assembler implements Runnable {
	private CarQueue chassisQueue;
	private CarQueue finishingQueue;
	private Car car;
	private CyclicBarrier cyclicBarrier = new CyclicBarrier(ROBOT_NUMBER + 1);
	private RobotPool robotPool;
	
	private static final int ROBOT_NUMBER = 6;
	
	Assembler(CarQueue chassisQueue, CarQueue finishingQueue, RobotPool robotPool) {
		this.chassisQueue = chassisQueue;
		this.finishingQueue = finishingQueue;
		this.robotPool = robotPool;
	}
	
	Car getCar() {
		return car;
	}
	
	CyclicBarrier getCyclicBarrier() {
		return cyclicBarrier;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				car = chassisQueue.take();
				robotPool.hire(EngineRobot.class, this);
				robotPool.hire(DriveTrainRobot.class, this);
				robotPool.hire(WheelRobot.class, this);
				robotPool.hire(ExhaustSystemRobot.class, this);
				robotPool.hire(BodyRobot.class, this);
				robotPool.hire(FenderRobot.class, this);
				cyclicBarrier.await();
				finishingQueue.put(car);
			}
		} catch(InterruptedException ie) {
			out.println("Interrupted: Assembler");
		}
		catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		out.println("Assembler off");
	}
	
}


class Reporter implements Runnable {
	private CarQueue completedCarQueue;
	Reporter(CarQueue completedCarQueue) {
		this.completedCarQueue = completedCarQueue;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				out.println("Reporter: " + completedCarQueue.take());
			}
		} catch(InterruptedException ie) {
			out.println("Interrupted: Reporter");
		}
		out.println("Reporter off");
	}
	
}


abstract class Robot implements Runnable {
	private RobotPool robotPool;
	Assembler assembler;
	private boolean engage = false;
	
	
	Robot(RobotPool robotPool) {
		this.robotPool = robotPool;
	}
	
	Robot assignAssembler(Assembler assembler) {
		this.assembler = assembler;
		return this;
	}
	
	synchronized void engage() {
		engage = true;
		notifyAll();
	}
	
	abstract void performService();

	@Override
	public void run() {
		try {
			powerDown();
			while (!Thread.interrupted()) {
				performService();
				assembler.getCyclicBarrier().await();
				powerDown();
			}
			
		} catch(InterruptedException ie) {
			out.println("Interrupted: " + this);
		}
		catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		out.println(this + " off");
	}
	
	private synchronized void powerDown() throws InterruptedException {
		engage = false;
		assembler = null;
		robotPool.add(this);
		while (engage == false)
			wait();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
	
}


class EngineRobot extends Robot {

	EngineRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing engine.");
		assembler.getCar().addEngine();
	}
	
}


class DriveTrainRobot extends Robot {

	DriveTrainRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing drive train.");
		assembler.getCar().addDriveTrain();
	}
	
}


class WheelRobot extends Robot {

	WheelRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing wheels.");
		assembler.getCar().addWheels();
	}
	
}


class ExhaustSystemRobot extends Robot {

	ExhaustSystemRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing exhaust system.");
		assembler.getCar().addExhaustSystem();
	}
	
}


class BodyRobot extends Robot {

	BodyRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing body.");
		assembler.getCar().addBody();
	}
	
}


class FenderRobot extends Robot {

	FenderRobot(RobotPool robotPool) {
		super(robotPool);
	}

	@Override
	void performService() {
		out.println(this + " installing fenders.");
		assembler.getCar().addFenders();
	}
	
}


class RobotPool {
	private Set<Robot> pool = new HashSet<Robot>();
	
	synchronized void add(Robot robot) {
		pool.add(robot);
		notifyAll();
	}
	
	synchronized int size() {
		return pool.size();
	}
	
	synchronized void hire(Class<? extends Robot> robotType, Assembler assembler) throws InterruptedException {
		for (Robot r : pool) {
			if (r.getClass().equals(robotType)) {
				pool.remove(r);
				r.assignAssembler(assembler);
				r.engage();
				return;
			}
		}
		while (size() == 0)
			wait();
		hire(robotType, assembler);
	}
	
}


public class CarBuilder {

	public static void main(String[] args) throws InterruptedException {
		CarQueue chassisQueue = new CarQueue();
		CarQueue finishingQueue = new CarQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		RobotPool robotPool = new RobotPool();
		exec.execute(new EngineRobot(robotPool));
		exec.execute(new DriveTrainRobot(robotPool));
		exec.execute(new WheelRobot(robotPool));
		exec.execute(new ExhaustSystemRobot(robotPool));
		exec.execute(new BodyRobot(robotPool));
		exec.execute(new FenderRobot(robotPool));
		exec.execute(new Assembler(chassisQueue, finishingQueue, robotPool));
		exec.execute(new Reporter(finishingQueue));
		exec.execute(new ChassisBuilder(chassisQueue));
//		TimeUnit.SECONDS.sleep(7);
//		exec.shutdownNow();
	}

}
