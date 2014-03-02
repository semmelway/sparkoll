package com.roberthelmbro.economy;
import java.io.Serializable;
import java.util.Calendar;



public class Happening implements Serializable
{
	static final long serialVersionUID = 0;
	private double amount;
	private Calendar date;
	private String comment;


	//konstruktor
	public Happening() {
		amount=0;
		comment="kommentar saknas";
		//sign=s;
	}


	public Happening(Calendar date, double amount,String comment) {
		this.date=date;
		this.amount=amount;
		this.comment=comment;
		//sign=s;
	}
	
	public Calendar getDate() {
		return date;
	}
	public double getAmount() {
		return amount;
	}
	public String getKommentar() {
		return comment;
	}
}
