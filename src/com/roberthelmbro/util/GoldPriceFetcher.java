package com.roberthelmbro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GoldPriceFetcher {
	
	private static final String GOLD_PRICE_URL = "http://www.oanda.com/lang/sv/currency/table?exch=SEK&sel_list=XAU&value=1&format=ASCII&redirected=1";

	private static double KG_PER_UNS = 0.0311;
	
	private static double PURITY = 0.75;
	
	public static double fetch() throws IOException, MalformedURLException {
		//return 11204.7d/KG_PER_UNS;
		return PURITY*sendRequest(GOLD_PRICE_URL)/KG_PER_UNS;
	}


	private static double sendRequest(String urlString) throws MalformedURLException, IOException {
		BufferedReader buffer= null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
			buffer= null;
			URL url = new URL(urlString);
			inputStream = url.openStream();
			inputStreamReader = new InputStreamReader(inputStream);
			buffer = new BufferedReader(inputStreamReader);
			return parseBuffer(buffer);

		} finally {
			if (buffer != null) buffer.close();
			if (inputStream != null) inputStream.close();
			if (inputStreamReader != null) inputStreamReader.close();
		}
	}
	
	private static double parseBuffer(BufferedReader buffer) throws IOException {
		double result = -1;
		do {
			result = parseLine(buffer.readLine());
		} while (result == -1);
		return result;
	}
	
	private static double parseLine(String line) {
		if(line.contains("Guld (uns)")) {
			// Line found
			line = line.substring(line.indexOf("XAU"));
			line = line.replace("XAU", "");
			while(true) {
				if(line.charAt(0) == ' ') {
					line = line.substring(1);
				} else {
					break;
				}
			}
			line = line.substring(0,line.indexOf(" "));
			line = line.replace("<span>", "");
			line = line.replace("</span>", "");
			return ParseUtil.parseDouble(line);
		} else {
			return -1d;
		}
	}
	
	public static void main(String[] args) {
		
		try {
			//System.out.println(parseLine("                 <TR BGCOLOR=#000000><TR><TD BGCOLOR=#efefef><PRE><font face=Verdana size=2>XAU         11204<span>.</span>70000                     0.00009     Guld (uns)"));
			System.out.println(fetch());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
