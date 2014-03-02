package com.roberthelmbro.economy.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.GetMilestoneDateListener;
import com.roberthelmbro.util.CalendarUtil;

public class GetMilestoneDateUI extends JFrame implements ActionListener{
	static final long serialVersionUID = 0;
	
	private GetMilestoneDateListener listener;
	
	//Lablar
	private JLabel namnL = new JLabel("Välj datum for milstolpe");	
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	//knappar
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Ok");
		
	//textfält
	private JTextField datumT=new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	public GetMilestoneDateUI(GetMilestoneDateListener listener, Calendar date) {

		this.listener = listener;
		setTitle("Lägg till milstolpe");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		//****************Lablar********
		
		
		c.add(namnL);
		namnL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd * 2,ltHojd);

		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		//****************knappar****************
		c.add(sparaB);
		sparaB.setBounds(fonsterBredd/2-ltBredd/2,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaB.addActionListener(this);
		
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		
		//****************textf�lt****************

		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		datumT.setText(CalendarUtil.getShortString(date));
			
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB){
			this.setVisible(false);
		}
		if (e.getSource()==sparaB){
			listener.setMilestone(CalendarUtil.parseString(datumT.getText()));
			this.setVisible(false);
		}
	
	}//actionperformed
}
