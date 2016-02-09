package cz.jpower8.scheduler;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to be able to assert job runs from test cases.
 * 
 * @author Martin Kalina
 *
 */
public abstract class JobTestSupport implements Job {

	private static final Logger log = LoggerFactory.getLogger(JobTestSupport.class);

	private static final AtomicInteger executed = new AtomicInteger();

	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Test Task executed, count:" + executed.incrementAndGet());
	}

	@Before
	public final void init() {
		executed.set(0);
	}
	
	protected static final int getExecuted(){
		return executed.get();
	}

}
