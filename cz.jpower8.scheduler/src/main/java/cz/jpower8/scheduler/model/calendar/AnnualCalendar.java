package cz.jpower8.scheduler.model.calendar;

import java.util.Collection;
import java.util.HashSet;

public class AnnualCalendar {
	
	private String name;
	private Collection<CalendarDate> dates  = new HashSet<CalendarDate>();
	private boolean exclude = true;

	public AnnualCalendar(String name) {
		this.name = name;
	}

	public Collection<CalendarDate> getDates() {
		return dates;
	}

	public void setDates(Collection<CalendarDate> dates) {
		this.dates = dates;
	}

	public void addDate(CalendarDate d){
		dates.add(d);
	}

	public boolean isExclude() {
		return exclude;
	}

	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
