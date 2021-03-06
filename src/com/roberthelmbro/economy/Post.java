package com.roberthelmbro.economy;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Robert Helmbro 
 */

public abstract class Post implements Serializable {
	static final long serialVersionUID = 1;

	protected String name;

	public Post(String name){
		this.name = name;
	}
	//funktioner
	public String getName()
	{
		return name;
	}
	
	public abstract void uppdateraVarde(KalkylUI kalkylUI, Calendar from, Calendar to);
	
	public abstract boolean isActive(Calendar from, Calendar to);
	
	public abstract List<Happening> getHappenings(Calendar from, Calendar to);
	
	public abstract double getTotalAmount(Calendar to, Calendar from);

	public abstract double getValue(Calendar date);
	
	public abstract double getLatestValue();
	
	public abstract double getMilestoneValue(Calendar date);
	
	public abstract boolean isMilestoneDate(Calendar date);
	
	public abstract double getInterest(Calendar from, Calendar to);
	
	public abstract JSONObject getJson() throws JSONException ;
}//class
