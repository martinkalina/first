package cz.jpower8.scheduler.configuration.xml;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.model.calendar.AnnualCalendar;
import cz.jpower8.scheduler.model.calendar.CalendarDate;

public class XmlCalendarFactroy {

	private final XStream xStream;

	public XmlCalendarFactroy () {
		xStream = new XStream();
		
		xStream.alias("annual-calendar", AnnualCalendar.class);
		xStream.alias("date", CalendarDate.class);
	}
	
	public XStream getXStream() {
		return xStream;
	}}
