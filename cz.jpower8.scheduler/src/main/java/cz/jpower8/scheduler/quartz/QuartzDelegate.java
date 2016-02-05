package cz.jpower8.scheduler.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.plugins.management.ShutdownHookPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.CronTimer;
import cz.jpower8.scheduler.model.Task;

public class QuartzDelegate implements IScheduler {

	private static final Logger log = LoggerFactory.getLogger(QuartzDelegate.class);
	
	private Scheduler quartz;

	public QuartzDelegate() {
		try {
			quartz = StdSchedulerFactory.getDefaultScheduler();
			new ShutdownHookPlugin().initialize("shutdown hook", quartz, null);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void schedule(Task task) {
		
		try {
			JobDetail wrap = newJob(JobConditionWrapper.class).withIdentity(task.getId()).build();
			wrap.getJobDataMap().put(JobConditionWrapper.WRAPPED_JOB_CLASS, Class.forName(task.getJobClass()));
			wrap.getJobDataMap().put(JobConditionWrapper.TASK_CONDITION, task.getCondition());
			wrap.getJobDataMap().put(JobConditionWrapper.TASK_ID, task.getId());
			
			if (task.getTrigger() instanceof AbstractTimer){
				Trigger trigger = createTrigger(task.getId(), (AbstractTimer) task.getTrigger());
				log.debug("Scheduling a task:" + task.getId());
				quartz.scheduleJob(wrap, trigger);
			}
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cant load job class", e);
		} catch (SchedulerException e) {
			throw new RuntimeException("Cant schedule job", e);
		}
		

	}

	private Trigger createTrigger(String id, AbstractTimer timer) {
		if (timer instanceof CronTimer) {
			
			CronTimer t = (CronTimer) timer;
			return newTrigger().withIdentity(id)
					.withSchedule(CronScheduleBuilder.cronSchedule(t.getExpression())).startNow().build();
		}
		throw new IllegalArgumentException("Unknown Timer type: " + timer.getClass());
	}

	public void start() {
		try {
			quartz.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}
