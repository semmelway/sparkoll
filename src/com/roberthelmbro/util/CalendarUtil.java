package com.roberthelmbro.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import com.roberthelmbro.economy.MileStone;

public class CalendarUtil {
	
	public static Calendar getTodayCalendarWithClearedClock(){
		GregorianCalendar now = new GregorianCalendar();
		now.setTimeInMillis(System.currentTimeMillis());
		
		GregorianCalendar nowWithClearedClock = new GregorianCalendar();
		
		nowWithClearedClock.clear();
		
		nowWithClearedClock.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		return nowWithClearedClock;
	}
	
	public static Calendar parseString(String dateSt) throws IllegalArgumentException{
		StringTokenizer dateT= new StringTokenizer(dateSt,"-");

		int year= (int)Integer.parseInt(dateT.nextToken());
		int month=(int)Integer.parseInt(dateT.nextToken());
		int day=  (int)Integer.parseInt(dateT.nextToken());
		
		if(month < 0 || month > 12 || day < 1 || day > 31){
			throw new IllegalArgumentException();
		}
		
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeZone(TimeZone.getDefault());
		date.clear();
		date.set(year, month-1, day);
		return date;
	}
	
	public static String getShortString(Calendar date) {
		String returnString = "";
		
		// Add year to the string
		returnString = returnString + date.get(Calendar.YEAR);
		
		// Add - sign
		returnString = returnString.concat("-");
		
		// Add month to the strin
		String monthString = "" + (date.get(Calendar.MONTH) + 1);
		if(monthString.length() == 1)
			monthString = "0" + monthString;
		returnString = returnString.concat(monthString);
		
		// Add - sign
		returnString = returnString.concat("-");
		
		// Add day to the string
		String dayString = "" + (date.get(Calendar.DAY_OF_MONTH));
		if(dayString.length() == 1)
			dayString = "0" + dayString;
		returnString = returnString.concat(dayString);
		
		
		return returnString;
	}
	
	public static String[] getShortStrings(Calendar[] dates, int from, int to) {
		String[] result = new String[to - from + 1];
		for(int i = from; i <= to; i++) {
			result[i - from] = getShortString(dates[i]);
		}
		return result;
	}
	
	public static Calendar[] add(Calendar[] dates, Calendar date) {
		
		if(dates.length == 0){
			Calendar milestone = new MileStone();
			milestone.clear();
			milestone.setTimeInMillis(date.getTimeInMillis());
			Calendar[] oneDateArray = new MileStone[1];
			oneDateArray[0] = milestone;
			return oneDateArray;
		} else {

			Calendar[] oneYearAdded = new MileStone[dates.length + 1];
			for (int i = 0; i < dates.length; i++) {
				oneYearAdded[i] = dates[i];
			}
			Calendar dateToAdd = new MileStone();
			dateToAdd.clear();
			int lastYearInDates = dates[dates.length -1].get(Calendar.YEAR);
			lastYearInDates++;
			dateToAdd.set(Calendar.YEAR, lastYearInDates);
			oneYearAdded[oneYearAdded.length -1] = dateToAdd;
			return oneYearAdded;
		}
	}
	
	public static Calendar[] addAnotherYear(Calendar[] dates) {
		
		if(dates.length == 0){
			Calendar year2010 = new MileStone();
			year2010.clear();
			year2010.set(Calendar.YEAR, 2010);
			Calendar[] oneYearArray = new MileStone[1];
			oneYearArray[0] = year2010;
			return oneYearArray;
		} else {
			Calendar[] oneYearAdded = new MileStone[dates.length + 1];
			for (int i = 0; i < dates.length; i++) {
				oneYearAdded[i] = dates[i];
			}
			Calendar dateToAdd = new MileStone();
			dateToAdd.clear();
			int lastYearInDates = dates[dates.length -1].get(Calendar.YEAR);
			lastYearInDates++;
			dateToAdd.set(Calendar.YEAR, lastYearInDates);
			oneYearAdded[oneYearAdded.length -1] = dateToAdd;
			return oneYearAdded;
		}
	}
	
	public static boolean isOnOrBetween(Calendar date, Calendar from, Calendar to) {
		boolean afterFrom = isAfter(date, from);//date.getTimeInMillis() > from.getTimeInMillis();
		boolean beforeTo = isBefore(date, to);//date.getTimeInMillis() < to.getTimeInMillis();
		boolean onFrom = isSameDay(date, from);//date.getTimeInMillis() == from.getTimeInMillis();
		boolean onTo = isSameDay(date, to);//date.getTimeInMillis() == to.getTimeInMillis();
		
		if((afterFrom && beforeTo) || onFrom || onTo) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isOnFromOrBetween(Calendar date, Calendar from, Calendar to) {
		boolean afterFrom = isAfter(date, from);//date.getTimeInMillis() > from.getTimeInMillis();
		boolean beforeTo = isBefore(date, to);//date.getTimeInMillis() < to.getTimeInMillis();
		boolean onFrom = isSameDay(date, from);//date.getTimeInMillis() == from.getTimeInMillis();
		
		if((afterFrom && beforeTo) || onFrom) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Map<MileStone, Long> getMap(Vector<MileStone> vector) {
		
		Map<MileStone, Long> map = new HashMap<MileStone, Long>();
		for (int i = 0; i < vector.size(); i++) {
			map.put(vector.get(i), vector.get(i).getTimeInMillis());
		}
		return map;
		
	}
	
	public static boolean isBefore(Calendar first, Calendar second) {
		if (first.get(Calendar.YEAR) < second.get(Calendar.YEAR)) {
			return true;
		} else if(first.get(Calendar.YEAR) == second.get(Calendar.YEAR)) {
			if (first.get(Calendar.MONTH) < second.get(Calendar.MONTH)) {
				return true;
			} else if (first.get(Calendar.MONTH) == second.get(Calendar.MONTH)) {
				if (first.get(Calendar.DAY_OF_MONTH) < second.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isAfter(Calendar first, Calendar second) {
		if (first.get(Calendar.YEAR) > second.get(Calendar.YEAR)) {
			return true;
		} else if(first.get(Calendar.YEAR) == second.get(Calendar.YEAR)) {
			if (first.get(Calendar.MONTH) > second.get(Calendar.MONTH)) {
				return true;
			} else if (first.get(Calendar.MONTH) == second.get(Calendar.MONTH)) {
				if (first.get(Calendar.DAY_OF_MONTH) > second.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isSameDay(Calendar first, Calendar second) {
		if (first.get(Calendar.YEAR) == second.get(Calendar.YEAR)) {
			if (first.get(Calendar.MONTH) == second.get(Calendar.MONTH)) {
				if (first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH)) {
					return true;
					
				}
				
			}
		}
		return false;
	}
}
