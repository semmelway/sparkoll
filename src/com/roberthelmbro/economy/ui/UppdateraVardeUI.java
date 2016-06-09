package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro 
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.AktiePost;
import com.roberthelmbro.economy.KalkylUI;
import com.roberthelmbro.economy.ValuePost;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseUtil;



public class UppdateraVardeUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	
	ValuePost vardePost;
	KalkylUI kalkylUI;
	
	double earned = 0;
	double amount = 0;
	
	
	// Labels
	private JLabel namnL = new JLabel("Ange nytt värde för ");	
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel interestL = new JLabel("Ränta");
	private JLabel beloppL = new JLabel("Värde");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	private JLabel earnedL = new JLabel("***********");
	
	// Buttons
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Spara");
	private JButton updateAndSaveB = new JButton("Plussa och spara");
		
	// Text Fields
	private JTextField datumT=new JTextField();
	private JTextField beloppT=new JTextField();
	private JTextField interestT = new JTextField();
	
	int fonsterBredd = 800;
	
	int ltBredd = 170;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	public UppdateraVardeUI(ValuePost vardePost, KalkylUI kalkylUI) throws IOException ,ClassNotFoundException
	{
		this.vardePost = vardePost;
		this.kalkylUI = kalkylUI;
		setTitle("Uppdatera värde");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(800,300);
	
		//**************** Labels ********
		
		
		c.add(namnL);
		namnL.setText("Ange nytt värde för " + vardePost.getName());
		namnL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd * 2,ltHojd);

		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(beloppL);
		beloppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		if(vardePost instanceof AktiePost){beloppL.setText("Kurs");} 
		
		c.add(interestL);
		interestL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(earnedL);
		earnedL.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+5*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		// **************** Buttons ****************
		c.add(sparaB);
		sparaB.setBounds(fonsterBredd/2-ltBredd/2,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaB.addActionListener(this);
		
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(updateAndSaveB);
		updateAndSaveB.setBounds(fonsterBredd/2 + ltBredd/2 + ltDist, ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		updateAndSaveB.addActionListener(this);
		
		
		//**************** Text Fields ****************

		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		datumT.setText((new Date(System.currentTimeMillis())).toString());
		
		
		
		c.add(beloppT);
		beloppT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		beloppT.setText("" + vardePost.getLatestValue());

		
		c.add(interestT);
		interestT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist), ltBredd,ltHojd);
		interestT.addActionListener(this);
			
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB){
			this.setVisible(false);
		}
		if (e.getSource()==sparaB){
			
			
			if(kollaDatum(datumT.getText()))
				return;
			double newValue = 0;
			try {
			newValue = ParseUtil.parseDouble(beloppT.getText());
			} catch(NumberFormatException n) {
				meddelandeL.setText("Ange korrekt värde.");
			}
			kalkylUI.reportInterest(vardePost.getName(), newValue - vardePost.getLatestValue());
			vardePost.setValue(CalendarUtil.parseString(datumT.getText()), 
					newValue);

			kalkylUI.updateTotal();
			kalkylUI.uppdateraUtskriftsPanelen();
			this.setVisible(false);
		} else if (e.getSource() == interestT) {
			String interestString = interestT.getText();
			if (!kollaVarde(interestString)) {
				double interest = Double.parseDouble(interestString);
				double amount = vardePost.getLatestValue();
				long numberOfDays = getNumberOfDays(vardePost.getLastUppdateDate(),
						CalendarUtil.getTodayCalendarWithClearedClock());
				double earned = calculateEarned(amount, numberOfDays, interest * 0.7D);
				this.earned = earned;
				this.amount = amount;
				earnedL.setText("+ " + round(earned) + " kr efter skatt.");
				
			}
		} else if (e.getSource() == updateAndSaveB) {
			String interestString = interestT.getText();
			if (!kollaVarde(interestString)) {
				double interest = Double.parseDouble(interestString);
				double amount = vardePost.getLatestValue();
				long numberOfDays = getNumberOfDays(vardePost.getLastUppdateDate(),
						CalendarUtil.getTodayCalendarWithClearedClock());
				double earned = calculateEarned(amount, numberOfDays, interest * 0.7D);
				this.earned = earned;
				this.amount = amount;
				earnedL.setText("+ " + round(earned) + " kr efter skatt.");
				
			}
			if(this.earned == 0D) {
				meddelandeL.setText("Finns inget att plussa.");
				return;
			}
			
			vardePost.setValue(CalendarUtil.parseString(datumT.getText()), 
					amount + earned);
			kalkylUI.reportInterest(vardePost.getName(), earned);
			kalkylUI.updateTotal();
			kalkylUI.uppdateraUtskriftsPanelen();
			this.setVisible(false);
		}
	
	}//actionperformed
	
	
	
	private double calculateEarned(double amount, long numberOfDays, double interest) {
		System.out.println("calculateEarden##");
		System.out.println("amount = " + amount);
		System.out.println("numberOfDays = " + numberOfDays);
		System.out.println("interest = " + interest);
		
		
		if(numberOfDays < 1) {
			System.out.println("returns zero");
			return 0;
		}
		interest = interest / 100 + 1;
		System.out.println("fixed interest = " + interest);
		double dayInterest = Math.pow(interest, (1D/365D));
		System.out.println("dayInterest" + dayInterest);
		
		System.out.println("new amount = " + amount * Math.pow(dayInterest,numberOfDays));
		double earned = amount * Math.pow(dayInterest,numberOfDays) - amount;
		System.out.println("earned = " + earned);
		return earned;
	}
	
	public double round(double value) {
		double tio = 10;
		return Math.round(value * 10) / tio;
	}

	public boolean kollaVarde(String varde) {
		try{Double.parseDouble(varde);}
		catch(NumberFormatException e)	
		{
			meddelandeL.setText("\n"+"Du måste ange korekt värde. Värdet måste skrivas med siffror." );
			return true;
		}
		if(varde.length()==0)
		{	meddelandeL.setText("\n"+"Du måste ange ett värde." );
			return true;
		}
		return false;
	}
	public boolean kollaDatum(String datum)
	{
	   // int year=0;
		int month=0;
		int day=0;
		if(datum.length()!=10)
		{
			meddelandeL.setText("\n"+"Du måste ange korekt datum." );
			return true;
		}
		try{
			StringTokenizer date= new StringTokenizer(datum,"-");
			
			Integer.parseInt(date.nextToken());
			month=(int)Integer.parseInt(date.nextToken());
			day=  (int)Integer.parseInt(date.nextToken());
		}	
		catch(NoSuchElementException e)
		{
			meddelandeL.setText("\n"+"Du måste ange korekt datum." );
			return true;
		}
		
		if( !( 1<= month  && month<= 12 && 1 <=day && day<=31) )
		{	
			meddelandeL.setText("\n"+"Du måste ange korekt datum." );
			return true;
		}
		
		try{
			CalendarUtil.parseString(datum);
		}catch(IllegalArgumentException ia){
			return true;
		}
		
		return false;
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
}//class
