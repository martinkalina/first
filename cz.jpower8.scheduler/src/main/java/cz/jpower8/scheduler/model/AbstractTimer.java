package cz.jpower8.scheduler.model;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

/**
 * Base class for Quartz time-based triggers.
 * 
 * @author Martin Kalina
 *
 */
public abstract class AbstractTimer implements ITrigger {

	private static final Logger log = LoggerFactory.getLogger(AbstractTimer.class);
	private String calendarName;
	private Date startTime;
	private boolean fireImmediateAfterMisfire = false;
	
	@Override
	public void register(IScheduler scheduler, String taskId) {
		if (scheduler instanceof QuartzDelegate){
			Scheduler quartz = ((QuartzDelegate) scheduler).getQuartz();
			try {
				Trigger trigger = quartz.getTrigger(TriggerKey.triggerKey(taskId));
				if (trigger == null){ // only trigger and schedule when no trigger found 
					ScheduleBuilder<?> schedule = createTimeSchedule();
					
					TriggerBuilder<?> triggerBuilder = newTrigger().forJob(taskId)
							.withIdentity(taskId).withSchedule(schedule);
					if (calendarName != null){
						triggerBuilder.modifiedByCalendar(calendarName);
					}
					if (startTime != null){
						triggerBuilder.startAt(startTime);
					}
					trigger = triggerBuilder.build();
					Date date = quartz.scheduleJob(trigger);
					log.debug("Task '{}' time scheduled, next run on {}", taskId, date);
				}
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new IllegalArgumentException("Unknown scheduler type: " + scheduler.getClass().getName());
		}
	}

	protected abstract ScheduleBuilder<? extends Trigger> createTimeSchedule();

	public String getCalendarName() {
		return calendarName;
	}

	/**
	 * Links to defined calendar.
	 * 
	 * @param calendarName
	 */
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isFireImmediateAfterMisfire() {
		return fireImmediateAfterMisfire;
	}
	
	/**
	 * For any scheduled timer, when missfire (=missed fire time) occurred, 
	 * we can fire the job immediately, or on next regular schedule(default).
	 * This somehow simplifies the rules available in Quartz, but should be enough.
	 * 
	 * @param fireImmediateAfterMisfire
	 */
	public void setFireImmediateAfterMisfire(boolean fireImmediateAfterMisfire) {
		this.fireImmediateAfterMisfire = fireImmediateAfterMisfire;
	}
	

}
