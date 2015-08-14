package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
import java.io.Serializable;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.util.CalendarUtil;

public class Happening implements Serializable {
	static final long serialVersionUID = 0;
	private double amount;
	private Calendar date;
	private String comment;

	public Happening() {
		amount=0;
		comment="kommentar saknas";
		//sign=s;
	}

	public Happening(Calendar date, double amount,String comment) {
		this.date=date;
		this.amount=amount;
		this.comment=comment;
		//sign=s;
	}
	
	public Happening(JSONObject json) throws JSONException {
		amount = json.getDouble("amount");
		date = CalendarUtil.parseMillis(json.getLong("date"));
		comment = json.getString("comment");
	}

	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("amount", amount);
		json.put("date", date.getTime().getTime());
		json.put("comment", comment);
		return json;
	}
	
	public Calendar getDate() {
		return date;
	}
	public double getAmount() {
		return amount;
	}
	public String getKommentar() {
		return comment;
	}
}
