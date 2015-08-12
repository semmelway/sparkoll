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

public class NyKontoPostUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	KalkylUI kalkyl; 
	
	
	// Labels
	private JLabel namnL = new JLabel("Namn");
	private JLabel beloppL= new JLabel("Startvärde");		
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel gruppL = new JLabel("Grupp");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	// Buttons
	private JButton sparaStangB = new JButton("Spara och stäng");
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Spara");
		
	// Text Fields
	private JTextField namnT = new JTextField();
	private JTextField beloppT=new JTextField();
	private JTextField datumT=new JTextField();
	private JTextField gruppT = new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	public NyKontoPostUI(KalkylUI k, String klickadGrupp) throws IOException ,ClassNotFoundException
	{
		kalkyl=k;	
		
		setTitle("Registrera ny kontopost");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		//**************** Labels ********
		
		
		c.add(namnL);
		namnL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(beloppL);
		beloppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(gruppL);
		gruppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+4*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		// **************** Buttons ****************
		c.add(sparaB);
		sparaB.setBounds(fonsterBredd/2-ltBredd/2,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaB.addActionListener(this);
		
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(sparaStangB);
		sparaStangB.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaStangB.addActionListener(this);
		
		//**************** Text Fields ****************
		c.add(namnT);
		namnT.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(beloppT);
		beloppT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(gruppT);
		gruppT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		gruppT.setText(klickadGrupp);
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB){
			this.setVisible(false);
		}
		if (e.getSource()==sparaB){

			meddelandeL.setText("inaktiv knapp");
		}
		
		if(e.getSource()==sparaStangB){
			
			if(((String)namnT.getText()).length()==0)
			{	meddelandeL.setText("Du måste ange ett namn");
				return ;
			}
			
			if(kalkyl.finsPost(namnT.getText())){
				meddelandeL.setText("Namnet måste vara unikt");
				return;
			}

			if(ParseCheckerTools.checkDate(datumT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
				return;
			}
			
			if(ParseCheckerTools.checkValue(beloppT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkValue(beloppT.getText()));
				return;
			}

			kalkyl.skapaKontoPost(namnT.getText(), Double.parseDouble(beloppT.getText()), CalendarUtil.parseString(datumT.getText()), gruppT.getText());
			this.setVisible(false);
		}		
	}//actionperformed
	
}//class