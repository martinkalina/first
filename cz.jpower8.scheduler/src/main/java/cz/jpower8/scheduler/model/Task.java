package cz.jpower8.scheduler.model;

import cz.jpower8.scheduler.model.condition.True;


/**
 * This is the root configuration class for the schedule of a job. 
 * It holds a trigger, a condition, a class name of the job. <br/>It works like:
 * 
 * <ol>
 * 	<li>When the trigger fires, the condition is evaluated.
 * 	<li>When the condition is true, the job is instantiated and run.
 * <ol>
 * @author Martin Kalina
 *
 */
public class Task {
	
	private String id;
	private ITrigger trigger ;
	private ICondition condition = new True();
	private boolean recovery;

	private String jobClass;

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


	public boolean isRecovery() {
		return recovery;
	}


	public void setRecovery(boolean recovery) {
		this.recovery = recovery;
	}

}
