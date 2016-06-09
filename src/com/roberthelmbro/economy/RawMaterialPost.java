package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 * @year 2012
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.GoldPriceFetcher;
import com.roberthelmbro.util.ParseUtil;

public class RawMaterialPost extends ValuePost
{
	static final long serialVersionUID = 4295043;
	
	// Price per kilo
	double price;
	
	// Number of kilos
	double weight;
	
	//konstruktor
	public RawMaterialPost(String namn, String groupName, double weight, double price, Calendar date) {
		super(namn, groupName);
		lastUppdateDate=date;
		this.price = price;
		this.weight = weight;
		value = this.price*this.weight;
		
		happenings = new Vector<Happening>();
		Happening temp = new Happening(date,this.price*this.weight,"Köp");
		happenings.addElement(temp);
	}
	
	public RawMaterialPost(JSONObject json) throws JSONException {
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
		weight = json.getDouble("weight");
		price = json.getDouble("price");
		
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
		json.put("price", price);
		json.put("weight", weight);
		json.put("class", "RawMaterialPost");
		return json;
	}

	@Override
	public void uppdateraVarde(KalkylUI kalkylUI, Calendar from, Calendar to) {
		try {
			if (name.equals("Guld")) {
				setValue(CalendarUtil.getTodayCalendarWithClearedClock(), "" + GoldPriceFetcher.fetch());
			} else {
				manualUpdate(kalkylUI);
			}
		} catch(Exception e ){
			manualUpdate(kalkylUI);
		} 
	}
	
	public void manualUpdate(KalkylUI kalkylUi) {
		try{new UppdateraVardeUI(this, kalkylUi);}
		catch(ClassNotFoundException d){}
		catch(IOException c){}
	}
	
	public void setValue(Calendar date, String priceString) {

		System.out.println("setValue, price = " + priceString);
		System.out.println("number of = " + weight);
		
		double price = ParseUtil.parseDouble(priceString);
		
		
		value = weight * price;
		
		System.out.println("Value is now " + value);
		
		lastUppdateDate = date;
	}
	
	public void setCount(int count) {
		weight = count;
		value = weight * price;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getPrice() {
		return price;
	}
}//class
