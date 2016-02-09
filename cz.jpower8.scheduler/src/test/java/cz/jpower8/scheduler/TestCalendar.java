package cz.jpower8.scheduler;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.SchedulerException;

import cz.jpower8.scheduler.configuration.xml.XmlTaskSerializer;
import cz.jpower8.scheduler.model.AbstractTimer;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.calendar.AnnualCalendar;
import cz.jpower8.scheduler.model.calendar.CalendarDate;
import cz.jpower8.scheduler.model.trigger.CronTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class TestCalendar extends JobTestSupport {


	
	@Test
	public void testAnnual() throws SchedulerException, InterruptedException {
		QuartzDelegate quartzDelegate = new QuartzDelegate();
		String name = "test-calendar";
		//create calendar with current and next date
		AnnualCalendar calendar = createTestCalendar(name);
		quartzDelegate.addCalendar(calendar);
		Task task = new Task("calendar-task");
		task.setJobClass(JobTestSupport.class.getName());
		AbstractTimer st = new CronTimer("*/2 * * ? * *");
		st.setCalendarName(name);
		task.setTrigger(st);
		quartzDelegate.schedule(task);
		quartzDelegate.start();
		Thread.sleep(1000 * 2);
		//asssert task is not executed
		Assert.assertEquals(0, getExecuted());
	}


	private cz.jpower8.scheduler.model.calendar.AnnualCalendar createTestCalendar(String name) {
		AnnualCalendar myCal = new AnnualCalendar(name);
		Calendar c = GregorianCalendar.getInstance();
		int d = c.get(Calendar.DAY_OF_MONTH);
		int m = c.get(Calendar.MONTH) + 1;
		myCal.addDate(new CalendarDate(m, d));
		c.add(Calendar.DATE, 1);
		d = c.get(Calendar.DAY_OF_MONTH);
		m = c.get(Calendar.MONTH) + 1;
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new XmlTaskSerializer().saveCalendar(myCal, baos);
		System.out.println(baos.toString());
	}

}
