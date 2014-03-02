package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro 
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import com.roberthelmbro.economy.ui.UppdateraVardeUI;

public class KontoPost extends VärdePost implements AddMilestoneUiListener
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
	
	//funktioner
	public void uppdateraVarde(KalkylUI kalkylUI) {
		try {
		new UppdateraVardeUI(this, kalkylUI);
		}catch(IOException e){}
		catch(ClassNotFoundException ee){}
		}
}//class
