package cz.jpower8.scheduler.model.calendar;

public class CalendarDate {
	
	private int month;
	private int day;
	
	/**
	 * @param month  1 based Month value 
	 * @param day 1 based Day value
	 */
	public CalendarDate(int month, int day) {
		this.month = month;
		this.day = day;
	}
	
	/**
	 * @return 1 based Month value
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * 
	 * @param month 1 based Month value
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * @return 1 based Day value
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day 1 based Day value
	 */
	public void setDay(int day) {
		this.day = day;
	}

}
