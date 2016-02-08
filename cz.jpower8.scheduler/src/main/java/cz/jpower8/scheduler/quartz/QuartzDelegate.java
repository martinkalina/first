package cz.jpower8.scheduler.quartz;

import static org.quartz.JobBuilder.newJob;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.TriggerListenerSupport;
import org.quartz.plugins.management.ShutdownHookPlugin;

import cz.jpower8.scheduler.IScheduler;
import cz.jpower8.scheduler.model.Task;

public class QuartzDelegate implements IScheduler {

//	private static final Logger log = LoggerFactory.getLogger(QuartzDelegate.class);
	
	private Scheduler quartz;

	
	public QuartzDelegate() {
		init();
	}

	private void init() {
		try {
			quartz = StdSchedulerFactory.getDefaultScheduler();
			new ShutdownHookPlugin().initialize("shutdown hook", quartz, null);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	public QuartzDelegate(String propertiesPath) {
		System.setProperty(org.quartz.impl.StdSchedulerFactory.PROPERTIES_FILE, propertiesPath);
		init();
	}

	@Override
	public void schedule(final Task task) {
		
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(task.getJobClass());
			final JobKey jobKey = JobKey.jobKey(task.getId());
			
			JobDetail jobDetail;
			if (quartz.getJobDetail(jobKey) != null){
				jobDetail = quartz.getJobDetail(jobKey) ;
			}  else {
				jobDetail = newJob(clazz).withIdentity(jobKey)
						.storeDurably()
						.build();
				quartz.addJob(jobDetail, false);
			}
			quartz.getListenerManager().addTriggerListener(new TriggerListenerSupport() {
				@Override
				public String getName() {
					return "Condition Evaluator";
				}
				@Override
				public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
					boolean jobFound = context.getJobDetail().getKey().equals(jobKey);
					boolean condition = task.getCondition().evaluate();
					return jobFound && !condition;
				}
			});
			
			task.getTrigger().register(this, task.getId());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cant load job class", e);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}


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

}
