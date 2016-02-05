package cz.jpower8.scheduler.quartz;

import java.text.MessageFormat;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.model.ICondition;

public final class JobConditionWrapper implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(JobConditionWrapper.class);
	
	static final String TASK_ID = "cz.jpower8.scheduler.task-id";
	static final String TASK_CONDITION = "cz.jpower8.scheduler.task-condition";
	static final String WRAPPED_JOB_CLASS = "cz.jpower8.scheduler.wrapped-job-class";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		ICondition condition = (ICondition) map.get(TASK_CONDITION);
		if (!condition.evaluate()){
			log.info("Condition '{}' is false for task '{}'", condition, map.getString(TASK_ID));
			return;
		}
		@SuppressWarnings("unchecked")
		Class<? extends Job> clazz = (Class<? extends Job>) map.get(WRAPPED_JOB_CLASS);
		try {
			Job job = clazz.newInstance();
			log.debug("Executing core job:" + map.getString(TASK_ID));
			job.execute(context);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
