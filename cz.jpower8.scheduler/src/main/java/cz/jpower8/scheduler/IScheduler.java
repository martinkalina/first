package cz.jpower8.scheduler;

import cz.jpower8.scheduler.model.Task;

/**
 * Interface to abstract from scheduler implementation.
 *  
 * @author Martin Kalina
 *
 */
public interface IScheduler {

	abstract void schedule(Task task);

	abstract void start();

	void shutDown();

}
