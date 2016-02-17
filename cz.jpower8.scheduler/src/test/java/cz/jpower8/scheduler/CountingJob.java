package cz.jpower8.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CountingJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(CountingJob.class);
	
	public static final String JOB_COUNTER = "counting-job-counter";
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		int count;
		if (jobDataMap.containsKey(JOB_COUNTER)){
			count = jobDataMap.getInt(JOB_COUNTER);
		} else {
			count = 0;
		}
		log.info("Execution no.: " + count++);
		jobDataMap.put(JOB_COUNTER, count);
	}

}
