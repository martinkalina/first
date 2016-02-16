package cz.jpower8.scheduler;

import org.junit.Assert;
import org.junit.Test;

import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

/**
 * *IMPORTANT* The missfire threshold (org.quartz.jobStore.misfireThreshold) must be configured to 1000ms in properties.
 * 
 * @author Martin Kalina
 *
 */
public class TestMisfires extends JobTestSupport {

	
	@Test
	public void testDoFireCron() throws InterruptedException {
		AbstractTimer timer = new CronTimer("*/2 * * ? * *");
		timer.setFireImmediateAfterMisfire(true);
		testFire(timer, true);
	}
	@Test
	public void testDoFireSimple() throws InterruptedException {
		AbstractTimer timer = new SimpleTimer(2, 4);
		timer.setFireImmediateAfterMisfire(true);
		testFire(timer, true);
	}
	@Test
	public void testDontFireCron() throws InterruptedException {
		AbstractTimer timer = new CronTimer("*/2 * * ? * *");
		timer.setFireImmediateAfterMisfire(false);
		testFire(timer, false);
	}
	@Test
	public void testDontFireSimple() throws InterruptedException {
		AbstractTimer timer = new SimpleTimer(2, 4);
		timer.setFireImmediateAfterMisfire(false);
		testFire(timer, false);
	}

	private void testFire(AbstractTimer timer, boolean fire) throws InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		String taskId = "long-running-task";
		Task task = new Task(taskId);
		task.setJobClass(getClass().getName());
		int tick = 1;
		task.setTrigger(timer);
		quartzDelegate.schedule(task);
		
		quartzDelegate.start();
		Thread.sleep(tick * 1000);
		Assert.assertEquals(1, getExecuted());
		
		quartzDelegate.pauseTask(taskId);
		Thread.sleep(tick * 2000);
		Assert.assertEquals(1, getExecuted());
		
		quartzDelegate.resumeTask(taskId);
		Thread.sleep(10); 
		if (fire){
			Assert.assertEquals(2, getExecuted()); //do  fire immediately
			Thread.sleep(tick * 2000);
			Assert.assertEquals(3, getExecuted()); //do fire on next schedule
		} else {
			Assert.assertEquals(1, getExecuted()); //do NOT  fire immediately
			Thread.sleep(tick * 2000);
			Assert.assertEquals(2, getExecuted()); //do fire on next schedule
		}
		
	}

}
