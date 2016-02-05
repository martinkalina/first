package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlElementRef;

import cz.jpower8.scheduler.model.condition.True;

public class Task {
	
	private String id;
	private ITrigger trigger;
	private ICondition condition = new True();

	private String jobClass;

	public Task() {
	}

	public Task(String id) {
		this.id = id;
	}
	

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public ITrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(ITrigger trigger) {
		this.trigger = trigger;
	}

	@XmlElementRef
	public ICondition getCondition() {
		return condition;
	}

	public void setCondition(ICondition condition) {
		this.condition = condition;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

}
