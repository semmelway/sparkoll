package com.roberthelmbro.economy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyUtil {
	
	public static final String EUR = "EUR";
	
	public static final String SEK = "SEK";
	
	public static final String GOLD = "XAU";
	
	public static final String GOOGLE_BASE_URI = "http://www.google.com.br/ig/calculator?q=";
	
	public static double transform(String from, String to, double amount) throws IOException, MalformedURLException, JSONException {
		
		String urlString = buildUrlString(from, to, amount);
		String jsonResponse = sendRequest(urlString);
		
		JSONObject jsonObject = new JSONObject(jsonResponse);
		
		Response response = new Response(jsonObject);
		
		return response.getResponseValue();
		
	}
	
	private static String buildUrlString(String from, String to, double amount) {
		// Example:
		// http://www.google.com.br/ig/calculator?q=1EUR=?SEK
		
		StringBuilder url = new StringBuilder(GOOGLE_BASE_URI);
		url.append(amount);
		url.append(from);
		url.append("=?");
		url.append(to);
		return url.toString();
		
	}
	
	private static String sendRequest(String urlString) throws MalformedURLException, IOException {
		BufferedReader buffer= null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
		
		URL url = new URL(urlString);
		inputStream = url.openStream();
		inputStreamReader = new InputStreamReader(inputStream);
		buffer = new BufferedReader(inputStreamReader);
		return buffer.readLine();
		} finally {
			if (buffer != null) buffer.close();
			if (inputStream != null) inputStream.close();
			if (inputStreamReader != null) inputStreamReader.close();
		}
		
		
		
	}
	

	public static class Response {
		
		private final JSONObject jsonResponse;
		
		private static final String VALUE_KEY = "rhs";
		
		public static final String SEK_UNIT = " Swedish kronor";
		
		public Response(JSONObject jsonObject) {
			jsonResponse = jsonObject;
		}
		
		public double getResponseValue() throws JSONException {
			String valueString = jsonResponse.getString(VALUE_KEY);
			// Remove the unit
			String value = valueString.replace(SEK_UNIT, "");
			System.out.println("value = " + value);
			value = value.replace(" ", "");// In case this format: 1 200 300
			System.out.println("value = " + value);
			return Double.parseDouble(value);
		}
	}
	
	public static void main(String[] args) {
		
		try {
			System.out.println(transform(GOLD, SEK, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
