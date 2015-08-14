package com.roberthelmbro.economy;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.economy.ui.AddMilestoneUI;
import com.roberthelmbro.util.CalendarUtil;

/**
 * @author Robert Helmbro
 */
public class VärdePost extends Post implements AddMilestoneUiListener {
	static final long serialVersionUID = 1;
	
	protected double value;
	protected String groupName;
	protected Vector<Happening> happenings = new Vector<Happening>();
	protected Calendar lastUppdateDate;
	protected Vector<MileStone> mMilestones = new Vector<MileStone>();
	
	public VärdePost(String name, String groupName) {
		super(name);
		this.groupName = groupName;
	}
	
	public VärdePost(JSONObject json) throws JSONException {
		super("");
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

		return json;
	}

	
	public String getGroupName() {
		return groupName;
	}
	
	public void deleteHappening(Calendar date, double amount) {
		for(int i =0;i<happenings.size();i++)
		{
			Happening happening = happenings.elementAt(i);
			if (happening.getDate().getTimeInMillis() == date.getTimeInMillis()     )
				if(happening.getAmount() == amount) {
					happenings.removeElementAt(i);
					return;
				}
		}
	}
	
	public void deleteMilestone(Calendar date) {
		for(int i =0;i<mMilestones.size();i++)
		{
			MileStone milestone = mMilestones.elementAt(i);
			if (milestone.getTimeInMillis() == date.getTimeInMillis()) {
				mMilestones.removeElementAt(i);
				return;
			}
		}
	}
	
	public void uppdateraVarde(KalkylUI kalkylUI){}
	
	public void addMileStone(Calendar date) {
		new AddMilestoneUI(this, name, date,value);
	}
	
	public void deleteAllHappenings() {
		happenings.removeAllElements();
	}
	
	public Calendar getLastUppdateDate() {
		return lastUppdateDate;
	}
	
	public void addHappening(Calendar date, double amount, String comment) {
		Happening temp = new Happening(date,amount,comment);
		happenings.addElement(temp);
		sort();
	}
	
	public double getHappeningAmount(int i) {
		try {
			return ((Happening)happenings.elementAt(i)).getAmount();
		}
		catch(ArrayIndexOutOfBoundsException r){
			return 0;
		}
	}
	
	public double getTotalAmount(Calendar from, Calendar to) {
		double sum=0;
		Happening happening;
		for(int i =0;i<happenings.size();i++) {
			happening = happenings.elementAt(i);
			if(CalendarUtil.isOnOrBetween(happening.getDate(), from, to)){
				sum+=happening.getAmount();
			}
		}
		Calendar milestone;
		for(int i =0;i<mMilestones.size();i++) {
			milestone = mMilestones.get(i);
			if(CalendarUtil.isSameDay(milestone, from)) {
				sum += ((MileStone)milestone).getValue();
			}
		}
		return sum;
	}

	public Calendar getHappeningDate(int i) {
		try {
			return  ((Happening)happenings.elementAt(i)).getDate();
		}
		catch(ArrayIndexOutOfBoundsException re){return new GregorianCalendar();}
	}
	
	public String getHappeningComment(int i) {
	return   ((Happening)happenings.elementAt(i)).getKommentar();
	}
	
	public int getNumberOfHappenings() {
		try {
			return happenings.size();
		}catch(NullPointerException e){return 0;}
	}
	
	public List<Happening> getHappenings(Calendar from, Calendar to) {
		List<Happening> happeningsInRange = new LinkedList<Happening>();
		for (Happening happening : happenings) {
			if (CalendarUtil.isOnOrBetween(happening.getDate(), from, to)) {
				happeningsInRange.add(happening);
			}
		}
		return happeningsInRange;
	}
	
	public void sort() {		
		if(happenings.size() <= 1)
			return;
		
		for(int i=0;i<happenings.size();i++) {
			for(int j=i+1;j<happenings.size();j++) {
				if(getNumberOfDays(((Happening)happenings.elementAt(i)).getDate(),((Happening)happenings.elementAt(j)).getDate())<0) {
					Happening temp1 = new Happening();
					Happening temp2 = new Happening();
					
					temp1=(Happening)happenings.elementAt(i);
					temp2=(Happening)happenings.elementAt(j);
					happenings.removeElementAt(j);
					happenings.removeElementAt(i);
					
					try{happenings.add(i,temp2);}
					catch(ArrayIndexOutOfBoundsException e){happenings.addElement(temp2);}
					
					try{happenings.add(j+1,temp1);}
					catch(ArrayIndexOutOfBoundsException e){happenings.addElement(temp1);}
				}	
			}
		}
	}

	@Override
	public double getInterest(Calendar from, Calendar to) {
		if(lastUppdateDate == null) {
			return 0;
		}
		double value;
		Calendar date;
		if(to.getTimeInMillis() == lastUppdateDate.getTimeInMillis()) {
			value = this.value;
			date = lastUppdateDate;
		} else if (isMilestoneDate(to)) {
			value = getMilestoneValue(to);
			date  = to;
		} else {
			value = this.value;
			date = lastUppdateDate;
		}
		
		if(zeroInterrest(from, to)) {
			return 0;
		}
		double a=0.0000;
		double b=1.0100000;	
		double e=0.00000100;
		double m=0;
		Vector<Happening> happeningsInRange = createHappeningsInRange(from, to);
		
		// The algoritm handles value = 0 bad
		if (value == 0) value = 0.000001;
		
		boolean first=true;
		while(first) {
			m=(a+b)/2;
			
			if(funktionOf(m, happeningsInRange, value, date) ==0)	
				first=false;
			if(first) {
				if(((b-a)/2)<=e)
					first =false;
				if(first) {
					if(( this.funktionOf(a, happeningsInRange, value, date) ) * ( this.funktionOf(m,happeningsInRange, value, date) )   > 0     ) {
						a=m;
					}	
					else 
						b=m;
				}			
			}
		}
		return (Math.pow(m,365)-1)*100;
	}
	
