package cz.jpower8.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.CronTimer;
import cz.jpower8.scheduler.model.Task;

public class QuartzDelegate implements IScheduler {

	private Scheduler quartz;

	public QuartzDelegate() {

		try {
			StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void schedule(Task task) {
		
		try {
			@SuppressWarnings("unchecked")
			JobDetail job = newJob((Class<? extends Job>)Class.forName(task.getJobClass())).withIdentity(task.getId()).build();
			
			if (task.getTrigger() instanceof AbstractTimer){
				Trigger trigger = createTrigger(task.getId(), (AbstractTimer) task.getTrigger());
					quartz.scheduleJob(job, trigger);
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
					.withSchedule(CronScheduleBuilder.cronSchedule(t.getCronExpression())).startNow().build();
		}
		throw new IllegalArgumentException("Unknown Timer type: " + timer.getClass());
	}

}
