package cz.jpower8.scheduler.model.trigger;

import javax.xml.bind.annotation.XmlElement;

import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;

import cz.jpower8.scheduler.model.AbstractTimer;

public class CronTimer extends AbstractTimer {

	private String expression;
	public CronTimer(){
	}
	public CronTimer(String expression){
		this.expression = expression;
	}
	@XmlElement
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
