package cz.jpower8.scheduler.model.trigger;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;

import cz.jpower8.scheduler.model.AbstractTimer;

/**
 * Uses Cron-like expression. It's using quartz cron expression implementation.
 * 
 * @see CronExpression
 * @author Martin Kalina
 *
 */
public class CronTimer extends AbstractTimer {

	private String expression;

	public CronTimer(String expression){
		this.expression = expression;
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String cronExpression) {
		this.expression = cronExpression;
	}
	protected ScheduleBuilder<? extends Trigger> createTimeSchedule() {
		return CronScheduleBuilder.cronSchedule(expression);
	}
	
}
