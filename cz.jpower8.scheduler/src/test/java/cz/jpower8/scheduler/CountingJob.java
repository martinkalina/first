package cz.jpower8.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
public class CountingJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(CountingJob.class);
	
	private static final String COUNTING_JOB_TIMER = "counting-job-timer";
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int count;
		if (jobDataMap.containsKey(COUNTING_JOB_TIMER)){
			count = jobDataMap.getInt(COUNTING_JOB_TIMER);
		} else {
			count = 0;
		}
		log.info("Execution no.: " + count++);
		jobDataMap.put(COUNTING_JOB_TIMER, count);
	}

}
