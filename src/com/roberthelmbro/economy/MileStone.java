package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.roberthelmbro.util.CalendarUtil;

public class MileStone extends GregorianCalendar implements Serializable {
	
	static final long serialVersionUID = 0;
	
	private double value;
	
	//For debug
	String readableDate = "not set";
	
	public MileStone() {
		super();
	}
	
	public MileStone clone() {
		MileStone clone = new MileStone(this);
		clone.setValue(value);
		return clone;
	}
	
	public MileStone(Calendar date){
		super();
		if(date != null) {
			this.setTimeInMillis(date.getTimeInMillis());
		} else {
			this.setTimeInMillis(CalendarUtil.getTodayCalendarWithClearedClock().getTimeInMillis());
		}
		readableDate = CalendarUtil.getShortString(this);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
		readableDate = CalendarUtil.getShortString(this);
	}
	
	public boolean equals(Calendar cal) {
		return cal.getTimeInMillis() == this.getTimeInMillis();
	}
}