	private boolean zeroInterrest(Calendar from, Calendar to) {
		double sum = 0;
		// Add all happenings in range
		for(int i = 0; i < happenings.size(); i++) {
			if(CalendarUtil.isOnOrBetween(happenings.elementAt(i).getDate(), from, to)){
				sum += happenings.elementAt(i).getAmount();
			}
		}		
		// Add initial value if present
		for(int i = 0; i < mMilestones.size(); i++) {
			if(((Calendar)mMilestones.get(i)).getTimeInMillis() == from.getTimeInMillis()){
				sum += ((MileStone)mMilestones.get(i)).getValue();
			}
		}

		try {
			MileStone milestone = getMilestone(to);
			if(sum - milestone.getValue() > -1 && sum - milestone.getValue() < 1)
				return true;
			else 
				return false;
		} catch(IllegalArgumentException iae) {
			if(sum - value > -1 && sum - value < 1)
				return true;
			else 
				return false;
		}
	}

	private MileStone getMilestone(Calendar to) throws IllegalArgumentException {
		for (int i = 0; i < mMilestones.size(); i++) {
			if(CalendarUtil.isSameDay(to, mMilestones.get(i))){
				return (MileStone)mMilestones.get(i);
			}
		}
		throw new IllegalArgumentException();
	}
	
	private double funktionOf(double x, Vector<Happening> happeningsInRange, double value, Calendar date) {
		double variabel=0;
		for(int q=0;q<happeningsInRange.size();q++ )
		{	
			Calendar dateOne=       ((Happening)  happeningsInRange.elementAt(q)     ).getDate()     ; 
			Calendar dateTwo	= date;
			//variabel=variabel+värdeändring*x^tiden
			variabel=variabel+  ((happeningsInRange.elementAt(q)).getAmount())*(Math.pow( x ,(double)getNumberOfDays(dateOne,dateTwo))) ;
		}	
		return variabel-value;
	}
	
	public Vector<Happening> createHappeningsInRange(Calendar from, Calendar to) {
		Vector<Happening> happeningsInRange = new Vector<Happening>();
		//Add start value
		try {
			Calendar date = getMilestone(from);
			double amount = getMilestone(from).getValue();
			happeningsInRange.addElement(new Happening(date, amount, "Milstolpe"));
		} catch(IllegalArgumentException iae) {
			// No start value to add
		}
		// Add other happenings
		Happening happening;
		for (int i = 0; i < happenings.size(); i++) {
			happening = happenings.elementAt(i);
			if(CalendarUtil.isOnOrBetween(happening.getDate(), from, to)){
				GregorianCalendar interestCountDate = new GregorianCalendar();
				interestCountDate.setTimeInMillis(happening.getDate().getTimeInMillis());
				
				happeningsInRange.addElement(new Happening(interestCountDate, happening.getAmount(), happening.getKommentar()));
			}
		}

		return happeningsInRange;
	}

	public void setValue(Calendar date, double value){
		this.value = value;
		lastUppdateDate = date;
	}
	
	public long getNumberOfDays(Calendar dateOne,Calendar dateTwo) {

		long timeOne = dateOne.getTimeInMillis();
		long timeTwo = dateTwo.getTimeInMillis();
		
		long diffMillis = timeTwo - timeOne;
		long diffSeconds = diffMillis/1000;
		long diffHours = diffSeconds/3600;
		long diffDays = diffHours/24;
		
		return diffDays;
	}//metod
	
	public void setMilestone(Calendar date, double value) {
		//first see if date exist, in that case just replace
		for (int i = 0; i < mMilestones.size(); i++) {
			if(mMilestones.get(i).getTimeInMillis() == date.getTimeInMillis()){
				((MileStone)mMilestones.get(i)).setValue(value);
				return;
			}
		}
		// Ok then we have to add the milestone
		MileStone mileStone = new MileStone(date);
		mileStone.setValue(value);
		mMilestones.add(mileStone);
	}
	
	@Override
	public double getValue(Calendar date) {
		try{
			return  getMilestone(date).getValue();
		} catch(IllegalArgumentException iae) {
			if(date.getTimeInMillis() == lastUppdateDate.getTimeInMillis()){
				return value;
			} else {
			return 0;
			}
		}
	}

	@Override
	public double getLatestValue() {
			return value;
	}
	
	@Override
	public double getMilestoneValue(Calendar date) {
		try{
			return  getMilestone(date).getValue();
		} catch(IllegalArgumentException iae) {
			return 0;
		}
	}
	
	public boolean isMilestoneDate(Calendar date) {
		for (MileStone milestone : mMilestones) {
			if(milestone.getTimeInMillis() == date.getTimeInMillis()) {
				return true;
			}
		} 
		return false;
	}

	public Vector<MileStone> getMilestones() {
		return mMilestones;
	}
	
	public boolean isActive(Calendar from, Calendar to) {
		// Check number of happenings
		if (getHappenings(from, to).size() > 0) {
			return true;
		}
		// Check value
		if (getValue(from) > 1) {
			return true;
		} else if (getValue(to) > 0) {
			return true;
		}
		// Then its not active
		return false;
	}
	

}