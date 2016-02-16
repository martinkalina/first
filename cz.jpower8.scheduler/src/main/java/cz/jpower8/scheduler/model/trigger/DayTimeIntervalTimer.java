package cz.jpower8.scheduler.model.trigger;

import java.util.Set;

import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;

import cz.jpower8.scheduler.model.AbstractTimer;



/**
 * Defines firing interval in specified week days, e.g. "fire each 30 minutes between 6:00 and 18:00 from Monday to Friday"
 * @author Martin Kalina
 *
 */
public class DayTimeIntervalTimer extends AbstractTimer {

	private TimeOfDay startTimeOfDay;
	private TimeOfDay endTimeOfDay;
	private int intervalInSeconds;
	private Set<Integer> daysOfWeek;
	
	@Override
	protected ScheduleBuilder<? extends Trigger> createTimeSchedule() {
		DailyTimeIntervalScheduleBuilder builder = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule();
		
		builder.withIntervalInSeconds(intervalInSeconds);
		
		if (startTimeOfDay != null){
			builder.startingDailyAt(startTimeOfDay);
		}
		
		if (endTimeOfDay != null){
			builder.endingDailyAt(endTimeOfDay);
		}
		if (daysOfWeek != null){
			builder.onDaysOfTheWeek(daysOfWeek);
		}
		if (isFireImmediateAfterMisfire()){
			builder.withMisfireHandlingInstructionFireAndProceed();
		} else {
			builder.withMisfireHandlingInstructionDoNothing();
		}
		return builder;
	}

	public TimeOfDay getStartTimeOfDay() {
		return startTimeOfDay;
	}

	public void setStartTimeOfDay(TimeOfDay startTimeOfDay) {
		this.startTimeOfDay = startTimeOfDay;
	}

	public TimeOfDay getEndTimeOfDay() {
		return endTimeOfDay;
	}

	public void setEndTimeOfDay(TimeOfDay endTimeOfDay) {
		this.endTimeOfDay = endTimeOfDay;
	}

	public int getIntervalInSeconds() {
		return intervalInSeconds;
	}

	public void setIntervalInSeconds(int intervalInSeconds) {
		this.intervalInSeconds = intervalInSeconds;
	}

	public Set<Integer> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(Set<Integer> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

}
