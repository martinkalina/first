package cz.jpower8.scheduler.model;

import java.io.Serializable;
import java.util.HashMap;

import org.quartz.JobDetail;

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
	private String description;
	private ITrigger trigger ;
	private ICondition condition = new True();
	private boolean recovery;

	private String jobClass;
	private HashMap<String, Serializable> jobData = new HashMap<String, Serializable>();

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

	/**
	 * If set to true, the job is recovered once failed.
	 * @see JobDetail#requestsRecovery()
	 * @param recovery
	 */
	public void setRecovery(boolean recovery) {
		this.recovery = recovery;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public HashMap<String, Serializable> getJobData() {
		return jobData;
	}


	public void setJobData(HashMap<String, Serializable>data){
		this.jobData = data;
	}


	public void addParameter(String name, Serializable value) {
		jobData.put(name, value);
	};

}
