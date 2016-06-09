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
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;
import com.roberthelmbro.util.CalendarUtil;


public class AktiePost extends ValuePost {
	static final long serialVersionUID = 1;
	
	double kurs;
	int antal;
	private URL uppdateringsUrl=null;

	
	//konstruktor
	public AktiePost(String namn, String groupName, int antal, double kurs, Calendar date, URL url)
	{
		super(namn, groupName);
		lastUppdateDate=date;
		this.kurs = kurs;
		this.antal = antal;
		uppdateringsUrl = url;
		value = this.kurs*this.antal;
		
		happenings = new Vector<Happening>();
		Happening temp = new Happening(date,this.kurs*this.antal,"Köp");
		happenings.addElement(temp);
	}

	public AktiePost(JSONObject json) throws JSONException, MalformedURLException {
		super("", "");
		name = json.getString("name");
		value = json.getDouble("value");
		groupName = json.getString("groupName");
		JSONArray jHappenings = json.getJSONArray("happenings");
		for (int i = 0; i < jHappenings.length(); i++) {
			happenings.add(new Happening(jHappenings.getJSONObject(i)));
		}
		lastUppdateDate = CalendarUtil.parseMillis(json.getLong("lastUpdateDate"));
		JSONArray jMilestones = json.getJSONArray("milestones");
		for (int i = 0; i < jMilestones.length(); i++) {
			mMilestones.add(new MileStone(jMilestones.getJSONObject(i)));
		}
		kurs = json.getDouble("kurs");
		antal = json.getInt("antal");
		uppdateringsUrl = new URL(json.getString("uppdateringsUrl"));
	}
	
	@Override
	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("value", value);
		json.put("groupName", groupName);
		JSONArray jHappenings = new JSONArray();
		for (Happening happening : happenings) {
			jHappenings.put(happening.getJson());
		}
		json.put("happenings", jHappenings);
		json.put("lastUpdateDate", lastUppdateDate.getTimeInMillis());
		JSONArray jMilestones = new JSONArray();
		for (MileStone milestone : mMilestones) {
			jMilestones.put(milestone.getJson());
		}
		json.put("milestones", jMilestones);
		json.put("kurs", kurs);
		json.put("antal", antal);
		json.put("uppdateringsUrl", uppdateringsUrl.toString());
		json.put("class", "AktiePost");
		return json;
	}

	@Override
	public void uppdateraVarde(KalkylUI kalkylUI, Calendar from, Calendar to) {
		try {
			System.out.println("Doing update for " + name);
			String rawData = readSource();
			System.out.println(rawData);
			StringTokenizer parseResult = new StringTokenizer(rawData, ",");

			parseResult.nextToken();//kastar första
			Calendar date = CalendarUtil.getTodayCalendarWithClearedClock();
			
			String wantedString = parseResult.nextToken();
			System.out.println("Parsed string: " + wantedString);
			
			setValue(date, wantedString);
		}catch(NoSuchElementException e ){
			try{new UppdateraVardeUI(this, kalkylUI);}
			catch(ClassNotFoundException d){}
			catch(IOException c){}
		}
	}
	
	public void setValue(Calendar date, String priceString) {

		System.out.println("setValue, price = " + priceString);
		System.out.println("number of = " + antal);
		
		// Transform to SEK if fortum
		double price = Double.parseDouble(priceString);
		
		
		if (name.equals("Fortum")) {
			if (CurrencyUtil.getMultiplicator("EUR/SEK") != -1) {
				price = price * CurrencyUtil.getMultiplicator("EUR/SEK");
			} else {
				return;
			}
		} else if (name.equals("Disney")) {
			if (CurrencyUtil.getMultiplicator("USD/SEK") != -1) {
				price = price * CurrencyUtil.getMultiplicator("USD/SEK");
			} else {
				return;
			}
		}
		
		value = antal * price;
		
		System.out.println("Value is now " + value);
		
		lastUppdateDate = date;
	}
	
	private String readSource() {
		
		BufferedReader buffer= null;
		try {
			InputStream inputStream = uppdateringsUrl.openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			buffer = new BufferedReader(inputStreamReader);
			return buffer.readLine();
		}catch(IOException e){return "tom";}
		catch(NullPointerException ee){return "tom";}
	}
	
	public void setCount(int count) {
		antal = count;
		value = antal * kurs;
	}
	
	public int getCount() {
		return antal;
	}
	
	public void setUpdateUrl(URL url){
		uppdateringsUrl = url;
	}
	
	public double getPrice() {
		return kurs;
	}

	public URL getUpdateUrl() {
		return uppdateringsUrl;
	}
	

}//class
