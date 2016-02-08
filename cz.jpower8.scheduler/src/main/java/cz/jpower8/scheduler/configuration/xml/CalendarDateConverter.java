package cz.jpower8.scheduler.configuration.xml;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import cz.jpower8.scheduler.model.calendar.CalendarDate;

public class CalendarDateConverter implements SingleValueConverter {
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return type.equals(CalendarDate.class);
	}

	@Override
	public String toString(Object obj) {
		CalendarDate calendarDate = (CalendarDate)obj;
		return calendarDate.getMonth() + "/" + calendarDate.getDay();
	}

	@Override
	public Object fromString(String str) {
		String[] dp = str.split("/");
		return new CalendarDate(Integer.parseInt(dp[0]), Integer.parseInt(dp[1]));
	}
}