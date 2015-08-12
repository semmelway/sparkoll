/**
 * @author Robert Helmbro
 */
package com.roberthelmbro.util;

public class ParseUtil {
	
	public static int parseInt(String number) throws NumberFormatException {
		if (number.contains(".") || number.contains(",")) {
			throw new NumberFormatException("number may not contains '.' or ','");
		}
		number = removeSpace(number);
		return Integer.parseInt(number);
	}
	
	public static double parseDouble(String number) throws NumberFormatException {
		number = removeSpace(number);
		number = replaceCommaChar(number);
		return Double.parseDouble(number);
	}
	
	public static String removeSpace(String org) {
		return org.replaceAll(" ", "");
	}
	
	public static String replaceCommaChar (String org) {
		return org.replace(',', '.');
	}
	
	public static void main(String[] args) {
		
		// Tests for parseInt
		String[] testStrings = {"12 000,45", "10 000 000", "16 000", "12.3", "23 400.45"};
		int[] expectedInts = {-1, 10000000, 16000, -1, -1};
		for (int i = 0; i < testStrings.length; i++) {
			try {
				System.out.print(testStrings[i] + " -> " + parseInt(testStrings[i]));
				System.out.println(" ## pass? " + (parseInt(testStrings[i]) == expectedInts[i]));
			} catch(NumberFormatException ne) {
				if (expectedInts[i] == -1) {
					System.out.println(testStrings[i] + " ## exception thrown as expected" );

				} else {
					System.out.println(testStrings[i] + " ## not expected exception thrown" );
				}
			}
		}
		
		// Tests for parseDouble
		double[] expectedDoubles = {12000.45d, 10000000d, 16000, 12.3d, 23400.45d};
		for (int i = 0; i < testStrings.length; i++) {
			System.out.print(testStrings[i] + " -> " + parseDouble(testStrings[i]));
			System.out.println(" ## pass? " + (parseDouble(testStrings[i]) == expectedDoubles[i]));
		}
		
		
		
		
		
	}

}
