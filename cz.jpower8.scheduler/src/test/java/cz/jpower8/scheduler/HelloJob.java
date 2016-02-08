package cz.jpower8.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(HelloJob.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    log.info("Hello Task {} !!!" , context.getJobDetail().getKey().getName());
	}

}
