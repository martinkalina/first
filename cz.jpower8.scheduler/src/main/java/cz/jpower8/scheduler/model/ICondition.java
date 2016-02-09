package cz.jpower8.scheduler.model;

/**
 * Condition to be evaluated on Trigger fire, before the Job is run.
 * 
 * @author Martin Kalina
 *
 */
public interface ICondition {

	abstract boolean evaluate();
	
}
