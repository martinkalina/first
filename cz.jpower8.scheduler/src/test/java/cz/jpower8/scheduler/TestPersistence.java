package cz.jpower8.scheduler;

import org.junit.Test;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestPersistence {

	
	@Test
	public void testPersistence1() throws InterruptedException, SchedulerException {
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.getQuartz().clear(); // persistent quartz must be cleared
		Task task = new Task("persistent");
		task.setJobClass(CountingJob.class.getName());
		task.setTrigger(new SimpleTimer(5, 5));
		quartzDelegate.schedule(task);
		
		quartzDelegate.start();
		
		//just run once, then shutdown
		Thread.sleep(1000);
	}
	
	@Test
	public void testPersistence2() throws InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.start();
		// run  4 remaining rounds
		Thread.sleep(1000*30);
	}

}
