package cz.jpower8.scheduler.model.trigger;

import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

import cz.jpower8.scheduler.model.AbstractTimer;

/**
 * Triggers the job instantly.
 * 
 * @author Martin Kalina
 *
 */
public class Now extends AbstractTimer {

	@Override
	protected ScheduleBuilder<? extends Trigger> createTimeSchedule() {
		SimpleScheduleBuilder simpleSchedule = SimpleScheduleBuilder.simpleSchedule();
		if (isFireImmediateAfterMisfire()){
			simpleSchedule.withMisfireHandlingInstructionNowWithExistingCount();
		} else {
			simpleSchedule.withMisfireHandlingInstructionNextWithRemainingCount();
		}
		return simpleSchedule;
	}

}
