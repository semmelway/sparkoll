package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.util.CalendarUtil;

public class Workspace implements Serializable {
	static final long serialVersionUID = 0;
	
	public Vector<Post> poster = new Vector<Post>();
	public Calendar lastUpdateDate;
	//public String path; 
	public Calendar showFrom;
	public Calendar showTo;
	
	public Workspace() {
		//path = new String();
		
		showFrom = new GregorianCalendar();
		showFrom.clear();
		showFrom.set(Calendar.YEAR, 2010);//deafult
		
		showTo = new GregorianCalendar();
		showTo.clear();
		showTo.set(Calendar.YEAR,2011);//deafult
	}
	
	public Workspace(JSONObject json) throws JSONException {
		JSONArray jPoster = json.getJSONArray("poster");
		try {
			for (int i = 0; i < jPoster.length(); i++) {
				switch((String)jPoster.getJSONObject(i).get("class")) {
				case "Grupp": 
					poster.add(new Grupp(jPoster.getJSONObject(i)));
					break;

				case "KontoPost":
					poster.add(new KontoPost(jPoster.getJSONObject(i)));
					break;

				case "RawMaterialPost":
					poster.add(new RawMaterialPost(jPoster.getJSONObject(i)));
					break;

				case "AktiePost": 
					poster.add(new AktiePost(jPoster.getJSONObject(i)));
					break;
				};
			}
		} catch(JSONException | MalformedURLException e) {
			e.printStackTrace();
		}
		lastUpdateDate = CalendarUtil.parseMillis(json.getLong("lastUpdateDate"));
		showFrom = CalendarUtil.parseMillis(json.getLong("showFrom"));
		showTo = CalendarUtil.parseMillis(json.getLong("showTo"));
	}
	
	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray posterJsonArray = new JSONArray();
		for (Post post : poster) {
			posterJsonArray.put(post.getJson());
		}
		json.put("poster", posterJsonArray);
		try {
		json.put("lastUpdateDate", lastUpdateDate.getTimeInMillis());
		} catch(NullPointerException npe) {
			json.put("lastUpdateDate", 0);
		}
		json.put("showFrom", showFrom.getTimeInMillis());
		json.put("showTo", showTo.getTimeInMillis());
		return json;
	}
}
