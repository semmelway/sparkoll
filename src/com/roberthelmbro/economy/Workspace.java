package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Workspace implements Serializable{
	static final long serialVersionUID = 0;
	
	public Vector<Post> poster;
	public Calendar lastUpdateDate;
	//public String path; 
	public Calendar showFrom;
	public Calendar showTo;
	
	public Workspace() {
		poster = new Vector<Post>();
		//path = new String();
		
		showFrom = new GregorianCalendar();
		showFrom.clear();
		showFrom.set(Calendar.YEAR, 2010);//deafult
		
		showTo = new GregorianCalendar();
		showTo.clear();
		showTo.set(Calendar.YEAR,2011);//deafult
	}
}
