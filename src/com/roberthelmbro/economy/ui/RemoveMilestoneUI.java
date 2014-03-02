package com.roberthelmbro.economy.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.DataUpdateListener;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseCheckerTools;

public class RemoveMilestoneUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	
	//Lablar
	private JLabel datumL= new JLabel("Datum(����-mm-dd)");
	private JLabel meddelandeL = new JLabel("Meddelande");

	//knappar
	private JButton performAndClose = new JButton("Spara och st�ng");
	private JButton avbrytB = new JButton("Avbryt");
		
	//textf�lt
	private JTextField datumT=new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	
	
	private DataUpdateListener mListener;
	
	private String post;
	
	
	public RemoveMilestoneUI(DataUpdateListener listener, String clickedPost)
	{
		mListener =  listener;
		this.post = clickedPost;
		setTitle("Ta bort milstople fr�n " + clickedPost);
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
	
		//****************Lablar********
		
		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+6*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		//****************knappar****************
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(performAndClose);
		performAndClose.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		performAndClose.addActionListener(this);
		
		//****************textf�lt****************
		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		
		setVisible(true);
	}//konstruktor
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==avbrytB) {
			this.setVisible(false);
		} else if (e.getSource()==performAndClose) {
			removeMilestone();
			this.setVisible(false);
		}
	}
	
	public void removeMilestone() {
		if(ParseCheckerTools.checkDate(datumT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
			return;
		}
		Calendar date = CalendarUtil.parseString(datumT.getText());
		mListener.removeMilestone(post, date);
		}
}

