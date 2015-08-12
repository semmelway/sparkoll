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

public class NyGruppUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	
	KalkylUI kalkyl; 
		
	// Labels
	private JLabel benamningL = new JLabel("Benämning");
	private JLabel meddelandeL = new JLabel("Meddelande");

	// Buttons
	private JButton sparaStangB = new JButton("Spara och stäng");
	private JButton avbrytB = new JButton("Avbryt");
	private JButton sparaB = new JButton("Spara");
		
	// Text Fields
	private JTextField benamningT = new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	

	int ltY = 50;
	
	public NyGruppUI(KalkylUI k) throws IOException ,ClassNotFoundException
	{
		kalkyl=k;	
		
		setTitle("Ny grupp");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
			
		//**************** Labels ********
		c.add(benamningL);
		benamningL.setBounds  (fonsterBredd/2-ltDist-ltBredd,ltY,ltBredd,ltHojd);
		

		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+5*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);

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
		c.add(benamningT);
		benamningT.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==avbrytB)
		{
			this.setVisible(false);
		}
		if(e.getSource()==sparaStangB){
			
			if(((String)benamningT.getText()).length()==0)
			{	meddelandeL.setText("Du måste ange en benämning");
				return ;
			}
			if(kalkyl.finsPost(benamningT.getText())){
				meddelandeL.setText("Gruppnamnet måste vara unikt");
				return;
			}
			kalkyl.skapaGrupp((String)benamningT.getText());
			this.setVisible(false);
			kalkyl.uppdateraUtskriftsPanelen();
		}
		
		if (e.getSource()==sparaB){
		
		if(((String)benamningT.getText()).length()==0)
		{	meddelandeL.setText("Du måste ange en benämning");
			return ;
		}
		if(kalkyl.finsPost(benamningT.getText())){
			meddelandeL.setText("Gruppnamnet måste vara unikt");
			return;
		}
		kalkyl.skapaGrupp((String)benamningT.getText());
		meddelandeL.setText("Posten "+(String)benamningT.getText()+" har skapats.");
		kalkyl.uppdateraUtskriftsPanelen();
		}
		
	}//metod
}//class
