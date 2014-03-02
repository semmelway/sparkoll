package com.roberthelmbro.economy.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import com.roberthelmbro.economy.YearChooserUiListener;
import com.roberthelmbro.util.CalendarUtil;

public class YearChooserUI extends JFrame implements ActionListener{
	static final long serialVersionUID = 0;
	
	YearChooserUiListener listener; 
		
	//Lablar
	private JLabel fromL = new JLabel("Från");
	private JLabel toL = new JLabel("Till");

	//knappar
	private JButton okB = new JButton("Ok");
	private JButton cancelB = new JButton("Avbryt");
		
	//textf�lt
	private JSpinner fromS;
	private JSpinner toS;
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	

	int ltY = 50;
	
	public YearChooserUI(YearChooserUiListener listener, Calendar[] milestones, Calendar from, Calendar to) {
		
		System.out.println("Length: " + milestones.length);
		
		this.listener = listener;
		setTitle("Välj tidsperiod");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
			
		//****************Lablar********
		c.add(fromL);
		fromL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY,ltBredd,ltHojd);

		c.add(toL);
		toL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY + ltHojd + ltDist,ltBredd,ltHojd);

		//****************knappar****************
		
		c.add(cancelB);
		cancelB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		cancelB.addActionListener(this);
		
		c.add(okB);
		okB.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		okB.addActionListener(this);
		
		//****************spinners****************
		String[] fromChoises = CalendarUtil.getShortStrings(milestones, 0, milestones.length -1);
		fromS = new JSpinner(new SpinnerListModel(fromChoises));
		//if(fromChoises.length != 1)
			//fromS.setValue(CalendarUtil.getShortString(from));
		c.add(fromS);
		fromS.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		String[] toChoises = CalendarUtil.getShortStrings(milestones, 0, milestones.length -1);
		toS = new JSpinner(new SpinnerListModel(toChoises));
		//if(toChoises.length != 1)
			//toS.setValue(CalendarUtil.getShortString(to));
		c.add(toS);
		toS.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==cancelB)
		{
			this.setVisible(false);
		}
		if(e.getSource()==okB){
			listener.setFilterDates(CalendarUtil.parseString((String)fromS.getValue()), CalendarUtil.parseString((String)toS.getValue()));
			this.setVisible(false);
		}
	}//metod
}
