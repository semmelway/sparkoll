package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.AddMilestoneUiListener;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseUtil;

public class AddMilestoneUI extends JFrame implements ActionListener{
	static final long serialVersionUID = 0;
	
	private AddMilestoneUiListener listener;
	
	// Labels
	private JLabel namnL = new JLabel("Ange nytt värde för ");	
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel beloppL = new JLabel("Värde");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	// Buttons
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Ok");
		
	// Text Fields
	private JTextField datumT=new JTextField();
	private JTextField beloppT=new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	public AddMilestoneUI(AddMilestoneUiListener listener, String name, Calendar date, double value) {

		this.listener = listener;
		setTitle("Lägg till milstolpe");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		// **************** Labels ********
		
		
		c.add(namnL);
		namnL.setText("Ange ett historiskt värde för " + name);
		namnL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd * 2,ltHojd);

		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(beloppL);
		beloppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		// **************** Buttons ****************
		c.add(sparaB);
		sparaB.setBounds(fonsterBredd/2-ltBredd/2,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaB.addActionListener(this);
		
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		
		// ****************textfält****************

		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		datumT.setText(CalendarUtil.getShortString(date));
		
		c.add(beloppT);
		beloppT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
	    beloppT.setText(value + "");
			
			
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB){
			this.setVisible(false);
		}
		if (e.getSource()==sparaB){
			
			listener.setMilestone(CalendarUtil.parseString(datumT.getText()), ParseUtil.parseDouble(beloppT.getText()));
			
			this.setVisible(false);
		}
	
	}//actionperformed
}
