package cz.jpower8.scheduler;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.CronTimer;

public class TestXstreamConfig {

	@Test
	public void testMarshaller() throws Exception {
		for (Task task : TaskRepository.TASKS) {
			String xml = marshall(task);
			System.out.println(xml);
			Task task2 = unmarshall(xml);
			Assert.assertEquals(task.getId(), task2.getId());
			Assert.assertEquals(((CronTimer)task.getTrigger()).getExpression(), ((CronTimer)task2.getTrigger()).getExpression());
			System.out.println("\n==========================================\n");
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
