package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro 
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;
import com.roberthelmbro.util.CalendarUtil;

public class KontoPost extends ValuePost implements AddMilestoneUiListener
{
	static final long serialVersionUID = 0;

	//konstruktor
	
	public KontoPost(String name, String groupName) {
		super(name, groupName);
		}
	
	public KontoPost(String name, String groupName, double amount, Calendar date) {
		super(name, groupName);
		this.value=amount;
		lastUppdateDate=date;
		
		happenings=new Vector<Happening>();
		Happening temp = new Happening(date,this.value,"Startvärde");
		happenings.addElement(temp);
	}
	
	public KontoPost(JSONObject json) throws JSONException {
		super("","");
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

		json.put("class", "KontoPost");
		return json;
	}
	
	@Override
	public void uppdateraVarde(KalkylUI kalkylUI, Calendar from, Calendar to) {
		try {
		new UppdateraVardeUI(this, kalkylUI);
		}catch(IOException e){}
		catch(ClassNotFoundException ee){}
		}

}//class
