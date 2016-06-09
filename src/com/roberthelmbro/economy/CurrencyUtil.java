package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyUtil {
	
	// USE http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json&view=basic instead
	
	public static final String EUR = "EUR";
	
	public static final String SEK = "SEK";
	
	public static final String GOLD = "XAU";
	
	public static final String GOOGLE_BASE_URI = "http://www.google.com.br/ig/calculator?q=";
	
	private static Map<String, Double> prices = new HashMap<String, Double>();
	
	private static boolean refreshPrices() {
		prices = new HashMap<String, Double>();
		try {
			String respString = sendRequest(
					"http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json&view=basic");
			JSONObject currencyDataJson = new JSONObject(respString);
			JSONArray currenciesJson = currencyDataJson.getJSONObject("list").getJSONArray("resources");
			
			JSONObject currencyEntry;
			for (int i = 0; i < currenciesJson.length(); i++) {
				currencyEntry = currenciesJson.getJSONObject(i).getJSONObject("resource").getJSONObject("fields");
				prices.put(currencyEntry.getString("name"), Double.parseDouble(currencyEntry.getString("price")));
			}
			prices.put("EUR/SEK", prices.get("USD/SEK")/prices.get("USD/EUR"));
			return true;
		} catch(Throwable t) {
			t.printStackTrace();
			return false;
		}
	}
	
	public static double getMultiplicator(String symbol) {
		if (!prices.containsKey(symbol)) {
			refreshPrices();
		}
		if (prices.containsKey(symbol)) {
			return prices.get(symbol);
		}
		
		return -1;
	}
	
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
		String readLine = null;
		StringBuilder resp = new StringBuilder("");
		try {
		
		URL url = new URL(urlString);
		inputStream = url.openStream();
		inputStreamReader = new InputStreamReader(inputStream);
		buffer = new BufferedReader(inputStreamReader);
		
		readLine = buffer.readLine();
		while(readLine != null) {
			resp.append(readLine);
			readLine = buffer.readLine();
		}
		return resp.toString();
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
			value = value.replace("�", "");// In case this format: 1 200 300
			System.out.println("value = " + value);
			return Double.parseDouble(value);
		}
	}
	
	public static void main(String[] args) {
		
		refreshPrices();
		
//		try {
//			System.out.println(transform(GOLD, SEK, 1));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static String DUMMY_DATA = "{\"list\" : { \"meta\" : { \"type\" : \"resource-list\",\"start\" : 0,\"count\" : 174},\"resources\" : [{\"resource\" : { \"classname\" : \"Quote\",\"fields\" : { \"change\" : \"0.000080\",\"chg_percent\" : \"0.008919\",\"name\" : \"USD/EUR\",\"price\" : \"0.891584\",\"symbol\" : \"EUR=X\",\"ts\" : \"1463652773\",\"type\" : \"currency\",\"utctime\" : \"2016-05-19T10:12:53+0000\",\"volume\" : \"0\"}}},{\"resource\" : { \"classname\" : \"Quote\",\"fields\" : { \"change\" : \"-0.006249\",\"chg_percent\" : \"-0.074823\",\"name\" : \"USD/SEK\",\"price\" : \"8.346050\",\"symbol\" : \"SEK=X\",\"ts\" : \"1463652776\",\"type\" : \"currency\",\"utctime\" : \"2016-05-19T10:12:56+0000\",\"volume\" : \"0\"}}}]}}";
}
