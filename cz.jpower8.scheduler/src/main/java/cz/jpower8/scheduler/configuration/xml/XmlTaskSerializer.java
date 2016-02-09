package cz.jpower8.scheduler.configuration.xml;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.configuration.ITaskSerializer;
import cz.jpower8.scheduler.model.AbstractConditionContainer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.calendar.AnnualCalendar;
import cz.jpower8.scheduler.model.calendar.CalendarDate;

public class XmlTaskSerializer implements ITaskSerializer<InputStream, OutputStream> {

	private final XStream xStream;

	public XmlTaskSerializer () {
		xStream = new XStream();
		
//		xStream.useAttributeFor(String.class);

		xStream.alias("task", Task.class);
		xStream.useAttributeFor(Task.class, "id");
		xStream.addImplicitCollection(AbstractConditionContainer.class, "children");
		
		xStream.alias("annual-calendar", AnnualCalendar.class);
		xStream.useAttributeFor(AnnualCalendar.class, "name");
		xStream.useAttributeFor(AnnualCalendar.class, "exclude");
		xStream.addImplicitCollection(AnnualCalendar.class, "dates", CalendarDate.class);
		xStream.alias("date", CalendarDate.class);
		xStream.registerConverter(new CalendarDateConverter());
	}
	
	
	@Override
	public Task loadTask(InputStream is) {
		return (Task) xStream.fromXML(is);
	}

	@Override
	public AnnualCalendar loadCalendar(InputStream is) {
		return (AnnualCalendar) xStream.fromXML(is);
	}

	@Override
	public void saveTask(Task task, OutputStream t) {
		xStream.toXML(task, t);
	}

	@Override
	public void saveCalendar(AnnualCalendar calendar, OutputStream t) {
		xStream.toXML(calendar, t);
	}

}
