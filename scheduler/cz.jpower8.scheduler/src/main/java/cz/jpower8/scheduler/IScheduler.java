package cz.jpower8.scheduler;

import cz.jpower8.scheduler.model.Task;

public interface IScheduler {

	abstract void schedule(Task task);

}
