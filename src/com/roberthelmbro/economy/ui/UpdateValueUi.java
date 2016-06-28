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



public class UpdateValueUi extends JFrame implements ActionListener {
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
	private JLabel newValueL = new JLabel("Värdeförändring");
	private JLabel meddelandeL = new JLabel("");

	private JLabel earnedL = new JLabel("***********");

	// Buttons
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Spara");

	// Text Fields
	private JTextField datumT = new JTextField();
	private JTextField beloppT = new JTextField();
	private JTextField changeT = new JTextField();
	private JTextField interestT = new JTextField();

	int leftColumnWidth = 200;
	int heightDist = 10;
	int widthDist = 20;

	int fieldWidth = 170;
	int fieldHeight = 20;


	public UpdateValueUi(ValuePost vardePost, KalkylUI kalkylUI) throws IOException ,ClassNotFoundException
	{
		this.vardePost = vardePost;
		this.kalkylUI = kalkylUI;
		setTitle("Uppdatera värde");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(800,300);

		//**************** Labels ********

		int x = leftColumnWidth;
		int y = heightDist;
		c.add(namnL);
		namnL.setText("Ange nytt värde för " + vardePost.getName());
		namnL.setBounds(x, y, fieldWidth * 3, fieldHeight);

		y+= fieldHeight + heightDist;
		c.add(datumL);
		datumL.setBounds(x, y, fieldWidth, fieldHeight);

		y+= fieldHeight + heightDist;
		c.add(beloppL);
		beloppL.setBounds(x, y, fieldWidth, fieldHeight);
		if(vardePost instanceof AktiePost){beloppL.setText("Kurs");}

		y+= fieldHeight + heightDist;
        c.add(newValueL);
        newValueL.setBounds(x, y, fieldWidth, fieldHeight);
        if(vardePost instanceof AktiePost){beloppL.setText("Kursförändring");}

		y+= fieldHeight + heightDist;
		c.add(interestL);
		interestL.setBounds(x, y, fieldWidth, fieldHeight);

		y+= fieldHeight + heightDist;
		c.add(earnedL);
		earnedL.setBounds(x, y, fieldWidth, fieldHeight);

		y+= fieldHeight + heightDist;
		c.add(meddelandeL);
		meddelandeL.setBounds(x, y, fieldWidth * 3, fieldHeight);

        // **************** Action bar ****************
        x = leftColumnWidth;

        c.add(avbrytB);
        avbrytB.setBounds(x, y, fieldWidth, fieldHeight);
        avbrytB.addActionListener(this);

        x+= fieldWidth + widthDist;
        c.add(sparaB);
        sparaB.setBounds(x, y, fieldWidth, fieldHeight);
        sparaB.addActionListener(this);

        //**************** Text Fields ****************
        y = heightDist * 2 + fieldHeight;
        c.add(datumT);
        datumT.setBounds(x, y, fieldWidth, fieldHeight);
        datumT.setText((new Date(System.currentTimeMillis())).toString());

        y+= fieldHeight + heightDist;
        c.add(beloppT);
        beloppT.setBounds(x, y, fieldWidth, fieldHeight);
        beloppT.setText("" + vardePost.getLatestValue());

        y+= fieldHeight + heightDist;
        c.add(changeT);
        changeT.setBounds(x, y, fieldWidth, fieldHeight);
        changeT.setText("");
        changeT.addActionListener(this);

        y+= fieldHeight + heightDist;
        c.add(interestT);
        interestT.setBounds(x, y, fieldWidth, fieldHeight);
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
		} else if (e.getSource() == changeT) {
            String valueChange = changeT.getText();
            if (valueChange.length() == 0) {
             return;
            }
            boolean add = valueChange.startsWith("+");
            boolean substract = valueChange.startsWith("-");
            if (!(add || substract)) {
                meddelandeL.setText("Värdeförändring måste inledas med + eller -");
                return;
            }

            if(!kollaVarde(valueChange.substring(1))) {
                double value = Double.parseDouble(valueChange.substring(1));
                if (add) {
                    beloppT.setText("" + (Double.parseDouble(beloppT.getText()) + value));
                } else if(substract) {
                    beloppT.setText("" + (Double.parseDouble(beloppT.getText()) - value));
                }
            }
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
				changeT.setText("+" + round(earned));

			}
		}

	}//actionperformed



	private double calculateEarned(double amount, long numberOfDays, double interest) {

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
			month=Integer.parseInt(date.nextToken());
			day=  Integer.parseInt(date.nextToken());
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
	}

    /*public static void main(String[] args) {
	    try {
	        ValuePost valuePost = new ValuePost("Hola", "Dola");
	                valuePost.setValue(CalendarUtil.getThisYearStart(), 333);

            new UpdateValueUi(valuePost, null);
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}*/

} // Class
