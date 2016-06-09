package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.roberthelmbro.economy.KalkylUI;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseCheckerTools;
import com.roberthelmbro.util.ParseUtil;

public class InsattningUtagUI extends JFrame implements ActionListener{



		static final long serialVersionUID = 0;
		
		KalkylUI kalkyl; 
				
		// Labels
		private JLabel insattningL = new JLabel("Insättning");
		private JLabel utagL= new JLabel("Utag");		
		private JLabel benamningL = new JLabel("Benämning");
		private JLabel beloppL= new JLabel("Belopp");
		private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
		private JLabel kommentarL = new JLabel("Kommentar");
		private JLabel meddelandeL = new JLabel("Meddelande");
		
		// RadioButtons
		private ButtonGroup insUtagSel = new ButtonGroup();
		private JRadioButton insattningR = new JRadioButton(); 
		private JRadioButton utagR = new JRadioButton();
		
		// Buttons
		private JButton sparastangB = new JButton("Spara och stäng");
		private JButton avbrytB = new JButton("Avbryt");
			
		// TextFields
		private JTextField benomningT=new JTextField();
		private JTextField beloppT=new JTextField();
		private JTextField datumT=new JTextField();
		private JTextField kommentarT = new JTextField();
		
		int fonsterBredd = 600;
		
		int ltBredd = 140;
		int ltHojd = 20;
		int ltDist =10;
		
		int ltY = 30;
		
		public InsattningUtagUI(KalkylUI k,String klickadPost) throws IOException ,ClassNotFoundException
		{
			kalkyl=k;	
			
			setTitle("Registrera insättning/utag");
			Container c= getContentPane();
			c.setLayout(null);
			setSize(600,300);
		
			// ****************Labels********
			
			
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
			
			c.add(kommentarL);
			kommentarL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+5*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(meddelandeL);
			meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+6*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
			
			// ****************Buttons****************
			c.add(avbrytB);
			avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
			avbrytB.addActionListener(this);
			
			c.add(sparastangB);
			sparastangB.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
			sparastangB.addActionListener(this);
			
			// ****************Radio Buttons************
			insUtagSel.add(insattningR);
			c.add(insattningR);
			insattningR.addActionListener(this);
			insattningR.setBounds(fonsterBredd/2, ltY+0*(ltHojd+ltDist), 20,20);
			
			insUtagSel.add(utagR);
			c.add(utagR);
			utagR.addActionListener(this);
			utagR.setBounds(fonsterBredd/2, ltY+1*(ltHojd+ltDist), 20, 20);
			
			// ****************textfields****************
			c.add(benomningT);
			benomningT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
			benomningT.setText(klickadPost);
			
			c.add(beloppT);
			beloppT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(datumT);
			datumT.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(kommentarT);
			kommentarT.setBounds(fonsterBredd/2+ltDist,ltY+5*(ltHojd+ltDist),ltBredd,ltHojd);
			
			setVisible(true);
		}//konstruktor
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==avbrytB)
			{
				
				this.setVisible(false);
			
			}
		
			
			
			if(e.getSource()==sparastangB){
				if(insattningR.isSelected()){
					this.ins(true);
					return;
				}
				else if(utagR.isSelected()){
					this.utag(true);
					return;
				}
				else{
					meddelandeL.setText("Du måste ange insättning eller utag.");
			}
			
			}					
		}//metod(actionPerformed)
		public void ins(boolean stang){
			if(ParseCheckerTools.checkDate(datumT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
				return;
			}
			if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
				return;
			}
			if(!kalkyl.isPostName(benomningT.getText())){
				meddelandeL.setText("Du måste ange korrekt gruppnamn.");
				return;
			}
								
			kalkyl.ins(benomningT.getText(),ParseUtil.parseDouble(beloppT.getText()),CalendarUtil.parseString(datumT.getText()),kommentarT.getText());	
			meddelandeL.setText("Insättning sparad.");
			if(stang)
				this.setVisible(!stang);
		}	
		
		public void utag(boolean stang) {
			if(ParseCheckerTools.checkDate(datumT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
				return;
			}
			if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
				return;
			}
			if(!kalkyl.isPostName(benomningT.getText())){
				meddelandeL.setText("Du måste ange korrekt gruppnamn.");
				return;
			}		
		
			kalkyl.utag(benomningT.getText(),ParseUtil.parseDouble(beloppT.getText()),CalendarUtil.parseString(datumT.getText()),kommentarT.getText());
			meddelandeL.setText("Utag sparat.");
			if(stang)
				this.setVisible(!stang);	
		}	
}//class

