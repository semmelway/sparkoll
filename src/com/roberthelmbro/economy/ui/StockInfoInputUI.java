package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro 
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.roberthelmbro.economy.AktiePost;
import com.roberthelmbro.economy.StockInfoInputUiListener;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.ParseCheckerTools;
import com.roberthelmbro.util.ParseUtil;

public class StockInfoInputUI extends JFrame implements ActionListener {
	static final long serialVersionUID = 0;
	
	Type type;
	
	public static enum Type {
		NEW_STOCK,
		UPPDATE_STOCK
	}
	
	StockInfoInputUiListener resultListener; 
	AktiePost stock;
	
	// Labels
	private JLabel nameL = new JLabel("Namn");
	private JLabel antalL= new JLabel("Antal");		
	private JLabel priceL = new JLabel("Kurs");
	private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
	private JLabel urlL= new JLabel("UppdateringsURL");
	private JLabel gruppL = new JLabel("Grupp");
	private JLabel meddelandeL = new JLabel("Meddelande");
	
	// Buttons
	private JButton sparaStangB = new JButton("Spara och stäng");
	private JButton avbrytB = new JButton("Avbryt");
		
	// Text Fields
	private JTextField namnT = new JTextField();
	private JTextField antalT = new JTextField();
	private JTextField priceT=new JTextField();
	private JTextField datumT=new JTextField();
	private JTextField urlT=new JTextField();
	private JTextField gruppT = new JTextField();
	
	int fonsterBredd = 600;
	
	int ltBredd = 140;
	int ltHojd = 20;
	int ltDist =10;
	
	int ltY = 30;
	

	/**
	 * @param k
	 * @param klickadGrupp
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public StockInfoInputUI(StockInfoInputUiListener listener, String groupName) throws IOException ,ClassNotFoundException
	{
		resultListener=listener;	
		this.type = Type.NEW_STOCK;
		setUpUI("",0,0,"",groupName);

	}
	
	public StockInfoInputUI(StockInfoInputUiListener listener, AktiePost stock) {
		resultListener = listener;
		type = Type.UPPDATE_STOCK;
		this.stock = stock;
		setUpUI(stock.getName(), stock.getCount(), stock.getPrice(), "" + stock.getUpdateUrl(),stock.getGroupName());
		setNonEditableFields();
	}
	
	private void setUpUI(String name, int count, double price, String url, String groupName) {
		//**************** Labels ********
		setTitle("Registrera ny aktiepost");
		Container c= getContentPane();
		c.setLayout(null);
		setSize(600,300);
		
		c.add(nameL);
		nameL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);

		
		c.add(antalL);
		antalL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);

		
		c.add(priceL);
		priceL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(datumL);
		datumL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(urlL);
		urlL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
	
		c.add(gruppL);
		gruppL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+5*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(meddelandeL);
		meddelandeL.setBounds(fonsterBredd/2-ltDist-ltBredd,ltY+6*(ltHojd+ltDist),ltBredd*2+ltDist,ltHojd);
		
		// **************** Buttons ****************
		c.add(avbrytB);
		avbrytB.setBounds(fonsterBredd/2-3*ltBredd/2-ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		avbrytB.addActionListener(this);
		
		c.add(sparaStangB);
		sparaStangB.setBounds(fonsterBredd/2+ltBredd/2+ltDist,ltY+7*(ltHojd+ltDist),ltBredd,ltHojd);
		sparaStangB.addActionListener(this);
		
		//****************  Text Fields ****************
		c.add(namnT);
		namnT.setBounds(fonsterBredd/2+ltDist,ltY+0*(ltHojd+ltDist),ltBredd,ltHojd);
		namnT.setText(name);
		
		c.add(antalT);
		antalT.setBounds(fonsterBredd/2+ltDist,ltY+1*(ltHojd+ltDist),ltBredd,ltHojd);
		antalT.setText("" + count);
		
		c.add(priceT);
		priceT.setBounds(fonsterBredd/2+ltDist,ltY+2*(ltHojd+ltDist),ltBredd,ltHojd);
		priceT.setText("" + price);

		c.add(datumT);
		datumT.setBounds(fonsterBredd/2+ltDist,ltY+3*(ltHojd+ltDist),ltBredd,ltHojd);
		
		c.add(urlT);
		urlT.setBounds(fonsterBredd/2+ltDist,ltY+4*(ltHojd+ltDist),ltBredd,ltHojd);
		urlT.setText("" + url);
		
		c.add(gruppT);
		gruppT.setBounds(fonsterBredd/2+ltDist,ltY+5*(ltHojd+ltDist),ltBredd,ltHojd);
		gruppT.setText(groupName);
		
		setVisible(true);
	}
	
	public void setNonEditableFields() {
		
		namnT.setEnabled(false);
		priceT.setEnabled(false);
		datumT.setText("not available");
		datumT.setEnabled(false);
		gruppT.setEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==avbrytB) {
			this.setVisible(false);
		}
		
		if(e.getSource()==sparaStangB){
			
			if(inputFieldsFailed()){
				return;
			}
			


			URL url = createUrl(urlT.getText());
			
			switch(type) {
			case NEW_STOCK:
				resultListener.addStock(namnT.getText(), ParseUtil.parseInt(antalT.getText()),
						ParseUtil.parseDouble(priceT.getText()),
						CalendarUtil.parseString(datumT.getText()), url, gruppT.getText());
				break;
			case UPPDATE_STOCK:
				stock.setCount(Integer.parseInt(antalT.getText()));
				stock.setUpdateUrl(url);
				break;
			}
			
			this.setVisible(false);
		}		
	}//actionperformed
	
	private boolean inputFieldsFailed(){
		if(((String)namnT.getText()).length()==0) {	meddelandeL.setText("Du måste ange ett namn");
			return true;
		} else if(type == Type.NEW_STOCK && resultListener.isPostPresent(namnT.getText())){
			meddelandeL.setText("Namnet måste vara unikt");
			return true;
		} else if(((String)priceT.getText()).length()==0) {	
			meddelandeL.setText("Du måste ange kurs");
			return true;
		} else if(ParseCheckerTools.checkInt(antalT.getText()) != null) {
			meddelandeL.setText(ParseCheckerTools.checkInt(antalT.getText()));
			return true;
		} else if(ParseCheckerTools.checkDouble(priceT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDouble(priceT.getText()));
			return true;
		} else if(type == Type.NEW_STOCK && ParseCheckerTools.checkDate(datumT.getText()) != null){
			meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
			return true;
		}
		return false;
	}
	
	private URL createUrl(String url){
		URL tempURL=null;
		try{
		if(url.length() != 0)
			tempURL = new URL(url);
		}catch (MalformedURLException ee){tempURL=null;}
		return tempURL;
		
	}
	
}//class



