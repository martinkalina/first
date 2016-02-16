package cz.jpower8.scheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestSchedule.class, 
				TestConditions.class,
				TestSchedulePersistence.class, 
				TestDataPersistence.class,
				TestCalendar.class, 
				TestManagingJobs.class,
				TestMisfires.class
				})
public class AllTests {

}
