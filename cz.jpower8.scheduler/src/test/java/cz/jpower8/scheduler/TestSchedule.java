package cz.jpower8.scheduler;

import org.junit.Test;

import cz.jpower8.scheduler.model.Task;
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

}
