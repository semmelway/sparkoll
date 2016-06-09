/**
 * @author Robert Helmbro
 */
package com.roberthelmbro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import com.roberthelmbro.economy.CurrencyUtil;

public class GoldPriceFetcher {
	
	private static double KG_PER_UNS = 0.0311;
	
	private static double PURITY = 0.75;
	
	private static double priceCashe = 0;
	
	public static double fetch() throws IOException, MalformedURLException {
		if (priceCashe == 0) {
			double usdPerOuncePrice = sendRequest("https://www.quandl.com/api/v1/datasets/BUNDESBANK/BBK01_WT5511.csv");
			priceCashe = CurrencyUtil.getMultiplicator("USD/SEK")*PURITY*usdPerOuncePrice/KG_PER_UNS;
		}
		return priceCashe;
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
		String firstLine = buffer.readLine();
		System.out.println(firstLine);
		String secondLine = buffer.readLine();
		double result = parseLine(secondLine);

		return result;
	}
	
	
	private static double parseLine(String line) {
		StringTokenizer parts = new StringTokenizer(line, ",");
		parts.nextToken();
		return Double.parseDouble(parts.nextToken());
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
