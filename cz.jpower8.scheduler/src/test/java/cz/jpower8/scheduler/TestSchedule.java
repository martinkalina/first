package cz.jpower8.scheduler;

import org.junit.Test;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.model.trigger.OnTaskExecuted;
import cz.jpower8.scheduler.model.trigger.OnTaskExecuted.Mode;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestSchedule {

	@Test
	public void test() throws InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		for (Task task : TaskRepository.TASKS) {
			quartzDelegate.schedule(task);
			quartzDelegate.start();
		}
		Thread.sleep(1000*60);
	}
	
	@Test
	public void testCrash() throws InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		Task task = new Task("crash");
		task.setJobClass(CrashJob.class.getName());
		task.setTrigger(new Now());
		quartzDelegate.schedule(task);
		
		task = new Task("crash_depend");
		task.setJobClass(HelloJob.class.getName());
		OnTaskExecuted trigger = new OnTaskExecuted("crash");
		trigger.setMode(Mode.ON_FAILURE);
		task.setTrigger(trigger);
		quartzDelegate.schedule(task);
		
		quartzDelegate.start();
		Thread.sleep(1000*10);
		
	}
	

}
