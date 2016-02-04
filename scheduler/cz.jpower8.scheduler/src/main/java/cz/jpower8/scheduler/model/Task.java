package cz.jpower8.scheduler.model;

import javax.xml.bind.annotation.XmlElementRef;

public class Task {
	
	private String id;
	private ITrigger trigger;
	private Condition condition;

	private String jobClass;

	
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
