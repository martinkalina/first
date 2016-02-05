package cz.jpower8.scheduler.model;

import cz.jpower8.scheduler.IScheduler;

public interface ITrigger {

	void register(IScheduler scheduler, String taskId);

	
}  
