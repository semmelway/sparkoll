package com.roberthelmbro.economy;

import java.util.Calendar;

public interface DataUpdateListener {

	public void removeTransaction(String post, Calendar date, double amount);
	
	public void removeMilestone(String post, Calendar date);
}
