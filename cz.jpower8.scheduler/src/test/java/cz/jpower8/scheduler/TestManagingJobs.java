package cz.jpower8.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.jobs.NoOpJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.model.trigger.Now;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;
public class TestManagingJobs extends JobTestSupport {
	
	private static final Logger log =  LoggerFactory.getLogger(TestManagingJobs.class);
	
	@Test
	public void testListDetails() throws InterruptedException, SchedulerException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		scheduleTasks(quartzDelegate);
		Scheduler quartz = quartzDelegate.getQuartz();
		dump(quartz);
		quartzDelegate.start();
		for (int i = 0; i < 20; i++) {
			Thread.sleep(500); // wait for some triggers
			dump(quartz);
		}

	}
	
	@Test
	public void testPauseAndResume() throws InterruptedException, SchedulerException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		String taskId = "task-to-control";
		Task task = new Task(taskId);
		task.setDescription("Run infinite times interval 1 seconds");
		task.setJobClass(this.getClass().getName());
		task.setTrigger(new SimpleTimer(2, -1));
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(500);
		Assert.assertEquals(1, getExecuted()); // task run once
		quartzDelegate.pauseTask(taskId);
		int executed1 = getExecuted();
		Thread.sleep(4000); // MUST NOT run during this period
		int executed2 = getExecuted();
		Assert.assertEquals(executed2, executed1);
		quartzDelegate.resumeTask(taskId);
		quartzDelegate.start();
		Thread.sleep(3000); // MUST run during this period
		int executed3 = getExecuted();
		Assert.assertTrue(executed3 > executed2);
	}
	
	

	private void dump(Scheduler quartz) throws SchedulerException {
		Set<JobKey> jobKeys = quartz.getJobKeys(GroupMatcher.anyJobGroup());
		for (JobKey jobKey : jobKeys) {
			JobDetail jobDetail = quartz.getJobDetail(jobKey);
			List<? extends Trigger> triggersOfJob = quartz.getTriggersOfJob(jobKey);
			log.info("Stored job id={} description={}, triggering by: ", jobDetail.getKey().getName(), jobDetail.getDescription());
			for (Trigger trigger : triggersOfJob) {
				log.info("\t\tTrigger {}, last fired {}, next fire {}",
					trigger.getKey(),	
					format(trigger.getPreviousFireTime()),
					format(trigger.getNextFireTime())
					);
			}
		}
	}

	private Object format(Date d) {
		if (d == null) return "NULL";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(d);
	}

	private void scheduleTasks(QuartzDelegate quartzDelegate) {
			Task task = new Task("task_1");
			task.setDescription("Start now run once task");
			task.setJobClass(NoOpJob.class.getName());
			task.setTrigger(new Now());
			quartzDelegate.schedule(task);
			
			task = new Task("task_2");
			task.setDescription("Run each 3 seconds");
			task.setJobClass(NoOpJob.class.getName());
			task.setTrigger(new CronTimer("* * * ? * *"));
			quartzDelegate.schedule(task);
			
			task = new Task("task_3");
			task.setDescription("Run infinite times interval 2 seconds");
			task.setJobClass(NoOpJob.class.getName());
			task.setTrigger(new SimpleTimer(2, -1));
			quartzDelegate.schedule(task);
			
			
			
			
	}

}
