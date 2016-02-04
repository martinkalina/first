package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlElementRef;

import org.quartz.Job;

public class Task {
	
	private String id;
	private AbstractTrigger trigger;
	private Condition condition;

	private String jobClass;

	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public AbstractTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(AbstractTrigger trigger) {
		this.trigger = trigger;
	}

	@XmlElementRef
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

}
