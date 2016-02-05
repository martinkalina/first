package cz.jpower8.scheduler;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.model.CronTimer;
import cz.jpower8.scheduler.model.LocalFileExist;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.True;

public class TestXstreamConfig {

	@After
	public void shutdown(){
		System.out.println("\n==========================================\n");
	}
	@Test
	public void testMarshaller() throws Exception {
		for (Task task : TaskRepository.TASKS) {
			String xml = marshall(task);
			System.out.println(xml);
			Task task2 = unmarshall(xml);
			Assert.assertEquals(task.getId(), task2.getId());
			Assert.assertEquals(((CronTimer)task.getTrigger()).getExpression(), ((CronTimer)task2.getTrigger()).getExpression());
		}
	}

	private Task unmarshall(String xml) throws JAXBException {
		XStream xstream = new XStream();
		return (Task) xstream.fromXML(xml);
	}

	private String marshall(Task task) throws JAXBException {
		XStream xstream = new XStream();
		return xstream.toXML(task);
	}

}
