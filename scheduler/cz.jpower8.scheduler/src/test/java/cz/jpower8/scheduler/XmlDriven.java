package cz.jpower8.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class XmlDriven {

	public static void main(String[] args) throws Exception {
		 SchedulerFactory sf = new StdSchedulerFactory();
		 final Scheduler sched = sf.getScheduler();
		 sched.start();
	}

}
