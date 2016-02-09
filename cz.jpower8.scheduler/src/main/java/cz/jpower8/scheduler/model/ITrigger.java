package cz.jpower8.scheduler.model;

import cz.jpower8.scheduler.IScheduler;


/**
 * Registers on scheduler and triggers the run of a Job.
 * 
 * @author Martin Kalina
 *
 */
public interface ITrigger {

	void register(IScheduler scheduler, String taskId);
	
}  
