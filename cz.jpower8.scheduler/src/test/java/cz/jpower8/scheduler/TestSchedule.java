package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.jobs.NoOpJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.model.trigger.OnTaskExecuted;
import cz.jpower8.scheduler.model.trigger.OnTaskExecuted.Mode;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestSchedule extends JobTestSupport {
	
	private static final Logger log =  LoggerFactory.getLogger(TestSchedule.class);
	
	@Test
	public void testSimpleSchedule() throws InterruptedException {
		IScheduler quartzDelegate = new QuartzDelegate();
		Task task = new Task("simple");
		task.setJobClass(getClass().getName());
		task.setTrigger(new SimpleTimer(1, 1));
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(100); // wait a tick for letting job run
		log.info("Checking counter for 1");
		Assert.assertEquals(1, getExecuted() );
		log.info("Going to sleep a second...");
		Thread.sleep(1000); // wait a second for letting job run next time
		log.info("Checking counter for 2");
		Assert.assertEquals(2, getExecuted() );
		Thread.sleep(1000); // wait a second for ensure it will not repeat 2 times
		Assert.assertEquals(2, getExecuted() );
	}
	
	@Test
	public void testCronSchedule() throws InterruptedException {
		IScheduler quartzDelegate = new QuartzDelegate();
		Task task = new Task("cron");
		task.setJobClass(getClass().getName());
		task.setTrigger(new CronTimer("*/5 * * ? * *"));
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(4900); 
		log.info("Checking counter for 1");
		Assert.assertEquals(1, getExecuted() );
		log.info("Going to sleep 2 seconds...");
		Thread.sleep(5000); 
		log.info("Checking counter for 2");
		Assert.assertEquals(2, getExecuted() );
	}
	
	
	
	
	@Test
	public void testRunOnTaskFailure() throws InterruptedException {
		
		IScheduler quartzDelegate = new QuartzDelegate();

		// this task crashes
		Task task = new Task("task1");
		task.setJobClass(CrashJob.class.getName());
		task.setTrigger(new Now());
		quartzDelegate.schedule(task);
		
		//and this will run then
		task = new Task("crash_depend");
		task.setJobClass(getClass().getName());
		OnTaskExecuted trigger = new OnTaskExecuted("task1");
		trigger.setMode(Mode.ON_FAILURE);
		task.setTrigger(trigger);
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(1000);
		Assert.assertEquals(1, getExecuted());
	}
	
	@Test
	public void testRunOnTaskSuccess() throws InterruptedException {
		
		IScheduler quartzDelegate = new QuartzDelegate();

		// this task crashes
		Task task = new Task("task1");
		task.setJobClass(NoOpJob.class.getName());
		task.setTrigger(new Now());
		quartzDelegate.schedule(task);
		
		//and this will run then
		task = new Task("crash_depend");
		task.setJobClass(getClass().getName());
		OnTaskExecuted trigger = new OnTaskExecuted("task1");
		trigger.setMode(Mode.ON_FAILURE);
		task.setTrigger(trigger);
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(1000);
		Assert.assertEquals(0, getExecuted());
		
	}

	

}
