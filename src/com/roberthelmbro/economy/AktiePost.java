package com.roberthelmbro.economy;
/**
 * @author Robert Helmbo
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.JSONException;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;
import com.roberthelmbro.util.CalendarUtil;


public class AktiePost extends VärdePost {
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
		Happening temp = new Happening(date,this.kurs*this.antal,"K�p");
		happenings.addElement(temp);
	}
	//funktioner

	public void uppdateraVarde(KalkylUI kalkylUI) {
		try {
			System.out.println("Doing update for " + name);
			String rawData = readSource();
			System.out.println(rawData);
			StringTokenizer parseResult = new StringTokenizer(rawData, ",");

			parseResult.nextToken();//kastar f�rsta
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
		
		
		if(name.equals("Fortum")) {
			//try {
			price = price * 8.89;//CurrencyUtil.transform(CurrencyUtil.EUR, CurrencyUtil.SEK, price);
		//	} catch(IOException ioe) {ioe.printStackTrace();return;}
		//	catch(JSONException j) {j.printStackTrace();return;}
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
