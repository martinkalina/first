package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlElement;

public class CronTimer extends AbstractTimer {

	private String expression;
	public CronTimer(){
	}
	public CronTimer(String expression){
		this.expression = expression;
	}
	@XmlElement
	public String getCronExpression() {
		return expression;
	}

	public void setCronExpression(String cronExpression) {
		this.expression = cronExpression;
	}

}
