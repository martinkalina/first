package cz.jpower8.scheduler;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.quartz.SchedulerException;

import com.thoughtworks.xstream.XStream;

import cz.jpower8.scheduler.configuration.xml.CalendarDateConverter;
import cz.jpower8.scheduler.configuration.xml.XmlCalendarFactroy;
import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.calendar.AnnualCalendar;
import cz.jpower8.scheduler.model.calendar.CalendarDate;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestCalendar {

	private static final String CZECH_HOLIDAYS = "czech-holidays";

	
	@Test
	public void testAnnual() throws SchedulerException, InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		quartzDelegate.addCalendar(createTestCalendar());
		Task task = new Task("calendar-task");
		task.setJobClass(HelloJob.class.getName());
		AbstractTimer st = new CronTimer("*/2 * * ? * *");
		st.setCalendarName(CZECH_HOLIDAYS);
		task.setTrigger(st);
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(1000*5);
	}


	private cz.jpower8.scheduler.model.calendar.AnnualCalendar createTestCalendar() {
		cz.jpower8.scheduler.model.calendar.AnnualCalendar myCal = new cz.jpower8.scheduler.model.calendar.AnnualCalendar(CZECH_HOLIDAYS);
		Calendar c = GregorianCalendar.getInstance();
		int d = c.get(Calendar.DAY_OF_MONTH);
		int m = c.get(Calendar.MONTH) + 1;
		myCal.addDate(new CalendarDate(m, d));
		return myCal;
	}
	
	@Test
	public void testConfigCzech(){
		
		AnnualCalendar myCal =  new AnnualCalendar("czech-holidays");
		myCal .addDate(new CalendarDate(1, 1));
		myCal.addDate(new CalendarDate(5, 1));
		myCal.addDate(new CalendarDate(5, 8));
		myCal.addDate(new CalendarDate(7, 5));
		myCal.addDate(new CalendarDate(7, 6));
		myCal.addDate(new CalendarDate(9, 28));
		myCal.addDate(new CalendarDate(10, 28));
		myCal.addDate(new CalendarDate(11, 17));
		myCal.addDate(new CalendarDate(12, 24));
		myCal.addDate(new CalendarDate(12, 25));
		myCal.addDate(new CalendarDate(12, 26));
		XStream xstream = new XmlCalendarFactroy().getXStream();
		xstream.alias("date", CalendarDate.class);
		xstream.registerConverter(new CalendarDateConverter());
		System.out.println(xstream.toXML(myCal));
	}

}
