package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestSchedulePersistence extends JobTestSupport {

	@Before
	public void setup(){
		new DbHelper().dropDb();
		new DbHelper().createDb();
	}
	
	@Test
	public void testSchedulePersistence() throws InterruptedException, SchedulerException {
		QuartzDelegate quartz1 = new QuartzDelegate("quartz-jdbc-store.properties");
		quartz1.getQuartz().clear(); // persistent quartz must be cleared
		Task task = new Task("persistent");
		task.setJobClass(TestSchedulePersistence.class.getName());
		SimpleTimer trigger = new SimpleTimer(2, 2);
		trigger.setFireImmediateAfterMisfire(true);
		task.setTrigger(trigger);
		quartz1.schedule(task);
		quartz1.start();
		//just run once, then shutdown
		Thread.sleep(100);
		quartz1.shutDown();
		Assert.assertEquals(1, getExecuted());
		
		//create new
		QuartzDelegate quartz2 = new QuartzDelegate("quartz-jdbc-store.properties");
		Assert.assertNotEquals(quartz2, quartz1); // it must NOT return old scheduler
		quartz2.start(); //should run 'persistent' task 2 more times
		Thread.sleep(5000);
		Assert.assertEquals(3, getExecuted());
		
	}
	

}
