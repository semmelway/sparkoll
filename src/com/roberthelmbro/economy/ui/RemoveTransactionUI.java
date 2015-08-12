/**
 * @author Robert Helmbro
 */
package com.roberthelmbro.economy.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.roberthelmbro.economy.DataUpdateListener;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseCheckerTools;
import com.roberthelmbro.util.ParseUtil;

public class RemoveTransactionUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	
	// Labels
	private JLabel insattningL = new JLabel("Insättning");
	private JLabel utagL= new JLabel("Utag");		
	private JLabel benamningL = new JLabel("Benämning");
	private JLabel beloppL= new JLabel("Belopp");
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	//RadioButtons
	private ButtonGroup insUtagSel = new ButtonGroup();
	private JRadioButton insattningR = new JRadioButton(); 
	private JRadioButton utagR = new JRadioButton();
	
	// Buttons
	private JButton performAndClose = new JButton("Spara och stang");
	private JButton avbrytB = new JButton("Avbryt");
	private JButton perform = new JButton("Spara");
		
	// Text Fields
	private JTextField benamningT=new JTextField();
	private JTextField beloppT=new JTextField();
	private JTextField datumT=new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	
	
	private DataUpdateListener mListener;
	
	
	public RemoveTransactionUI(DataUpdateListener listener, String clickedPost)
	{
		mListener =  listener;
		setTitle("Ta bort insättning/utag");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		//**************** Labels ********
		
		
		c.add(insattningL);
		insattningL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(utagL);
		utagL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(benamningL);
		benamningL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(beloppL);
		beloppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+6*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		// **************** Buttons ****************
		c.add(perform);
		perform.setBounds(fonsterBredd/2-ltBredd/2,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		perform.addActionListener(this);
		
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(performAndClose);
		performAndClose.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		performAndClose.addActionListener(this);
		
		//****************Radio Buttons************
		insUtagSel.add(insattningR);
		c.add(insattningR);
		insattningR.addActionListener(this);
		insattningR.setBounds(fonsterBredd/2, ltY+0*(ltHojd+ltDist), 20,20);
		
		insUtagSel.add(utagR);
		c.add(utagR);
		utagR.addActionListener(this);
		utagR.setBounds(fonsterBredd/2, ltY+1*(ltHojd+ltDist), 20, 20);
		
		//**************** Text Fields ****************
		c.add(benamningT);
		benamningT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		benamningT.setText(clickedPost);
		benamningT.setEditable(false);
		
		c.add(beloppT);
		beloppT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==avbrytB) {
			this.setVisible(false);
		} else if (e.getSource()==performAndClose) {
			if(insattningR.isSelected()) {
				this.removePositiveTransaction(true);
				return;
			}
			else if(utagR.isSelected()) {
				this.removeNegativeTransaction(true);
				return;
			}
			else{
				meddelandeL.setText("Du måste ange insättning eller uttag.");
			}
		}

		if (e.getSource() == perform) {
			if(insattningR.isSelected()){
				this.removePositiveTransaction(false);
				return;
			}
			else if(utagR.isSelected()){
				this.removeNegativeTransaction(false);
				return;
			}
			else{
				meddelandeL.setText("Du måste ange insättning eller uttag.");
			}
		}
		
	}
	
	public void removePositiveTransaction(boolean stang) {
		if(ParseCheckerTools.checkDate(datumT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
			return;
		}
		if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
			return;
		}
			
		String label = benamningT.getText();
		Calendar date = CalendarUtil.parseString(datumT.getText());
		double amount = ParseUtil.parseDouble(beloppT.getText());
		
							
		mListener.removeTransaction(label, date, amount);
		if(stang)
			this.setVisible(!stang);
		
			
		
	}	
	
	public void removeNegativeTransaction(boolean stang)
	{
		if(ParseCheckerTools.checkDate(datumT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
			return;
		}
		if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
			return;
		}
	
		String label = benamningT.getText();
		Calendar date = CalendarUtil.parseString(datumT.getText());
		double amount = ParseUtil.parseDouble(beloppT.getText());
							
		mListener.removeTransaction(label, date, (-1)*amount);
		if(stang)
			this.setVisible(!stang);	
	}	
}
