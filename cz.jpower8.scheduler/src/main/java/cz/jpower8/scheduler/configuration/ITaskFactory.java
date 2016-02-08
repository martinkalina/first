package cz.jpower8.scheduler.configuration;

import java.io.InputStream;

import cz.jpower8.scheduler.model.Task;

public interface ITaskFactory {
	
	Task createTask(InputStream is);

}
