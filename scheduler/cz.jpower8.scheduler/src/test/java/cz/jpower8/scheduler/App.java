package cz.jpower8.scheduler;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.ScheduleBuilder.*;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.core.jmx.TriggerSupport;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class App {
	
	  public void run() throws Exception {
		    Logger log = LoggerFactory.getLogger(App.class);

		    log.info("------- Initializing ----------------------");

		    // First we must get a reference to a scheduler
		    SchedulerFactory sf = new StdSchedulerFactory();
		    final Scheduler sched = sf.getScheduler();

		    log.info("------- Initialization Complete -----------");

		    // computer a time that is on the next round minute
		    Date runTime = evenMinuteDate(new Date());

		    log.info("------- Scheduling Job  -------------------");

		    // define the job and tie it to our HelloJob class
		    JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();

		    // Trigger the job to run on the next round minute
		    final Trigger trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(
		    		simpleSchedule().withIntervalInSeconds(6).withRepeatCount(10)).startNow().build();
		    
		    // Tell quartz to schedule the job using our trigger
		    sched.scheduleJob(job, trigger);
		    log.info(job.getKey() + " will run at: " + runTime);

		    
		    // Start up the scheduler (nothing can actually run until the
		    // scheduler has been started)
		    sched.start();

		    log.info("------- Started Scheduler -----------------");

		    // wait long enough so that the scheduler as an opportunity to
		    // run the job!
		    log.info("------- Waiting 65 seconds... -------------");
		    try {
		      // wait 65 seconds to show job
		      Thread.sleep(165L * 1000L);
		      // executing...
		    } catch (Exception e) {
		      //
		    }

		    // shut down the scheduler
		    log.info("------- Shutting Down ---------------------");
		    sched.shutdown(true);
		    log.info("------- Shutdown Complete -----------------");
		  }
	
	
	public static void main(String[] args) throws Exception {
			new App().run();
	}
}
