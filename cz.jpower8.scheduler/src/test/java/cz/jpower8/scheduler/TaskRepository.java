package cz.jpower8.scheduler;

import java.util.ArrayList;
import java.util.Collection;

import cz.jpower8.scheduler.model.And;
import cz.jpower8.scheduler.model.CronTimer;
import cz.jpower8.scheduler.model.LocalFileExist;
import cz.jpower8.scheduler.model.Not;
import cz.jpower8.scheduler.model.Task;

public class TaskRepository {

	public static Collection<Task> TASKS = new ArrayList<Task>();

	static {
		
		Task task = new Task();
		task.setId("simple_task");
		CronTimer timer = new CronTimer("*/5 * * ? * *");
		task.setTrigger(timer);
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
		
		task = new Task();
		task.setId("task_with_files");
		timer = new CronTimer("*/10 * * ? * *");
		task.setTrigger(timer);
		task.setCondition(new And().add(new LocalFileExist(("d:/j2ee/test/file1.txt"))).add(new Not(new LocalFileExist(("d:/j2ee/test/file2.txt")))));
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
		
	}
	
	
}
