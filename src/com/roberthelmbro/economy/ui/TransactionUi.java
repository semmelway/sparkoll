package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.KalkylUI;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseCheckerTools;
import com.roberthelmbro.util.ParseUtil;

public class TransactionUi extends JFrame implements ActionListener{

		static final long serialVersionUID = 0;
		
		KalkylUI kalkyl;
				
		// Labels
		private JLabel fromL = new JLabel("Från");
		private JLabel toL= new JLabel("Till");		
		private JLabel beloppL= new JLabel("Belopp");
		private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
		private JLabel kommentarL = new JLabel("Kommentar");
		private JLabel meddelandeL = new JLabel("Meddelande");
		
		// Buttons
		private JButton sparastangB = new JButton("Spara och stäng");
		private JButton avbrytB = new JButton("Avbryt");
		
		// TextFields
        private JTextField fromT = new JTextField(); 
        private JTextField toT = new JTextField();
		private JTextField beloppT=new JTextField();
		private JTextField datumT=new JTextField();
		private JTextField kommentarT = new JTextField();
		
		int fonsterBredd = 600;
		
		int ltBredd = 140;
		int ltHojd = 20;
		int ltDist =10;
		
		int ltY = 30;
		
		public TransactionUi(KalkylUI k,String klickadPost, boolean from) throws IOException ,ClassNotFoundException
		{
			kalkyl=k;	
			
			setTitle("Registrera transaktion");
			Container c= getContentPane();
			c.setLayout(null);
			setSize(600,300);
		
			// ****************Labels********
			c.add(fromL);
			fromL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(toL);
			toL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
						
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
			
			// **************** Text Fields ****************
			c.add(fromT);
			fromT.setBounds(fonsterBredd/2+ltDist, ltY+0*(ltHojd+ltDist), ltBredd,ltHojd);
			
			c.add(toT);
			toT.setBounds(fonsterBredd/2+ltDist, ltY+1*(ltHojd+ltDist), ltBredd, ltHojd);
			if (from) {
				fromT.setText(klickadPost);
				toT.setText("Skandiabanken");
			} else {
				toT.setText(klickadPost);
				fromT.setText("Skandiabanken");
			}
			
			c.add(beloppT);
			beloppT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(datumT);
			datumT.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
			
			c.add(kommentarT);
			kommentarT.setBounds(fonsterBredd/2+ltDist,ltY+5*(ltHojd+ltDist),ltBredd,ltHojd);
			
			setVisible(true);
		}//konstruktor
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==avbrytB) {
				this.setVisible(false);
				return;
			} else if (e.getSource()==sparastangB) {
				transaction(true);
				return;
			}					
		}//metod(actionPerformed)

		public void transaction (boolean stang){
			if(ParseCheckerTools.checkDate(datumT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
				return;
			}
			if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
				return;
			}
			if(kalkyl.kollaGruppNamn(fromT.getText())){
				meddelandeL.setText("Du måste ange korrekt gruppnamn.");
				return;
			}

			if(kalkyl.kollaGruppNamn(toT.getText())){
				meddelandeL.setText("Du måste ange korrekt gruppnamn.");
				return;
			}

			kalkyl.transaction(fromT.getText(), toT.getText(), ParseUtil.parseDouble(beloppT.getText()),CalendarUtil.parseString(datumT.getText()),kommentarT.getText());	
			meddelandeL.setText("Insättning sparad.");
			if(stang)
				this.setVisible(!stang);
		}	
		
		public static void main(String[] args) throws ClassNotFoundException, IOException {
			new TransactionUi(null, "Dummy", true);
		}
		
}//class
