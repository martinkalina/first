package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestSchedulePersistence extends JobTestSupport {

	@Test
	public void testSchedulePersistence() throws InterruptedException, SchedulerException {
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.getQuartz().clear(); // persistent quartz must be cleared
		Task task = new Task("persistent");
		task.setJobClass(TestSchedulePersistence.class.getName());
		task.setTrigger(new SimpleTimer(2, 2));
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		//just run once, then shutdown
		Thread.sleep(1000);
		quartzDelegate.shutDown();
		Assert.assertEquals(1, getExecuted());
		
		//create new
		QuartzDelegate quartz2 = new QuartzDelegate("quartz-jdbc-store.properties");
		quartz2.start(); //should run 'persistent' task 2 more times
		Thread.sleep(5000);
		Assert.assertEquals(3, getExecuted());
		
	}
	

}
