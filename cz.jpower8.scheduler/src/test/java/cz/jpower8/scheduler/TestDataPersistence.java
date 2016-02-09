package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestDataPersistence {

	@Test
	public void testDataPersistence() throws InterruptedException, SchedulerException {
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.getQuartz().clear(); // persistent quartz must be cleared
		Task task = new Task("counting");
		task.setJobClass(CountingJob.class.getName());
		task.setTrigger(new SimpleTimer(2, 2));
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		//just run once, then shutdown
		Thread.sleep(100);
		JobDataMap jobDataMap = quartzDelegate.getQuartz().getJobDetail(JobKey.jobKey("counting")).getJobDataMap();
		int storedCount = jobDataMap.getInt(CountingJob.COUNTING_JOB_COUNTER);
		Assert.assertEquals(1, storedCount);
		quartzDelegate.shutDown();
		
		//create new
		quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.start(); //should run 'persistent' task 2 more times
		Thread.sleep(4000);
		jobDataMap = quartzDelegate.getQuartz().getJobDetail(JobKey.jobKey("counting")).getJobDataMap();
		storedCount = jobDataMap.getInt(CountingJob.COUNTING_JOB_COUNTER);
		Assert.assertEquals(3, storedCount);
	}

}
