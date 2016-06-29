package com.roberthelmbro.economy;

/**
 * @author Robert Helmbro
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;

import org.json.JSONException;

import com.roberthelmbro.economy.ui.GetMilestoneDateUI;
import com.roberthelmbro.economy.ui.InsattningUtagUI;
import com.roberthelmbro.economy.ui.NewRawMaterialPostUi;
import com.roberthelmbro.economy.ui.NyGruppUI;
import com.roberthelmbro.economy.ui.NyKontoPostUI;
import com.roberthelmbro.economy.ui.RemoveMilestoneUI;
import com.roberthelmbro.economy.ui.RemoveTransactionUI;
import com.roberthelmbro.economy.ui.StockInfoInputUI;
import com.roberthelmbro.economy.ui.TransactionUi;
import com.roberthelmbro.economy.ui.YearChooserUI;
import com.roberthelmbro.util.CalendarUtil;
import com.roberthelmbro.util.FileUtil;
import com.roberthelmbro.util.FileUtil.JsonAndPath;

public class KalkylUI extends JFrame implements ActionListener, GetMilestoneDateListener, YearChooserUiListener, DataUpdateListener  {
	static final long serialVersionUID = 0;
	private Workspace workspace = new Workspace();
	private Grupp all = new Grupp("all");
	private Vector<MileStone> allMilestoneDates = new Vector<MileStone>();

	// Menubar
	private JMenuBar menuBar = new JMenuBar();

	// Menues
	private JMenu fileMenu = new JMenu("Funktioner");
	private JMenuItem newWorkspace = new JMenuItem("Ny");
	private JMenuItem openMenuItem = new JMenuItem("Öppna");
	private JMenuItem saveAsMenuItem = new JMenuItem("Spara som");
	private JMenuItem updateMenuItem = new JMenuItem("Uppdatera");
	private JMenuItem addMilestoneMenuItem = new JMenuItem("Milstolpe");
	private JMenuItem quitMenuItem = new JMenuItem("Avsluta");

	private JMenu posterM = new JMenu("Poster");
	private JMenuItem newGroupMenuItem = new JMenuItem("Ny grupp");
	private JMenuItem nyAktiePost = new JMenuItem("Ny aktiepost");
	private JMenuItem nyKontoPost = new JMenuItem("Ny kontopost");
	private JMenuItem newRawMaterialPost = new JMenuItem("Ny råvarupost");

	private JMenu loggM = new JMenu("Loggutskrifter");
	private JMenuItem logOverview = new JMenuItem("Skriv ut översyn");
	private JMenuItem skrivUtInsUtag = new JMenuItem("Skriv ut ins./utag");
	private JMenuItem printMilestones = new JMenuItem("Skriv ut milstolpar");

	private JMenu viewM = new JMenu("Visa");
	private JMenuItem allMenuItem = new JMenuItem("Full");
	private JMenuItem thisYearMenuItem = new JMenuItem("I år");
	private JMenuItem period = new JMenuItem("Period");

	// Popup menus
	private JPopupMenu popupMenyn = new JPopupMenu();
	private JMenuItem transactionFrom = new JMenuItem("Överför från");
	private JMenuItem transactionTo = new JMenuItem("Överför till");
	private JMenuItem edit = new JMenuItem("Redigera");
	private JMenuItem radera = new JMenuItem("Radera");
	private JMenuItem raderaT = new JMenuItem("Transformera till ny struktur");
	private JMenuItem registreraInsUt = new JMenuItem("Registrera insättning eller utag");
	private JMenuItem visaInsUt = new JMenuItem("Visa insättnigar & utag");
	private JMenuItem removeTransaction = new JMenuItem("Radera insättning/utag");
	private JMenuItem removeMilestone = new JMenuItem("Radera milstolpe");

	String klickadPost = "";

	// Labels
	private JLabel titleLabel = new JLabel();
	private int antalRader = 40;
	private int antalKolumner = 6;
	private JPanel utskriftsPanel = new JPanel();
	private JLabel[][] utskriftsLabel = new JLabel[antalRader][antalKolumner];
	JScrollPane matrixScrollPane = new JScrollPane(utskriftsPanel);

	private Font gruppFont;
	private Font rubrikFont;
	private Font vardeFont;

	// textarea
	JTextArea textArean = new JTextArea();
	JScrollPane textAreaScrollPane = new JScrollPane(textArean);

	// command
	JTextField commandT = new JTextField();

	//Buttons
	private JButton backButton;

	// Colors
	Color redText = new Color(199,19,32);
	Color greenText = new Color(27,86,9);
	Color blackText = new Color(0,0,0);

	Font font = new Font("arial", 0, 12);

	public KalkylUI() throws IOException, ClassNotFoundException {


		setTitle("Robbans program för översyn av pengabingen");
		Container c = getContentPane();
		c.setLayout(null);
		setSize(1024, 1024);
		this.addWindowListener(fonsterLyssnare);

		// ***************menyer***********
		// Menubar
		setJMenuBar(menuBar);
		// menyer
		menuBar.add(fileMenu);
		fileMenu.add(newWorkspace);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(updateMenuItem);
		fileMenu.add(addMilestoneMenuItem);
		fileMenu.add(quitMenuItem);
		newWorkspace.addActionListener(this);
		openMenuItem.addActionListener(this);
		saveAsMenuItem.addActionListener(this);
		updateMenuItem.addActionListener(this);
		addMilestoneMenuItem.addActionListener(this);
		quitMenuItem.addActionListener(this);

		menuBar.add(posterM);
		posterM.add(newGroupMenuItem);
		posterM.add(nyAktiePost);
		posterM.add(nyKontoPost);
		posterM.add(newRawMaterialPost);
		newGroupMenuItem.addActionListener(this);
		nyAktiePost.addActionListener(this);
		nyKontoPost.addActionListener(this);
		newRawMaterialPost.addActionListener(this);

		menuBar.add(loggM);
		loggM.add(logOverview);
		loggM.add(skrivUtInsUtag);
		loggM.add(printMilestones);
		logOverview.addActionListener(this);
		skrivUtInsUtag.addActionListener(this);
		printMilestones.addActionListener(this);


		menuBar.add(viewM);
		viewM.add(allMenuItem);
		viewM.add(thisYearMenuItem);
		viewM.add(period);
		allMenuItem.addActionListener(this);
		thisYearMenuItem.addActionListener(this);
		period.addActionListener(this);

		// Popup Menu
		popupMenyn.add(transactionFrom);
		popupMenyn.add(transactionTo);
		popupMenyn.add(edit);
		popupMenyn.add(radera);
		popupMenyn.add(raderaT);
		popupMenyn.add(registreraInsUt);
		popupMenyn.add(visaInsUt);
		popupMenyn.add(removeTransaction);
		popupMenyn.add(removeMilestone);

		transactionFrom.addActionListener(this);
		transactionTo.addActionListener(this);
		edit.addActionListener(this);
		radera.addActionListener(this);
		raderaT.addActionListener(this);
		registreraInsUt.addActionListener(this);
		visaInsUt.addActionListener(this);
		removeTransaction.addActionListener(this);
		removeMilestone.addActionListener(this);

		// ****************** Labels
		utskriftsPanel.setLayout(new GridLayout(antalRader, antalKolumner));
		titleLabel.setBounds(12, 5, 800, 20);
		c.add(titleLabel);
		matrixScrollPane.setBounds( 12, 30, 1000, 726);

		for (int i = 0; i < antalRader; i++)
			for (int j = 0; j < antalKolumner; j++) {
				utskriftsLabel[i][j] = new JLabel("");

				if (j == 0) {
					utskriftsLabel[i][j].add(popupMenyn);
					utskriftsLabel[i][j].addMouseListener(mouseListener);
				}
				if( 1 <= j && j <= 5){
					utskriftsLabel[i][j].setHorizontalAlignment(SwingConstants.RIGHT);
				}
				//utskriftsLabel[i][j].setFont(font);
				utskriftsPanel.add(utskriftsLabel[i][j]);
			}

		c.add(matrixScrollPane);
		matrixScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		backButton = new JButton("Tillbaka");
		backButton.setBounds(900, 5, 100, 20);
		backButton.addActionListener(this);
		backButton.setVisible(false);
		c.add(backButton);

		gruppFont = new Font("arial", Font.BOLD, 12);
		vardeFont = new Font("arial", Font.PLAIN, 12);
		rubrikFont = new Font("arial", Font.BOLD, 13);

		// ****************textarea****************
		c.add(textAreaScrollPane);
		textAreaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textAreaScrollPane.setBounds(12, 756, 1000, 225);

		c.add(commandT);
		commandT.setBounds(12, 981, 1000, 20);
		commandT.addActionListener(this);

		setVisible(true);
	}// konstruktor

	private MouseListener mouseListener = new MouseAdapter() {

		public void mousePressed(MouseEvent e) {
			if (e.isMetaDown()) {
				klickadPost = ((JLabel) e.getComponent()).getText();
				if (klickadPost == "Summa" || klickadPost == "Post")
					return;
				if (!isPostName(klickadPost))
					return;

				edit.setText("Redigera " + klickadPost);
				radera.setText("Radera " + klickadPost);
				raderaT.setText("Transformera " + klickadPost);

				Post post = getValuePost(klickadPost);

				transactionFrom.setVisible(true);
				transactionTo.setVisible(true);
				edit.setVisible(true);
				registreraInsUt.setVisible(true);
				visaInsUt.setVisible(true);
				removeTransaction.setVisible(true);
				removeMilestone.setVisible(true);


				if (klickadPost == null) {
					// This is a group
					transactionFrom.setVisible(false);
					transactionTo.setVisible(false);
					edit.setVisible(false);
					registreraInsUt.setVisible(false);
					visaInsUt.setVisible(false);
					removeTransaction.setVisible(false);
					removeMilestone.setVisible(false);
				} else if(post instanceof AktiePost) {

				} else {
					edit.setVisible(false);
				}
				popupMenyn.show(e.getComponent(), e.getX(), e.getY());
			}

		}
	};

	public void actionPerformed(ActionEvent e) {
		// ****************** Funktioner
		// ***********************************************
		if(e.getSource() == newWorkspace) {
			workspace = new Workspace();
			SavedData.clear();
			all = new Grupp("all");
			allMilestoneDates = new Vector<MileStone>();
			uppdateraUtskriftsPanelen();

		} else if (e.getSource() == openMenuItem) {
			//			ObjectAndString objectAndString = FileUtil.readObjectFromUserSpecifiedFile(getContentPane());
			//			workspace = (Workspace)objectAndString.object;
			//			SavedData.setLastSavedWorkspace(objectAndString.string);
			JsonAndPath jsonAndPath = FileUtil.readJsonFromUserSpecifiedFile(getContentPane());


			try {
				workspace = new Workspace(jsonAndPath.json);
			} catch (JSONException e1) {
				e1.printStackTrace();
				workspace = new Workspace();
				log("JSONException while loading workspace: " + jsonAndPath.path);
			}
			SavedData.setLastSavedWorkspace(jsonAndPath.path);
			try {
				SavedData.save();
			} catch(IOException ioe){ioe.printStackTrace();}
			updateAllMilestones();
			updateTotal();
			uppdateraUtskriftsPanelen();
		}
		if (e.getSource() == saveAsMenuItem) {
			try {
				saveWorkspaceAs();
			} catch (JSONException | URISyntaxException | IOException exc) {
				log("Could not save workspace");
				exc.printStackTrace();
			}
		}

		if (e.getSource() == updateMenuItem) {
			for (Post rootPost : workspace.poster) {
				if (rootPost.isActive(workspace.showFrom, workspace.showTo)) {
					rootPost.uppdateraVarde(this, workspace.showFrom, workspace.showTo);
				}
			}
			workspace.lastUpdateDate = CalendarUtil.getTodayCalendarWithClearedClock();
			workspace.showTo = workspace.lastUpdateDate;
			log("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " -> " + CalendarUtil.getShortString(workspace.showTo));
			uppdateraUtskriftsPanelen();
		} else if(e.getSource() == addMilestoneMenuItem) {
			addMilestone();
		}
		if (e.getSource() == quitMenuItem) {
			try {
				saveWorkspace();
			} catch (JSONException | URISyntaxException | IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		// ****************** Poster
		// ***********************************************
		if (e.getSource() == newGroupMenuItem) {
			try {
				new NyGruppUI(this);

			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		}
		if (e.getSource() == nyAktiePost) {

			try {
				new StockInfoInputUI(this, "");

			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		}
		if (e.getSource() == nyKontoPost) {

			try {
				new NyKontoPostUI(this, "");

			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		} else if(e.getSource() == newRawMaterialPost) {
			try {
				new NewRawMaterialPostUi(this, "");

			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		}



		// Logg utskrifter
		if (e.getSource() == logOverview) {
			printTextSumary();
		}
		if (e.getSource() == skrivUtInsUtag) {
			printTransactions();
		} else if(e.getSource() == printMilestones) {
			printAllMilestones();
		} else if (e.getSource() == allMenuItem) {
			setFilterDates(CalendarUtil.parseString("2010-01-01"), workspace.lastUpdateDate);
		} else if (e.getSource() == thisYearMenuItem) {
			setFilterDates(CalendarUtil.getThisYearStart(), workspace.lastUpdateDate);
		} else if (e.getSource() == period) {
			showYearChooserUI();
		}
		// ****************** Popup Menu *****************
		// ***********************************************
		if (e.getSource() == transactionFrom) {
			try {
				new TransactionUi(this, klickadPost, true);
			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		}

		if (e.getSource() == transactionTo) {
			try {
				new TransactionUi(this, klickadPost, false);
			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		}

		if (e.getSource() == edit) {
			new StockInfoInputUI(this, (AktiePost)getValuePost(klickadPost));
		}
		if (e.getSource() == radera) {
			Post rootPost;
			for (int i = 0; i < workspace.poster.size(); i++) {
				rootPost = workspace.poster.get(i);
				if (rootPost instanceof Grupp) {
					((Grupp)rootPost).removePost(klickadPost);
					((Grupp)rootPost).updateTotal(workspace.showFrom, workspace.showFrom);
				}
				if (rootPost.getName().equals(klickadPost)) {
					workspace.poster.removeElementAt(i);
					log("\n" + klickadPost + " har tagits bort");
				}
			}
			updateTotal();
			updateAllMilestones();
			uppdateraUtskriftsPanelen();
		} else if (e.getSource() == raderaT) {

			Post post;
			for (int i = 0; i < workspace.poster.size(); i++) {
				post = workspace.poster.get(i);
				if ((post.getName())
						.equals(klickadPost)) {

					workspace.poster.removeElementAt(i);
					log("\nGruppen " + klickadPost
							+ " har transformerats");
				}
			}
			updateTotal();
			updateAllMilestones();
			uppdateraUtskriftsPanelen();
		}
		if (e.getSource() == registreraInsUt) {
			try {
				new InsattningUtagUI(this, klickadPost);

			} catch (ClassNotFoundException c) {
				log("Fatal error");
			} catch (IOException i) {
				log("Fatal error");
			}
		} else if(e.getSource() == removeTransaction) {
			new RemoveTransactionUI(this, klickadPost);
		} else if(e.getSource() == removeMilestone){
			new RemoveMilestoneUI(this, klickadPost);
		} else if (e.getSource() == visaInsUt) {
			visaInsUtag(klickadPost);
		} else if(e.getSource() == backButton) {
			backButton.setVisible(false);
			rensaPanelen();
			uppdateraUtskriftsPanelen();
		} else if(e.getSource() == commandT) {
			handleCommand(commandT.getText());
		}
	}// metod

	private void addMilestone() {
		Calendar proposedDate;

		updateAllMilestones();

		// First find out proposed year
		if(allMilestoneDates.isEmpty()) {
			proposedDate = new MileStone();
			proposedDate.clear();
			proposedDate.set(Calendar.YEAR, 2010);
		} else {
			proposedDate = allMilestoneDates.lastElement().clone();
			int proposedYear = proposedDate.get(Calendar.YEAR);
			proposedDate.clear();
			proposedDate.set(Calendar.YEAR, proposedYear + 1);
		}

		new GetMilestoneDateUI(this, proposedDate);

	}

	private void handleCommand(String command) {
//		if(false/*add command comparision here*/) {
//			//Add command task here
//		} else {
//			showUpdateString("Command: " + command + " not recognised.");
//			commandT.setText("");
//		}
//		showUpdateString("Command: " + command + " performed.");
//		commandT.setText("");
	}

	private ValuePost getValuePost(String label) {
		for (Post rootPost : workspace.poster) {
			 if (rootPost instanceof Grupp) {
				for (ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					if (label.equals(childPost.getName())) {
						return childPost;
					}
				}
			} else if (label.equals(rootPost.getName())) {
				return (ValuePost)rootPost;
			}
		}
		return null;
	}

	public void rensaPanelen() {
		for (int i = 0; i < antalRader; i++)
			for (int j = 0; j < antalKolumner; j++) {
				utskriftsLabel[i][j].setText("");
				// utskriftsLabel[i][j].setFont(f);
				utskriftsPanel.add(utskriftsLabel[i][j]);
			}
	}

	public double setValue(double value, String unit, String typ, int rad, int column, int decimalCount) {
		double roundedValue;
		if (decimalCount == 0) {
			roundedValue = Math.round(value);
		} else {
			roundedValue = Math.round(value * decimalCount * 10) / (double)(decimalCount * 10);
		}
		if (unit == null) {
			setText(roundedValue, typ, rad, column);
		} else {
			setText(roundedValue + " " + unit, typ, rad, column);
		}
		return roundedValue;
	}

	public void setColoredValue(double value, String unit, String typ, int rad, int column, int decimalCount) {

		double roundedValue = setValue(value, unit, typ, rad, column, decimalCount);
		if(roundedValue > 0){
			utskriftsLabel[rad][column].setForeground(greenText);
		} else if( roundedValue  < -0.0001d){
			utskriftsLabel[rad][column].setForeground(redText);
		} else {
			utskriftsLabel[rad][column].setForeground(blackText);
		}
	}

	public void setText(Object text, String typ, int rad, int kolumn) {
		String textString = "";
		String firstPart;
		String secondPart;
		int length;
		if(text instanceof String) {
			textString = (String)text;
		} else if (text instanceof Integer || text instanceof Long) {
			textString = text.toString();
			length = textString.length();
			if(length > 3) {
				firstPart = textString.substring(0, length - 3);
				secondPart = textString.substring(length - 3, length);
				textString = firstPart + " " + secondPart;
			}
			length = textString.length();
			if(length > 7) {
				firstPart = textString.substring(0, length - 7);
				secondPart = textString.substring(length - 7, length);
				textString = firstPart + " " + secondPart;
			}

		} else if (text instanceof Double) {
			textString = text.toString();
			// length is here the length before first dot
			length = textString.indexOf('.');
			if(length > 3) {
				firstPart = textString.substring(0, length - 3);
				secondPart = textString.substring(length - 3, length);
				textString = firstPart + " " + secondPart;
			}
			length = textString.length();
			if(length > 7) {
				firstPart = textString.substring(0, length - 7);
				secondPart = textString.substring(length - 7, length);
				textString = firstPart + " " + secondPart;
			}
		}

		if (isGroup(typ) || typ.equals("group")) {
			utskriftsLabel[rad][kolumn].setFont(gruppFont);
			utskriftsLabel[rad][kolumn].setText(textString);
			return;
		}
		if (typ.equals("rubrik")) {
			utskriftsLabel[rad][kolumn].setFont(rubrikFont);
			utskriftsLabel[rad][kolumn].setText(textString);
			return;
		}
		utskriftsLabel[rad][kolumn].setFont(vardeFont);
		utskriftsLabel[rad][kolumn].setText(textString);
		return;
	}

	private void showYearChooserUI() {
		MileStone[] possibleChoises = new MileStone[allMilestoneDates.size() + 1];

		for (int i = 0; i < allMilestoneDates.size(); i++) {
			possibleChoises[i] = allMilestoneDates.get(i);
		}

		possibleChoises[possibleChoises.length -1] = new MileStone(workspace.lastUpdateDate) ;
		new YearChooserUI(this, possibleChoises);
	}

	public void updateTotal() {
		all = new Grupp("all");
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;

		for (Post rootPost : workspace.poster) {
			if (rootPost instanceof Grupp) {
				((Grupp)rootPost).updateTotal(from, to);
				for(ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					all.addPost(childPost);
				}
			} else {
				all.addPost((ValuePost)rootPost);
			}
		}
		all.updateTotal(from, to);
	}

	public void updateAllMilestones() {
		// TODO Test this one
		ValuePost valuePost;
		Vector<MileStone> mileStones;

		for (Post rootPost : workspace.poster) {
			if(rootPost instanceof ValuePost){
				valuePost = (ValuePost)rootPost;
				mileStones = valuePost.getMilestones();
				for (int j = 0; j < mileStones.size(); j++) {
					setToAllMilestones(mileStones.get(j));
				}
			} else if (rootPost instanceof Grupp) {
				for (ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					mileStones = childPost.getMilestones();
					for (int j = 0; j < mileStones.size(); j++) {
						setToAllMilestones(mileStones.get(j));
					}
				}
			}
		}
	}

	private void setToAllMilestones(MileStone milestone) {
		if(allMilestoneDates.isEmpty()) {
			allMilestoneDates.add(milestone);
		} else {
			boolean exists = false;
			for (int i = 0; i < allMilestoneDates.size(); i++) {
				if(allMilestoneDates.get(i).getTimeInMillis() == milestone.getTimeInMillis()) {
					exists = true;
				}
			}
			if(!exists){
				allMilestoneDates.add(milestone);
			}
		}
	}

	public void adjustValue(String postName, double amount, Calendar date) {
	    for (Post rootPost : workspace.poster) {
	        if(rootPost instanceof Grupp) {
	            if(((Grupp)rootPost).contains(postName)){
	                ((Grupp)rootPost).adjustValue(postName, date, amount);
	            }
	        } else {
	            if ((rootPost.getName()).equals(postName)) {
	                ((ValuePost) rootPost).adjustValue(date, amount);
	            }
	        }
	    }
	    updateTotal();
	    uppdateraUtskriftsPanelen();
	}

	public void ins(String postName, double amount, Calendar date, String comment) {
		// TODO Test this one
		all.addHappening(null, date, amount, comment);
		for (Post rootPost : workspace.poster) {
			if(rootPost instanceof Grupp) {
				if(((Grupp)rootPost).contains(postName)){
					((Grupp)rootPost).addHappening(postName, date, amount, comment);
				}
			} else {
				if ((rootPost.getName()).equals(postName)) {
					((ValuePost) rootPost).addHappening(date,
							amount, comment);
					log("\nInsättning har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nGrupp:  "
							+ postName + "\nKommentar:" + comment + "\n");
				}
			}
		}
		uppdateraUtskriftsPanelen();
	}

	public boolean isGroup(int index) {
		if (workspace.poster.elementAt(index) instanceof Grupp)
			return true;
		return false;
	}

	public void uppdateraUtskriftsPanelen() {
		rensaPanelen();

		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;

		titleLabel.setText(CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));

		log("Procentuell utveckling under perioden:");

		double allValue = all.getValue(to);
		double allValueStart = all.getValue(from);
		double allTransactions = all.getTotalAmount(from, to) - allValueStart;
		double allRevenue = allValue - allTransactions - allValueStart;
		log("Totalt: " + round((100 * allRevenue / (allValueStart + allTransactions))) + " %");

		int antal = workspace.poster.size();

		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
				if (isGroup(i)) {
					double value = currentPost.getValue(to);
					double valueStart = currentPost.getValue(from);
					double transactions = currentPost.getTotalAmount(from, to) - valueStart;
					double revenue = value - transactions - valueStart;
					log(currentPost.getName() + ": " + round((100 * revenue / (valueStart + transactions))) + " %");
				}
			}
		}


		int offset = 2; // "Dynamic" values start at row with index 2
		// ******************************* Name ****************************************
		int c = 0;

		setText("Innehav", "rubrik", 0, c);
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
				setText(currentPost.getName(), currentPost.name, i + offset, c);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						setText(värdePost.getName(), "", i + offset, c);
					}
				}
			} else {
				offset--;
			}
		}
		setText("Totalt", "rubrik", antal + offset, c);

		// ******************************* Value ****************************************
		c = 1;
		offset = 2;
		setText("Marknadspris", "rubrik", 0, c);
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
				setText(new Long(Math.round(currentPost.getValue(to))).intValue(),
						currentPost.name, i + offset, c);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						setText(new Long(Math.round(värdePost.getValue(to))).intValue(), "", i + offset, c);
					}
				}
			} else {
				offset--;
			}
		}
		setText(new Long(Math.round(all.getValue(to))).intValue(), "group", antal + offset, c);

		// ******************************* Allocation ****************************************
		c = 2;
		setText("Fördelning", "rubrik", 0, c);
		offset = 2;
		double tio = 10;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
				double pr = currentPost.getValue(to) / (all.getValue(to) * 0.01);
				setText(Math.round(pr*10)/tio + " %", currentPost.name, i + offset,	c);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						pr = värdePost.getValue(to) / (all.getValue(to) * 0.01);
						setText(Math.round(pr*10)/tio + " %", "", i + offset, c);
					}
				}
			} else {
				offset--;
			}
		}

		// ******************************* Yield *************************
		c = 3;
		setText("Effektiv årsränta", "rubrik", 0, c);
		offset = 2;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
				double ranta = currentPost.getInterest(from, to);
				setColoredValue(ranta, "%", currentPost.name, i + offset, c, 1);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						ranta = värdePost.getInterest(from, to);
						setColoredValue(ranta, "%", "", i + offset, c, 1);
					}
				}

			} else {
				offset--;
			}
		}
		double totalInterest = all.getInterest(from, to);
		setColoredValue(totalInterest, "%", "group", antal + offset, c, 1);

		// ******************************* Revenue ****************************************
		c = 4;
		setText("Avkastning", "rubrik", 0, c);
		double revenue;
		offset = 2;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
				revenue = (currentPost.getValue(to) - currentPost
						.getTotalAmount(from, to));
				setColoredValue(revenue, null, currentPost.name, i + offset, c, 1);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						revenue = (värdePost.getValue(to) - värdePost
								.getTotalAmount(from, to));
						setColoredValue(revenue, null, "", i + offset, c, 1);
					}
				}
			} else {
				offset--;
			}
		}
		double totalRevenue = all.getValue(to) - all.getTotalAmount(from, to);
		setColoredValue(totalRevenue, null, "group", antal + offset, c, 0);

		// ******************************* Transactions ****************************************
		c = 5;
		setText("Transaktioner", "rubrik", 0, c);
		offset = 2;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
				double balance = currentPost.getTotalAmount(from, to) - currentPost.getMilestoneValue(from);
				setValue(balance, null, currentPost.name, i + offset, c, 0);
				if (isGroup(i)) {
					List<ValuePost> groupPosts = ((Grupp)currentPost).getPosts(from, to);
					for (ValuePost värdePost : groupPosts) {
						offset++;
						balance = värdePost.getTotalAmount(from, to) - värdePost.getMilestoneValue(from);
						setValue(balance, null, "", i + offset, c, 0);
					}
				}
			} else {
				offset--;
			}
		}
		double totalBalance = all.getTotalAmount(from, to) - all.getMilestoneValue(from);
		setValue(totalBalance, null, "group", antal + offset, c, 0);
	}

	public void utag(String postName, double amount, Calendar date,	String comment) {
		// TODO Test this one
		amount = -1 * amount;
		all.addHappening(null, date, amount, comment);
		for (Post rootPost : workspace.poster) {
			if(rootPost instanceof Grupp) {
				if(((Grupp)rootPost).contains(postName)){
					((Grupp)rootPost).addHappening(postName, date, amount, comment);
				}
			} else {
				if ((rootPost.getName()).equals(postName)) {
					((ValuePost) rootPost).addHappening(date,
							amount, comment);
					log("\nUttag har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nGrupp:  "
							+ postName + "\nKommentar:" + comment + "\n");
				}
			}
		}
		uppdateraUtskriftsPanelen();
	}

	public void transaction(String from, String to, double amount, Calendar date, String comment) {
		all.addHappening(null, date, amount, comment);
		all.addHappening(null, date, (-1 * amount), comment);
		Grupp currentGroup;
		for (Post rootPost : workspace.poster) {
			if (rootPost instanceof Grupp) {
				currentGroup = (Grupp)rootPost;
				if (currentGroup.contains(from)) {
					currentGroup.addHappening(from, date, (-1 * amount), comment);
					log("\nUttag har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nPost:  "
							+ from + "\nKommentar:" + comment + "\n");

				}
				if (currentGroup.contains(to)) {
					currentGroup.addHappening(to, date, amount, comment);
					log("\nInsättning har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nPost:  "
							+ to + "\nKommentar:" + comment + "\n");

				}
			} else {
				if (rootPost.getName().equals(from)) {
					((ValuePost)rootPost).addHappening(date, (-1 * amount), comment);
					log("\nUttag har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nPost:  "
							+ from + "\nKommentar:" + comment + "\n");
				} else if (rootPost.getName().equals(to)) {
					((ValuePost)rootPost).addHappening(date, amount, comment);
					log("\nInsättning har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nPost:  "
							+ to + "\nKommentar:" + comment + "\n");
				}
			}
		}
		uppdateraUtskriftsPanelen();
	}

	private boolean isGroup(String name) {
		for (int i = 0; i < workspace.poster.size(); i++) {

			if (workspace.poster.elementAt(i).getName().equals(name)
					&& workspace.poster.elementAt(i) instanceof Grupp)
				return true;
		}
		return false;
	}

	public void printTextSumary() {
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;

		// TODO Fix tabs
		int antal = workspace.poster.size();

		if (antal == 0) {
			log("\nDet finns inga poster att visa");
			return;
		}



		log("\n\nEkonomisk situation för perioden ");
		log(CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));

		//List<List<String>> rows = new LinkedList<List<String>>();

