package com.roberthelmbro.util;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class ParseCheckerTools {
	
	public static String checkDouble(String value) {
		try {
			ParseUtil.parseDouble(value);
		} catch (NumberFormatException e) {
			return "Du måste ange korekt värde. Värdet måste skrivas med siffror.";
		}
		if (value.length() == 0) {
			return "Du måste ange ett värde.";
		}
		return null;
	}
	
	public static String checkInt(String value) {
		try {
			ParseUtil.parseInt(value);
		} catch (NumberFormatException e) {
			return "Du måste ange korekt värde. Värdet måste vara ett heltal och skrivas med siffror.";
		}
		if (value.length() == 0) {
			return "Du måste ange ett värde.";
		}
		return null;
	}
	
	public static String checkValue(String value) {
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return "Du måste ange korekt värde. Värdet måste vara ett heltal och skrivas med siffror.";
		}
		if (value.length() == 0) {
			return "Du måste ange ett värde.";
		}
		return null;
	}//method
	
	public static String checkDate(String date) {
		// int year=0;
		int month = 0;
		int day = 0;
		if (date.length() != 10) {
			return "Du måste ange korrekt datum.";
		}//if
		try {
			StringTokenizer dateTokenizer = new StringTokenizer(date, "-");

			Integer.parseInt(dateTokenizer.nextToken());
			month = (int) Integer.parseInt(dateTokenizer.nextToken());
			day = (int) Integer.parseInt(dateTokenizer.nextToken());
		} catch (NoSuchElementException e) {
			return "Du måste ange korrekt datum.";
		}//catch

		if (!(1 <= month && month <= 12 && 1 <= day && day <= 31)) {
			return "Du måste ange korrekt datum.";
		}//if
		return null;
	}//method
}//class
