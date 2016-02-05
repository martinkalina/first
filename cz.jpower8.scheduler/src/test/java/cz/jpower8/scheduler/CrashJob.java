package cz.jpower8.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CrashJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		throw new JobExecutionException();
	}

}
