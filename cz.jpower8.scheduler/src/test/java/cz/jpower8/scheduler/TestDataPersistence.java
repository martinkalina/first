package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestDataPersistence {

	private static final String TASK_NAME = "counting";

	private static final String TASK_DESCRIPTION = "This is a task for testing data persistence.";


	@Before
	public void init() {
		JdbcHelper jdbcHelper = new JdbcHelper();
		jdbcHelper.dropDb();
		jdbcHelper.createDb();
	}
	
	
	@Test
	public void testDataPersistence() throws InterruptedException, SchedulerException {
		QuartzDelegate quartz1 = new QuartzDelegate("quartz-jdbc-store.properties");
		quartz1.getQuartz().clear(); // persistent quartz must be cleared
		Task task = new Task(TASK_NAME);
		task.setDescription(TASK_DESCRIPTION);
		task.setJobClass(CountingJob.class.getName());
		task.setTrigger(new SimpleTimer(2, 2));
		quartz1.schedule(task);
		quartz1.start();
		//just run once, then shutdown
		Thread.sleep(100);
		JobDetail jobDetail = quartz1.getQuartz().getJobDetail(JobKey.jobKey(TASK_NAME));
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		int storedCount = jobDataMap.getInt(CountingJob.JOB_COUNTER);
		Assert.assertEquals(1, storedCount);
		quartz1.shutDown(); // cannot be used anymore
		
		//create new
		QuartzDelegate quartz2 = new QuartzDelegate("quartz-jdbc-store.properties");
		Assert.assertNotEquals(quartz2, quartz1); // must not return same object
		quartz2.start(); //should run 'persistent' task 2 more times
		Thread.sleep(4000);
		JobDetail jobDetail2 = quartz2.getQuartz().getJobDetail(JobKey.jobKey(TASK_NAME));
		String description2 = jobDetail2.getDescription();
		Assert.assertEquals(TASK_DESCRIPTION, description2);
		JobDataMap jobDataMap2 = jobDetail2.getJobDataMap();
		storedCount = jobDataMap2.getInt(CountingJob.JOB_COUNTER);
		Assert.assertEquals(3, storedCount);
	}

}
