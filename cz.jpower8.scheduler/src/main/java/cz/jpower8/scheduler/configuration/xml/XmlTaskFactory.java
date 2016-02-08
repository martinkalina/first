package cz.jpower8.scheduler.configuration.xml;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.configuration.ITaskFactory;
import cz.jpower8.scheduler.model.AbstractConditionContainer;
import cz.jpower8.scheduler.model.Task;

public class XmlTaskFactory implements ITaskFactory {

	private final XStream xStream;

	public XmlTaskFactory () {
		xStream = new XStream();
		
		xStream.alias("task", Task.class);
//		xStream.useAttributeFor(String.class);
		xStream.useAttributeFor(Task.class, "id");
		xStream.addImplicitCollection(AbstractConditionContainer.class, "children");
	}
	
	public XStream getXStream() {
		return xStream;
	}
	
	@Override
	public Task createTask(InputStream is) {
		return (Task) xStream.fromXML(is);
	}

}
