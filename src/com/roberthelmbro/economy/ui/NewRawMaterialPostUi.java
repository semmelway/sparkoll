package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro 
 * @year 2012
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

public class NewRawMaterialPostUi extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	KalkylUI kalkyl; 
	
	
	//Lablar
	private JLabel namnL = new JLabel("Namn");
	private JLabel priceL= new JLabel("Pris (kr/kg)");
	private JLabel weightL= new JLabel("Vikt (kg)");
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel gruppL = new JLabel("Grupp");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	//knappar
	private JButton sparaStängB = new JButton("Spara och stäng");
	private JButton avbrytB = new JButton("Avbryt");
		
	//textf�lt
	private JTextField namnT = new JTextField();
	private JTextField beloppT =new JTextField();
	private JTextField weightT =new JTextField();
	private JTextField datumT =new JTextField();
	private JTextField gruppT = new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	public NewRawMaterialPostUi(KalkylUI k, String klickadGrupp) throws IOException ,ClassNotFoundException
	{
		kalkyl=k;	
		
		setTitle("Registrera ny kontopost");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		//****************Lablar********
		
		
		c.add(namnL);
		namnL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(priceL);
		priceL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(weightL);
		weightL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);

		c.add(gruppL);
		gruppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+5*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		
		//****************knappar****************
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(sparaStängB);
		sparaStängB.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+6*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaStängB.addActionListener(this);
		
		//****************textf�lt****************
		c.add(namnT);
		namnT.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(beloppT);
		beloppT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(weightT);
		weightT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(gruppT);
		gruppT.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		gruppT.setText(klickadGrupp);
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB){
			this.setVisible(false);
		}
		
		if(e.getSource()==sparaStängB){
			
			if(((String)namnT.getText()).length()==0)
			{	meddelandeL.setText("Du måste ange ett namn");
				return ;
			}
			
			if(kalkyl.finsPost(namnT.getText())){
				meddelandeL.setText("Namnet m�ste vara unikt");
				return;
			}

			if(ParseCheckerTools.checkDate(datumT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
				return;
			}
			
			if(ParseCheckerTools.checkDouble(beloppT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkValue(beloppT.getText()));
				return;
			}
			
			if(ParseCheckerTools.checkDouble(weightT.getText()) != null){
				meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
				return;
			}

			kalkyl.createRawMaterialPost(namnT.getText(), ParseUtil.parseDouble(beloppT.getText()), ParseUtil.parseDouble(weightT.getText()), CalendarUtil.parseString(datumT.getText()), gruppT.getText());
			this.setVisible(false);
		}		
	}//actionperformed
	
}//class
