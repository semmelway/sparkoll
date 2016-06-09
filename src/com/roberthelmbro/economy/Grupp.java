package com.roberthelmbro.economy;

/**
 * @author Robert Helmbro 
 */

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.util.CalendarUtil;

public class Grupp extends Post {
	static final long serialVersionUID = 0;

	private Vector<ValuePost> poster = new Vector<ValuePost>();
	private ValuePost total;

	// konstruktor
	public Grupp(String name) {
		super(name);
		total = new ValuePost("total", "");
	}

	public Grupp(JSONObject json) throws JSONException, MalformedURLException {
		super("");		
		JSONArray jPoster = json.getJSONArray("poster");
		for (int i = 0; i < jPoster.length(); i++) {
			switch((String)jPoster.getJSONObject(i).get("class")) {
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
		name = json.getString("name");
	}
	
	@Override
	public JSONObject getJson() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray posterJsonArray = new JSONArray();
		for (Post post : poster) {
			posterJsonArray.put(post.getJson());
		}
		json.put("poster", posterJsonArray);
		json.put("name", name);
		json.put("class", "Grupp");
		return json;
	}

	// funktioner
	public void addPost(ValuePost post) {
		poster.addElement(post);
	}

	public Vector<ValuePost> getPoster() {
		return poster;
	}
	
	public List<ValuePost> getPosts(Calendar from, Calendar to) {
		List<ValuePost> posts = new LinkedList<ValuePost>();
		for (ValuePost post : poster) {
			if (post.isActive(from, to)) {
				posts.add(post);
			}
		}
		return posts;
	}
	
	public boolean contains(String label) {
		for(int i = 0; i < poster.size(); i++){
			if(poster.get(i).name.equals(label)) {
				return true;
			}
		}
		return false;
	}

	public void removePost(String name) {
		for (int i = 0; i < poster.size(); i++) {
			
			if (poster.elementAt(i).getName().equals(name))
				poster.remove(i);
		}
	}

	public double getTotalAmount(Calendar from, Calendar to) {
		double sum = 0;
		for (int index = 0; index < poster.size(); index++) {
			sum += ((Post) poster.elementAt(index)).getTotalAmount(from, to);

		}
		return sum;
	}
	
	@Override
	public double getValue(Calendar date) {
		double sum = 0;
		for (int index = 0; index < poster.size(); index++) {
			sum += poster.elementAt(index).getValue(date);
		}
		return sum;
	}
	
	@Override
	public double getLatestValue() {
		double sum = 0;
		for (int index = 0; index < poster.size(); index++) {
			sum += poster.elementAt(index).getLatestValue();
		}
		return sum;
	}
	
	@Override
	public double getMilestoneValue(Calendar date) {

		double sum = 0;
		for (int index = 0; index < poster.size(); index++) {
			sum += poster.elementAt(index).getMilestoneValue(date);
		}
		return sum;
	}
	
	public boolean isMilestoneDate(Calendar cal) {
		for (MileStone milestone : total.getMilestones()) {
			if(milestone.getTimeInMillis() == cal.getTimeInMillis()) {
				return true;
			}
		} 
		return false;
	}

	public void updateTotal(Calendar from, Calendar to) {
		if(total == null) {
			total = new ValuePost("total","");
		} else {
			total.deleteAllHappenings();
			total.mMilestones = new Vector<MileStone>();
		}
		double totalValue = 0;
		double fromMileStoneValue = 0;
		double toMileStoneValue = 0;
		Calendar tempDate = null;
		ValuePost post;

		for (int i = 0; i < poster.size(); i++) {
			post = poster.elementAt(i);
			//Uppdate happenings
			for (int j = 0; j < post.getNumberOfHappenings(); j++) {
				Calendar tempHapppeningDate = post.getHappeningDate(j);
				if(CalendarUtil.isOnOrBetween(tempHapppeningDate, from, to)) {
					double tempAmount = post.getHappeningAmount(j);
					total.addHappening(tempHapppeningDate, tempAmount, "");
				}
			}
			// Update total value
			double tempValue = post.getLatestValue();
			tempDate = post.getLastUppdateDate();
			totalValue += tempValue;
			
			// Update milestones
			fromMileStoneValue += post.getMilestoneValue(from);
			toMileStoneValue += post.getMilestoneValue(to);
			
		}
		total.setMilestone(from, fromMileStoneValue);
		total.setMilestone(to, toMileStoneValue);
		
		total.setValue(tempDate, totalValue);
	}

	public void updateTotalValue(Calendar date) {
		double totalValue = 0;
		Calendar tempDate = null;
		for (int i = 0; i < poster.size(); i++) {
			totalValue += ((ValuePost) poster.elementAt(i)).getLatestValue();
			tempDate = ((ValuePost) poster.elementAt(i)).getLastUppdateDate();
		}
		total.setValue(tempDate, totalValue);
	}
	
	@Override
	public double getInterest(Calendar from, Calendar to) {
		double returnValue = total.getInterest(from, to);
		return returnValue;
		
	}

	public void addHappening(String name, Calendar date, double amount, String comment) {
		if (name != null) {
			for (ValuePost post : poster) {
				if (post.getName().equals(name)) {
					post.addHappening(date, amount, comment);
				}
			}
		}
		total.addHappening(date, amount, "");
	}
	
	public List<Happening> getHappenings(Calendar from, Calendar to) {
		return total.getHappenings(from, to);
	}
	
	public boolean isActive(Calendar from, Calendar to) {
		// Check number of happenings
		if (getHappenings(from, to).size() > 0) {
			return true;
		}
		// Check value
		if (getValue(to) > 1 || getValue(from) > 1) {
			return true;
		}
		// Then its not active
		return false;
	}

	@Override
	public void uppdateraVarde(KalkylUI kalkylUI, Calendar from, Calendar to) {
		for (ValuePost childPost : poster) {
			if (childPost.isActive(from, to))
			childPost.uppdateraVarde(kalkylUI, from, to);
		}
	}

}// class
