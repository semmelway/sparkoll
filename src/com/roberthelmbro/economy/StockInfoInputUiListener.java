package com.roberthelmbro.economy;

import java.net.URL;
import java.util.Calendar;

public interface StockInfoInputUiListener {
	
	public void stockUpdated();
	
	public void addStock(String name, int count, double price, Calendar date, URL url, String groupName);
	
	public boolean isPostPresent(String name);

}
