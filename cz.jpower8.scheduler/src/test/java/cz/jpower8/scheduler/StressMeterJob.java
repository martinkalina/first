package cz.jpower8.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StressMeterJob implements Job {

	
	public static final String JOB_DURATION = "counting-job-delay";
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDetail jobDetail = context.getJobDetail();
		long scheduled = context.getScheduledFireTime().getTime();
		long fire = context.getFireTime().getTime();
		System.out.println(jobDetail.getKey().getName() + "\t" + (fire - scheduled));//print out for excel
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		if (jobDataMap.containsKey(JOB_DURATION)){
			try {
				long delay = jobDataMap.getLong(JOB_DURATION);
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				throw new JobExecutionException(e);
			}
		}
	}

}
