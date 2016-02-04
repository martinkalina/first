package cz.jpower8.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(HelloJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		 // Say Hello to the World and display the date/time
	    log.info("Hello World! - " + new Date());
	}

}
