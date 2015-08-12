package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 * @year 2012
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.GoldPriceFetcher;
import com.roberthelmbro.util.ParseUtil;

public class RawMaterialPost extends VärdePost
{
	static final long serialVersionUID = 4295043;
	
	// Price per kilo
	double price;
	
	// Number of kilos
	double weight;
	
	//konstruktor
	public RawMaterialPost(String namn, String groupName, double weight, double price, Calendar date)
	{
		super(namn, groupName);
		lastUppdateDate=date;
		this.price = price;
		this.weight = weight;
		value = this.price*this.weight;
		
		happenings = new Vector<Happening>();
		Happening temp = new Happening(date,this.price*this.weight,"Köp");
		happenings.addElement(temp);
	}
	//funktioner

	public void uppdateraVarde(KalkylUI kalkylUI) {
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
		
		// Transform to SEK if fortum
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
