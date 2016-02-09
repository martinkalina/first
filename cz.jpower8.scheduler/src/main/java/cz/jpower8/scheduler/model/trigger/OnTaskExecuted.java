package cz.jpower8.scheduler.model.trigger;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.listeners.JobListenerSupport;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.model.AbstractEvent;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

/**
 * Event fired on execution of some other task. 
 * Could optionally fire on success/failure only, and with interval delay. 
 * @author Martin Kalina
 *
 */
public class OnTaskExecuted extends AbstractEvent {

	public static enum Mode{ALWAYS, ON_SUCCESS, ON_FAILURE}
	
	private String taskId;
	private int waitSeconds = 0;
	private Mode mode = Mode.ALWAYS;
	
	public OnTaskExecuted(String taskToListen) {
		taskId = taskToListen;
	}
	

	@Override
	public void register(IScheduler scheduler, final String taskId) {
		if (scheduler instanceof QuartzDelegate){
			final Scheduler quartz = ((QuartzDelegate)scheduler).getQuartz();
			JobListener jobListener = new JobListenerSupport() {
				@Override
				public String getName() {
					return taskId + "_was_executed";
				}
				
				@Override
				public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
					if (!context.getJobDetail().getKey().getName().equals(OnTaskExecuted.this.taskId)){
						return;
					}
					if (mode == Mode.ON_SUCCESS && jobException != null){
						return;
					}
					if (mode == Mode.ON_FAILURE && jobException == null){
						return;
					}
					try {
						JobDetail jobToRun = quartz.getJobDetail(JobKey.jobKey(taskId));
						TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
								.forJob(jobToRun)
								.withSchedule(SimpleScheduleBuilder.simpleSchedule());
						SimpleTrigger trigger;
						if (waitSeconds == 0){
							trigger = triggerBuilder.startNow().build();
						} else {
							Date date = DateBuilder.futureDate(waitSeconds, IntervalUnit.SECOND);
							trigger = triggerBuilder.startAt(date).build();
						}
						quartz.scheduleJob(trigger);
						
					} catch (SchedulerException e) {
						throw new RuntimeException(e);
					}
				}
			};
			try {
				quartz.getListenerManager().addJobListener(jobListener);
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
			
		} else {
			throw new IllegalArgumentException("Unknown scheduler type: " + scheduler.getClass().getName());
		}
		
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getWaitSeconds() {
		return waitSeconds;
	}

	public void setWaitSeconds(int waitSeconds) {
		this.waitSeconds = waitSeconds;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

}
