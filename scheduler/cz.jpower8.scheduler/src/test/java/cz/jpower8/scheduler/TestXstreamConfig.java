package cz.jpower8.scheduler;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.model.CronTimer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.True;

public class TestXstreamConfig {

	@Test
	public void test() throws Exception {
		Task task = new Task();
		task.setId("task_1");
		CronTimer timer = new CronTimer("* * * * *");
		task.setTrigger(timer);
		task.setCondition(new True());
		task.setJobClass(HelloJob.class.getName());
		
		String xml = marshall(task);
		System.out.println(xml);
		Task task2 = unmarshall(xml);
		System.out.println(task2.getId());
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
