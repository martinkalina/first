package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

/**
 * This test methods has to be run separate, to first simulate system failure in
 * {@link #systemFailure()}, and then {@link #recover()} in new JVM.
 */
public class TestRecovery extends JobTestSupport {

	private static final Logger log =  LoggerFactory.getLogger(TestRecovery.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
		try {
			log.info("Start sleeping in task thread before finsh...");
			Thread.sleep(1000);
			log.info("Task finished");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void systemFailure() throws Exception {
		new DbHelper().dropDb();
		new DbHelper().createDb();
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.getQuartz().clear();
		Task task = new Task("failing-task");
		task.setRecovery(true);
		task.setJobClass(getClass().getName());
		task.setTrigger(new Now());
		quartzDelegate.schedule(task);
		log.info("Task scheduled");
		quartzDelegate.start();
		log.info("Scheduler started");
		Thread.sleep(500);
		log.info("System shutdown initiated");
		System.exit(0);
		// run, but shut down during execution
	}

	@Test
	public void recover() throws InterruptedException {
		IScheduler quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.start();
		Thread.sleep(2 * 1000);
		Assert.assertEquals(1, getExecuted());
		// run it again
	}

}
