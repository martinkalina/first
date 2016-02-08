package cz.jpower8.scheduler.model;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public abstract class AbstractTimer implements ITrigger {

	private static final Logger log = LoggerFactory.getLogger(AbstractTimer.class);
	private String calendarName;
	
	
	
	@Override
	public void register(IScheduler scheduler, String taskId) {
		if (scheduler instanceof QuartzDelegate){
			ScheduleBuilder<?> schedule = createTimeSchedule();
			TriggerBuilder<?> withSchedule = newTrigger().forJob(taskId)
					.withIdentity(taskId).withSchedule(schedule);
			if (calendarName != null){
				withSchedule.modifiedByCalendar(calendarName);
			}
			Trigger trigger = withSchedule.build();
			try {
				Date date = ((QuartzDelegate) scheduler).getQuartz().scheduleJob(trigger);
				log.debug("Task '{}' time scheduled, next run on {}", taskId, date);
				return ;
			} catch (SchedulerException e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("Unknown scheduler type: " + scheduler.getClass());
	}

	protected abstract ScheduleBuilder<? extends Trigger> createTimeSchedule();

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	

}