//		List<String> row1 = new LinkedList<String>();
//
//		// ******************************* Name ****************************************
//
//		for (Post rootPost : workspace.poster) {
//
//			rows.add(new LinkedList<String>)rootPost.getName());
//			rows.
//			if (rootPost instanceof Grupp) {
//				for (VärdePost childPost : ((Grupp) rootPost).getPoster()) {
//					names.add(ellipsize(childPost.getName(),12));
//				}
//			}
//		}
//		log("Totalt");
//		names.add("Totalt");
//
//		// ******************************* Value ****************************************
//		log("\nDagsvärde:\t");
//		for (Post rootPost : workspace.poster) {
//			log(Math.round((rootPost).getValue(to))	+ "\t");
//			if (rootPost instanceof Grupp) {
//				for (VärdePost childPost : ((Grupp) rootPost).getPoster()) {
//					log(Math.round((childPost).getValue(to))	+ "\t");
//				}
//			}
//		}
//		log("" + Math.round(all.getValue(to)));
//
//		// ******************************* Allocation ****************************************
//		log("\nFördelning:\t");
//		double tio = 10;
//		for (Post rootPost : workspace.poster) {
//			double pr = (rootPost).getValue(to) / (all.getValue(to) * 0.01);
//			pr = Math.round(pr * 10) / tio;
//			log("" + pr + " %" + "\t");
//			if (rootPost instanceof Grupp) {
//				for (VärdePost childPost : ((Grupp) rootPost).getPoster()) {
//					pr = (childPost).getValue(to) / (all.getValue(to) * 0.01);
//					pr = Math.round(pr * 10) / tio;
//					log(pr + " %\t");				}
//			}
//		}
//
//		// ******************************* Yield ********************************
//		log("\nRänta:\t");
//
//		for (Post rootPost : workspace.poster) {
//			double ranta = (rootPost).getInterest(from, to);
//			log("" + Math.round(ranta * 10) / tio + " %" + "\t");
//			if (rootPost instanceof Grupp) {
//				for (VärdePost childPost : ((Grupp) rootPost).getPoster()) {
//					log(Math.round((childPost).getValue(to))	+ "\t");
//				}
//			}
//		}
//		textArean.append(Math.round(10 * all.getInterest(from, to)) / tio + " %");
//
//		// ******************************* Revenue ****************************************
//		log("\nAvkastning:\t");
//		for (Post rootPost : workspace.poster) {
//			log(Math.round((rootPost.getValue(to) - rootPost.getTotalAmount(from, to))) + "\t");
//			if (rootPost instanceof Grupp) {
//				for (VärdePost childPost : ((Grupp) rootPost).getPoster()) {
//					log(Math.round((childPost.getValue(to) - childPost.getTotalAmount(from, to))) + "\t");
//					}
//			}
//		}
//		log(Math.round(all.getValue(to) - all.getTotalAmount(from, to)) + "\n\n");
//
//

		for (int  i = 0 ; i < antalRader; i++) {
			for (int j = 0; j < antalKolumner; j++) {
				textArean.append(utskriftsLabel[i][j].getText() + "\t");
			}
			textArean.append("\n");
		}
	}

	public void skapaGrupp(String namn) {
		Grupp tempGrupp = new Grupp(namn);
		workspace.poster.addElement(tempGrupp);
	}

	public void createKontoPost(String namn, double belopp, Calendar date,
			String groupName) {
		// TODO Test this one
		KontoPost newPost = new KontoPost(namn, groupName, belopp, date);
		newPost.setValue(CalendarUtil.getTodayCalendarWithClearedClock(), belopp);

		Grupp group = getGroup(groupName);
		if (group != null) {
			group.addPost(newPost);
			group.addHappening(null, date, belopp, "");
		} else {
			workspace.poster.add(0,newPost);
		}

		all.addPost(newPost);
		all.addHappening(null, date, belopp, "");
		uppdateraUtskriftsPanelen();
	}

	public void createRawMaterialPost(String name, double price, double weight, Calendar date,
			String groupName) {
		// TODO Test this one
		RawMaterialPost newPost = new RawMaterialPost(name, groupName, weight, price, date);
		newPost.setValue(CalendarUtil.getTodayCalendarWithClearedClock(), price * weight);

		Grupp group = getGroup(groupName);
		if (group != null) {
			group.addPost(newPost);
			group.addHappening(null, date, price * weight, "");
		} else {
			workspace.poster.add(0,newPost);
		}

		all.addPost(newPost);
		all.addHappening(null, date, price * weight, "");
		uppdateraUtskriftsPanelen();
	}

	public void createStockPost(String name, int count, double price, Calendar date,
			URL url, String groupName) {
		// TODO Test this one
		AktiePost newStock = new AktiePost(name, groupName, count, price, date, url);
		newStock.setValue(CalendarUtil.getTodayCalendarWithClearedClock(), price * count);

		Grupp group = getGroup(groupName);
		if (group != null) {
			group.addPost(newStock);
			group.addHappening(null, date, count * price, "");
		} else {
			workspace.poster.add(0,newStock);
		}
		all.addPost(newStock);
		all.addHappening(null, date, count * price, "");
		uppdateraUtskriftsPanelen();
	}

	public Grupp getGroup(String name) {
		for (Post rootPost : workspace.poster) {
			if (rootPost instanceof Grupp && rootPost.getName().equals(name))
				return (Grupp)rootPost;
		}
		return null;
	}

	public double round(double value) {
		double tio = 10;
		return Math.round(value * 10) / tio;
	}

	public void visaInsUtag(String postNamn) {
		// TODO Test this one
		rensaPanelen();
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;
		backButton.setVisible(true);

		ValuePost post = getValuePost(postNamn);
		titleLabel.setText("Transaktioner, " + post.getName() + ", " + CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));

		List<Happening> happengings = post.getHappenings(from, to);
		for (int j = 0; j < happengings.size(); j++) {
			setText(CalendarUtil.getShortString(happengings.get(j).getDate()), "", j + 1, 0);
			setText(happengings.get(j).getAmount(), "", j + 1, 1);

			if (happengings.get(j).getKommentar().equals("")) {
				setText("Kommentar saknas", "", j + 1, 2);
			}// if
			else {
				setText(happengings.get(j).getKommentar(), "", j + 1, 2);
			}// else
		}// for
	}

	public void printTransactions() {
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;
		if (workspace.poster.size() == 0) {
			log("\nDet finns inga poster.");
			return;
		}
		for (Post rootPost : workspace.poster) {
			if (rootPost instanceof ValuePost) {
				logHappenings((ValuePost)rootPost, from, to);
			} else {
				for (ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					logHappenings(childPost, from, to);
				}
			}
		}// for
	}// metod

	private void logHappenings(ValuePost postToPrint, Calendar from, Calendar to) {
		log("\n" + "Förtydligande av insättningar och uttag för "
				+ postToPrint.getName() + "\n");
		Vector<Happening> happeningsInRange = postToPrint.createHappeningsInRange(from, to);
		for (Happening happening : happeningsInRange) {
			log(CalendarUtil.getShortString(happening.getDate()) + "\t");
			log(happening.getAmount() + "\t");
			if (happening.getKommentar() == "") {
				log("Kommentar saknas\n");
			} else {
				log(happening.getKommentar() + "\n");
			}
		}// for

	}

	public boolean isPostName(String name) {
		for (Post rootPost : workspace.poster) {
			if (rootPost instanceof Grupp) {
				Vector<ValuePost> childPosts = ((Grupp) rootPost).getPoster();
				for (ValuePost childPost : childPosts) {
					if (childPost.getName().equals(name)) {
						return true;
					}
				}
			}
			if (rootPost.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	WindowListener fonsterLyssnare = new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			try {
			saveWorkspace();
			System.exit(0);
			} catch (JSONException | URISyntaxException | IOException exc) {
				exc.printStackTrace();
				log("Could not save workspace");
			}
		}
	};

	private void saveWorkspace() throws JSONException, URISyntaxException, IOException {
		try {
			String filePath = SavedData.getLastSavedWorkspace();
			//FileUtil.writeObjectToFile(workspace, filePath);
			FileUtil.writeJsonToFile(workspace.getJson(), filePath);
		} catch(NullPointerException npe){
			saveWorkspaceAs();
		}
	}

	private void saveWorkspaceAs() throws JSONException, URISyntaxException, IOException {
		SavedData.setLastSavedWorkspace(FileUtil.writeJsonToUserSpecifiedFile(
				getContentPane(), workspace.getJson()));
		try {
			SavedData.save();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}



	private void openLastUsedWorkspace() {
		try {
			SavedData.load(this);
			if(SavedData.gotLastUsedWorkspace()) {
				log("Loading workspace...");
				//workspace = (Workspace)FileUtil.readObjectFromFile(this, SavedData.getLastSavedWorkspace());
				try {
					workspace = new Workspace(FileUtil.readFromJsonFile(SavedData.getLastSavedWorkspace()));
				} catch(Exception e) {
					e.printStackTrace();
					workspace = new Workspace();
				}
			} else {
				workspace = new Workspace();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return;
		} catch(ClassCastException cce) {
			cce.printStackTrace();
			return;
		}
		log("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " - " + CalendarUtil.getShortString(workspace.showTo));
		updateTotal();
		updateAllMilestones();
		uppdateraUtskriftsPanelen();
	}

	private void printAllMilestones() {
		log("Visar alla milstolpar: ");
		Vector<Post> rootPosts = workspace.poster;
		Post rootPost;
		Vector<MileStone> milestones;
		for(int i = 0; i < rootPosts.size(); i++) {
			rootPost = rootPosts.elementAt(i);
			if(isGroup(i)) {
				log(rootPost.getName() + ":");
				for (ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					log("   " + childPost.getName() + ":");
					milestones = childPost.getMilestones();
					for(int j = 0; j < milestones.size(); j++) {
						log("   " + CalendarUtil.getShortString(milestones.get(j)) + " \t\t" + milestones.get(j).getValue());
					}
					log("");
				}
			} else {
				log(rootPost.getName() + ":");
				milestones = ((ValuePost)rootPost).getMilestones();
				for(int j = 0; j < milestones.size(); j++) {
					log(CalendarUtil.getShortString(milestones.get(j)) + " \t\t" + milestones.get(j).getValue());
				}
				log("");
			}
		}
	}

	public void setFilterDates(Calendar from, Calendar to) {
		workspace.showFrom = from;
		workspace.showTo = to;
		log("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " -> " + CalendarUtil.getShortString(workspace.showTo));
		updateTotal();
		uppdateraUtskriftsPanelen();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		KalkylUI kalkylUI = new KalkylUI();
		kalkylUI.openLastUsedWorkspace();
	}

	public void stockUpdated() {
		updateTotal();
		uppdateraUtskriftsPanelen();
	}

	public void setMilestone(Calendar date) {
		// TODO Adapt to new post in group strategy
//		for(int i = 0; i < workspace.poster.size(); i++) {
//			if(isGroup(i)){
//				//do nothing
//			} else {
//				((VärdePost)workspace.poster.elementAt(i)).addMileStone(date);
//			}
//		}
		//updateAllMilestones();
		//updateTotal();
		//uppdateraUtskriftsPanelen();





		Vector<Post> rootPosts = workspace.poster;
		Post rootPost;
		for(int i = 0; i < rootPosts.size(); i++) {
			rootPost = rootPosts.elementAt(i);
			if(isGroup(i)) {
				for (ValuePost childPost : ((Grupp)rootPost).getPoster()) {
					childPost.addMileStone(date);
				}
			} else {
				((ValuePost)rootPosts.elementAt(i)).addMileStone(date);
			}
		}


		updateAllMilestones();
		updateTotal();
		uppdateraUtskriftsPanelen();

	}


	/* (non-Javadoc)
	 * @see com.roberthelmbro.economy.DataUpdateListener#removeTransaction(java.lang.String, java.util.Calendar, double)
	 */
	public void removeTransaction(String postName, Calendar date, double amount) {
		getValuePost(postName).deleteHappening(date, amount);
		updateTotal();
		uppdateraUtskriftsPanelen();
	}

	public void removeMilestone(String post, Calendar date) {
	    // TODO Test this one
	    getValuePost(post).deleteMilestone(date);
		updateTotal();
		uppdateraUtskriftsPanelen();
	}

	private double interestToday = 0;
	public void reportInterest (String name, double add) {
		interestToday += add;
		log("Ränta inrapporterad");
		log("Post: " + name);
		log("Ränta: " + add);
		log("Totalt hittils: " + interestToday);
	}

	public void log(String message) {
		textArean.append(message + "\n");
		JViewport vp = textAreaScrollPane.getViewport();
		Rectangle visible = vp.getVisibleRect();
		visible.y = 99999999/*Just a big value*/;
		vp.scrollRectToVisible(visible);
	}

}// class
