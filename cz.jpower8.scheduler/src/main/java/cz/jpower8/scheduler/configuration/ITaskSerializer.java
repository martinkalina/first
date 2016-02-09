package cz.jpower8.scheduler.configuration;

import cz.jpower8.scheduler.model.Task;
import cz.jpower8.scheduler.model.calendar.AnnualCalendar;

/**
 * Interface for serializing / deserializing Task and Calendar.
 *  
 * @author Martin Kalina
 *
 * @param <T>
 * @param <S>
 */
public interface ITaskSerializer<T,S> {
	
	Task loadTask(T t);
	
	void saveTask(Task task, S t);
	
	AnnualCalendar loadCalendar(T is);
	
	void saveCalendar(AnnualCalendar calendar, S t);
	

}
