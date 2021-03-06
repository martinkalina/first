package cz.jpower8.scheduler.quartz;

import static org.quartz.JobBuilder.newJob;

import java.util.Calendar;
import java.util.Collection;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.TriggerListenerSupport;
import org.quartz.plugins.management.ShutdownHookPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.calendar.AnnualCalendar;
import cz.jpower8.scheduler.model.calendar.CalendarDate;

/**
 * Scheduler implementation based on Quartz scheduler.
 *  
 * @author Martin Kalina
 *
 */
public class QuartzDelegate implements IScheduler {

	private static final Logger log = LoggerFactory.getLogger(QuartzDelegate.class);
	
	private Scheduler quartz;

	
	public QuartzDelegate() {
		init();
		try {
			quartz.clear();// when no persistence is used, clear (for test purposes mainly)
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	private void init() {
		
		try {
			log.info("Scheduler start");
			quartz = StdSchedulerFactory.getDefaultScheduler();
			
			//shutdown scheduler on system shutdown
			ShutdownHookPlugin shutdownHookPlugin = new ShutdownHookPlugin();
			shutdownHookPlugin.setCleanShutdown(false);
			shutdownHookPlugin.initialize("shutdown hook", quartz, null);
			
//			//add db history
//			if (JobStoreSupport.class.isAssignableFrom(quartz.getMetaData().getJobStoreClass())){
//				JdbcHistoryLogger jdbcHistoryLogger = new JdbcHistoryLogger();
//				jdbcHistoryLogger.initialize("", scheduler, loadHelper);
//			}
//			
			
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public QuartzDelegate(String propertiesPath) {
		System.setProperty(org.quartz.impl.StdSchedulerFactory.PROPERTIES_FILE, propertiesPath);
		log.info("Quartz properties: "+ propertiesPath);
		init();
	}

	@Override
	public void schedule(final Task task) {
		
		log.info("Scheduling task "+task.getId());
		try {
			final JobKey jobKey = JobKey.jobKey(task.getId());
			JobDetail jobDetail;
			if (quartz.getJobDetail(jobKey) != null){
				// if the job was persisted and we are recovering from system crash, just load the job, and add the listener
				jobDetail = quartz.getJobDetail(jobKey) ;
			}  else {
				// add the new job
				@SuppressWarnings("unchecked")
				Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(task.getJobClass());
				JobBuilder jobBuilder = newJob(clazz)
						.withIdentity(jobKey)
						.withDescription(task.getDescription())
						.storeDurably();
				
				jobBuilder.usingJobData(new JobDataMap(task.getJobData()));
				
				if (task.isRecovery()){
					jobBuilder.requestRecovery(true);
				}
				jobDetail = jobBuilder
						.build();
				quartz.addJob(jobDetail, false);
			}

			// add listener for evaluating the condition before launch the job
			quartz.getListenerManager().addTriggerListener(new ConditionEvaluator(task));

			// register the trigger
			task.getTrigger().register(this, task.getId());
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cant load job class", e);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}


	private final class ConditionEvaluator extends TriggerListenerSupport {
		private Task task;
	
		private ConditionEvaluator(Task task) {
			this.task = task;
		}
	
		@Override
		public String getName() {
			return "Condition Evaluator for " + task.getId();
		}
	
		@Override
		public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
			boolean jobFound = context.getJobDetail().getKey().equals(JobKey.jobKey(task.getId()));
			boolean condition = task.getCondition().evaluate();
			return jobFound && !condition;
		}
	}
	
	@Override
	public void start() {
		try {
			quartz.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Scheduler getQuartz() {
		return quartz;
	}

	public void addCalendar(AnnualCalendar calendar) {
		log.info("Add calendar " + calendar);
		Collection<CalendarDate> dates = calendar.getDates();
		org.quartz.impl.calendar.AnnualCalendar qc = new org.quartz.impl.calendar.AnnualCalendar();
		for (CalendarDate calendarDate : dates) {
			Calendar day =  Calendar.getInstance();
			day.set(Calendar.DAY_OF_MONTH, calendarDate.getDay());
			day.set(Calendar.MONTH, calendarDate.getMonth() - 1);
			qc.setDayExcluded(day , calendar.isExclude());
		}
		
		try {
			quartz.addCalendar(calendar.getName(), qc, true, true);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void shutDown() {
		log.info("Scheduler shutdown");
		try {
			quartz.shutdown();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public void pauseTask(String taskId) {
		log.info("Pause task " + taskId);
		try {
			quartz.pauseJob(JobKey.jobKey(taskId));
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public void resumeTask(String taskId) {
		log.info("Resume task " + taskId);
		try {
			quartz.resumeJob(JobKey.jobKey(taskId));
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}
