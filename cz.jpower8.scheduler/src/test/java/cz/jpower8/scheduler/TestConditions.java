package cz.jpower8.scheduler;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.condition.And;
import cz.jpower8.scheduler.model.condition.LocalFileExist;
import cz.jpower8.scheduler.model.condition.Not;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestConditions extends JobTestSupport {

	@Test
	public void testLocalFileExist() throws InterruptedException {
		Task task = new Task("task_with_files");
		task.setTrigger(new Now());
		String absolutePath = new File("src/test/java").getAbsolutePath();
		task.setCondition(new And().add(new LocalFileExist((absolutePath + "\\file1.txt")))
				.add(new Not(new LocalFileExist((absolutePath + "\\file2.txt")))));
		task.setJobClass(TestConditions.class.getName());
		QuartzDelegate scheduler = new QuartzDelegate();
		scheduler.schedule(task);
		scheduler.start();
		Thread.sleep(100);
		Assert.assertEquals(1, getExecuted());
		
	}

}
