package cz.jpower8.scheduler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import cz.jpower8.scheduler.configuration.xml.XmlTaskSerializer;
import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.condition.And;
import cz.jpower8.scheduler.model.condition.LocalFileExist;
import cz.jpower8.scheduler.model.condition.Not;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.model.trigger.OnTaskExecuted;

public class TestXstreamConfig {
	private static final Collection<Task> TASKS = new ArrayList<Task>();

	static {
		Task task = new Task("simple_task");
		AbstractTimer timer = new CronTimer("*/5 * * ? * *");
		task.setTrigger(timer);
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
		
		task = new Task("task_with_files");
		timer = new CronTimer("*/10 * * ? * *");
		task.setTrigger(timer);
		task.setCondition(new And().add(new LocalFileExist(("d:/j2ee/test/file1.txt"))).add(new Not(new LocalFileExist(("d:/j2ee/test/file2.txt")))));
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
		
		task = new Task("task_depends_on_simpletask");
		task.setTrigger(new OnTaskExecuted("simple_task"));
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
		
		task = new Task("task_depends_on_task_with_files");
		OnTaskExecuted trigger = new OnTaskExecuted("task_with_files");
		trigger.setWaitSeconds(2);
		task.setTrigger(trigger);
		task.setJobClass(HelloJob.class.getName());
		TASKS.add(task);
	}
	@Test
	public void testMarshaller() throws Exception {
		for (Task task : TASKS) {
			String xml = marshall(task);
			System.out.println(xml);
			Task task2 = unmarshall(xml);
			Assert.assertEquals(task.getId(), task2.getId());
			System.out.println("\n==========================================\n");
		}
	}

	private Task unmarshall(String xml) throws JAXBException {
		return new XmlTaskSerializer().loadTask(new ByteArrayInputStream(xml.getBytes()));
	}

	private String marshall(Task task) throws JAXBException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new XmlTaskSerializer().saveTask(task, baos);
		return baos.toString();
	}
	
	

}
