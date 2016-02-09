package cz.jpower8.scheduler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCalendar.class, 
				TestDataPersistence.class, 
				TestSchedule.class, 
				TestSchedulePersistence.class, 
				TestConditions.class
				})
public class AllTests {

}
