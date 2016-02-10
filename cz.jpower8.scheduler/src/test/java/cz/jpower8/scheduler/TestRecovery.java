package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

/**
 * This test methods has to be run separate, to first simulate system failure in
 * {@link #systemFailure()}, and then {@link #recover()} in new JVM.
 */
public class TestRecovery extends JobTestSupport {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void systemFailure() throws Exception {
		new JdbcHelper().dropDb();
		new JdbcHelper().createDb();
		QuartzDelegate quartzDelegate = new QuartzDelegate("quartz-jdbc-store.properties");
		quartzDelegate.getQuartz().clear();
		Task task = new Task("failing-task");
		task.setRecovery(true);
		task.setJobClass(getClass().getName());
		task.setTrigger(new Now());
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(500);
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
