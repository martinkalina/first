package cz.jpower8.scheduler.model.trigger;

import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

import cz.jpower8.scheduler.model.AbstractTimer;

/**
 * Timer capable of repeatable triggering  with defined interval and optional count.
 * @author Martin Kalina
 *
 */
public class SimpleTimer extends AbstractTimer {

	private int intervalSeconds;
	private int repeatCount;
	
	public SimpleTimer(int intervalSeconds, int repeatCount) {
		this.intervalSeconds = intervalSeconds;
		this.repeatCount = repeatCount;
	}

	
	@Override
	protected ScheduleBuilder<? extends Trigger> createTimeSchedule() {
		if (intervalSeconds <= 0) {
			throw new RuntimeException();
		}
		SimpleScheduleBuilder simpleSchedule = SimpleScheduleBuilder.simpleSchedule();
		simpleSchedule.withIntervalInSeconds(intervalSeconds);
		simpleSchedule.withRepeatCount(repeatCount);
		return simpleSchedule;
	}

	public int getIntervalSeconds() {
		return intervalSeconds;
	}

	public void setIntervalSeconds(int intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	
}
