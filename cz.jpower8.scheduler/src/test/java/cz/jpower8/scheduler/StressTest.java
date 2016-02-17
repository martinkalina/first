package cz.jpower8.scheduler;

import java.util.Set;

import org.junit.Test;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.trigger.SimpleTimer;
import cz.jpower8.scheduler.quartz.QuartzDelegate;

public class StressTest {
	
//	private static final Logger log = LoggerFactory.getLogger(StressTest.class);
	
	@Test
	public void test() throws Exception {
		new JdbcHelper().dropDb();
		new JdbcHelper().createDb();
		QuartzDelegate quartz = new QuartzDelegate("quartz-performance.properties");
		quartz.start();
		
		for(int i=0; i<1000; i++){
			Task task = new Task("Task_"+i);
			task.setDescription("This is a description of my task " + i);
			task.setJobClass(StressMeterJob.class.getName());
			task.addParameter(StressMeterJob.JOB_DURATION, (long)(100 * Math.random()));  //wait inside 0-100 ms
			task.setTrigger(new SimpleTimer(((int)(Math.random()*10))+1, ((int)(Math.random()*10))));
			quartz.schedule(task);
		}
	
		while(true){
			Set<TriggerKey> keys = quartz.getQuartz().getTriggerKeys(GroupMatcher.anyTriggerGroup());
			
			Thread.sleep(1000);
//			log.info("*** Living jobs :"  +  keys.size());
			if (keys.isEmpty()) break;
			
		}
	}


}
