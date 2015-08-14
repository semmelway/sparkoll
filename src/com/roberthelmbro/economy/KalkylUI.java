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
import com.roberthelmbro.util.FileUtil.ObjectAndString;

public class KalkylUI extends JFrame implements ActionListener, GetMilestoneDateListener, StockInfoInputUiListener, PrintUpdate, YearChooserUiListener, DataUpdateListener  {
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
	private JMenuItem skrivOversyn = new JMenuItem("Skriv ut översyn");
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
		loggM.add(skrivOversyn);
		loggM.add(skrivUtInsUtag);
		loggM.add(printMilestones);
		skrivOversyn.addActionListener(this);
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
		popupMenyn.add(registreraInsUt);
		popupMenyn.add(visaInsUt);
		popupMenyn.add(removeTransaction);
		popupMenyn.add(removeMilestone);
		
		transactionFrom.addActionListener(this);
		transactionTo.addActionListener(this);
		edit.addActionListener(this);
		radera.addActionListener(this);
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
				if (kollaGruppNamn(klickadPost))
					return;

				edit.setText("Redigera " + klickadPost);
				radera.setText("Radera " + klickadPost);
				

				if (isGroup(klickadPost)) {
					registreraInsUt.setEnabled(false);
					visaInsUt.setEnabled(false);
					removeTransaction.setEnabled(false);
					removeMilestone.setEnabled(false);
				} else {
					registreraInsUt.setEnabled(true);
					visaInsUt.setEnabled(true);
					removeTransaction.setEnabled(true);
					removeMilestone.setEnabled(true);
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

			// uppdaterar alla VardePoster
			for (int i = 0; i < workspace.poster.size(); i++) {
				if (!isGroup(i)) {
					VärdePost currentPost = (VärdePost)workspace.poster.get(i);
					if (currentPost.isActive(workspace.showFrom, workspace.showTo)) {
						((VärdePost) workspace.poster.elementAt(i)).uppdateraVarde(this);
					}
				}
			}
			workspace.lastUpdateDate = CalendarUtil.getTodayCalendarWithClearedClock();
			workspace.showTo = workspace.lastUpdateDate;
			showUpdateString("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " -> " + CalendarUtil.getShortString(workspace.showTo));
			uppdateraUtskriftsPanelen();
			
		} else if(e.getSource() == addMilestoneMenuItem) {
			addMilestone();
		}
		if (e.getSource() == quitMenuItem) {
			try {
				saveWorkspace();
			} catch (JSONException | URISyntaxException | IOException e1) {
				// TODO Auto-generated catch block
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
		if (e.getSource() == skrivOversyn) {
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
			if(isStock(klickadPost)){
				new StockInfoInputUI(this, (AktiePost)getPost(klickadPost));
			}
		}
		if (e.getSource() == radera) {
			Post post;
			for (int i = 0; i < workspace.poster.size(); i++) {
				post = workspace.poster.get(i);
				if (post instanceof Grupp) {
					((Grupp)post).removePost(klickadPost);
					((Grupp)post).updateTotal(workspace.showFrom, workspace.showFrom);
				}
				if (((String) (((Post) post).getName()))
						.equals(klickadPost)) {
					if (post instanceof Grupp) {
						for (int j = i + 1; j <= i
								+ ((Grupp) post).getPoster()
										.size(); j++) {
							workspace.poster.remove(i + 1);
						}

					}
					workspace.poster.removeElementAt(i);
					log("\nGruppen " + klickadPost
							+ " har tagits bort");
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

	private void showYearChooserUI() {
		MileStone[] possibleChoises = new MileStone[allMilestoneDates.size() + 1];
		
		for (int i = 0; i < allMilestoneDates.size(); i++) {
			possibleChoises[i] = allMilestoneDates.get(i);
		}

		possibleChoises[possibleChoises.length -1] = new MileStone(workspace.lastUpdateDate) ;
		new YearChooserUI(this, possibleChoises);
	}

	private void printAllMilestones() {
		showUpdateString("Visar alla milstolpar: ");
		
		Vector<Post> items = workspace.poster;
		Post item;
		Vector<MileStone> milestones;
		
		for(int i = 0; i < items.size(); i++) {
			if(!isGroup(i)){
				item = items.elementAt(i);
				showUpdateString(item.getName() + ":");
				milestones = ((VärdePost)item).getMilestones();
				for(int j = 0; j < milestones.size(); j++) {
					showUpdateString(CalendarUtil.getShortString(milestones.get(j)) + " \t\t" + milestones.get(j).getValue());
				}
			}
		}
	}

	public boolean isStock(String label) {
		for(int i = 0; i < workspace.poster.size(); i++) {
			if(label.equals(workspace.poster.elementAt(i).getName())) {
				if(workspace.poster.elementAt(i) instanceof AktiePost) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Post getPost(String label) {
		for(int i = 0; i < workspace.poster.size(); i++) {
			if(label.equals(workspace.poster.elementAt(i).getName())) {
				return workspace.poster.elementAt(i);
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

	public void updateTotal() {
		all = new Grupp("all");
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;

		for (int i = 0; i < workspace.poster.size(); i++) {
			if (isGroup(i)) {
				((Grupp)workspace.poster.elementAt(i)).updateTotal(from, to);
			} else {
				all.addPost((VärdePost)workspace.poster.elementAt(i));
			}
		}
		all.updateTotal(from, to);
	}
	
	public void updateAllMilestones() {
		Vector<Post> poster = workspace.poster;
		Post post;
		VärdePost valuePost;
		Vector<MileStone> mileStones;
		
		for (int i = 0; i < poster.size(); i++) {
			post = poster.get(i);
			if(post instanceof VärdePost){
				valuePost = (VärdePost)post;
				mileStones = valuePost.getMilestones();
				for (int j = 0; j < mileStones.size(); j++) {
					setToAllMilestones(mileStones.get(j));
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

	public void uppdateraUtskriftsPanelen() {
		rensaPanelen();
		
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;

		titleLabel.setText(CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));
		
		
		int antal = workspace.poster.size();
		int decr = 0;
		// *******************************Gruppnamn****************************************
		int kolumn = 0;

		setText("Post", "rubrik", 0, kolumn);
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
			setText(currentPost.getName(), currentPost.name, i + 2 - decr, kolumn);
			} else {
				decr++;
			}
		}

		setText("Totalt", "rubrik", antal + 2 - decr, kolumn);

		// *******************************Varde****************************************
		kolumn = 1;
		decr = 0;
		setText("Varde", "rubrik", 0, kolumn);
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
			setText(new Long(Math.round(currentPost.getValue(to))).intValue(),
					currentPost.name, i + 2 - decr, kolumn);
		} else {
			decr++;
		}
		}
		setText(new Long(Math.round(all.getValue(to))).intValue(), "group", antal + 2 - decr, kolumn);

		// *******************************Fardelning****************************************
		kolumn = 2;
		setText("Fardelning", "rubrik", 0, kolumn);
		decr = 0;
		double tio = 10;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if (currentPost.isActive(from, to)) {
			double pr = currentPost.getValue(to)
					/ (all.getValue(to) * 0.01);

			setText("" + Math.round(pr*10)/tio + " %", workspace.poster.elementAt(i).name, i + 2 - decr,
					kolumn);
			} else {
				decr++;
			}

		}

		// *******************************Effektiv_årsränta*************************
		kolumn = 3;
		setText("Effektiv årsränta", "rubrik", 0, kolumn);
		decr = 0;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
				double ranta = currentPost.getInterest(from, to);
				ranta = round(ranta);
				setText("" + ranta + " %", currentPost.name, i + 2 - decr,
						kolumn);
				if(ranta > 0){
					utskriftsLabel[i+2 - decr][kolumn].setForeground(greenText);
				} else if( ranta  < -0.1d){
					utskriftsLabel[i+2 - decr][kolumn].setForeground(redText);
				} else {
					utskriftsLabel[i+2 - decr][kolumn].setForeground(blackText);
				}
			} else {
				decr++;
			}
		}
		double totalInterest = round(all.getInterest(from, to));
		setText("" + totalInterest + " %", "group", antal + 2 - decr,
				kolumn);
		if(totalInterest > 0){
		    utskriftsLabel[antal+2 - decr][kolumn].setForeground(greenText);
		} else if( totalInterest  < -0.1d){
			utskriftsLabel[antal+2 - decr][kolumn].setForeground(redText);
		} else {
			utskriftsLabel[antal+2 - decr][kolumn].setForeground(blackText);
		}

		// *******************************Avkastning****************************************
		kolumn = 4;
		setText("Avkastning", "rubrik", 0, kolumn);
		double revenue;
		decr = 0;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
			revenue = Math.round((currentPost.getValue(to) - currentPost
					.getTotalAmount(from, to)));
			setText(revenue, currentPost.name,
					i + 2 - decr, kolumn);
			if(revenue > 0){
			    utskriftsLabel[i+2 - decr][kolumn].setForeground(greenText);
			} else if( revenue  < -1){
				utskriftsLabel[i+2 - decr][kolumn].setForeground(redText);
			} else {
				utskriftsLabel[i+2 - decr][kolumn].setForeground(blackText);
			}
			} else {
				decr++;
			}
		}
		double totalRevenue = Math.round(all.getValue(to) - all.getTotalAmount(from, to));
		setText(totalRevenue, "group",
				antal + 2 - decr, kolumn);
		if(totalRevenue > 0){
		    utskriftsLabel[antal+2 - decr][kolumn].setForeground(greenText);
		} else if( totalRevenue  < -1){
			utskriftsLabel[antal+2 - decr][kolumn].setForeground(redText);
		}
		
		// *******************************Avkastning****************************************
		kolumn = 5;
		setText("Transaktioner", "rubrik", 0, kolumn);
		decr = 0;
		for (int i = 0; i < antal; i++) {
			Post currentPost = workspace.poster.get(i);
			if(currentPost.isActive(from, to)) {
				double balance = Math.round(( currentPost.getTotalAmount(from, to) - currentPost.getMilestoneValue(from)));
			setText(balance, currentPost.name,
					i + 2 - decr, kolumn);
			} else {
				decr++;
			}
		}
		double totalBalance= Math.round(all.getTotalAmount(from, to) - all.getMilestoneValue(from));
		setText(totalBalance, "group",
				antal + 2 - decr, kolumn);
		if(totalBalance > 0){
		    utskriftsLabel[antal+2 - decr][kolumn].setForeground(greenText);
		} else if( totalBalance  < -1){
			utskriftsLabel[antal+2 - decr][kolumn].setForeground(redText);
		}
		
	}

	public boolean isGroup(int index) {
		if (workspace.poster.elementAt(index) instanceof Grupp)
			return true;
		return false;
	}

	public void ins(String benamning, double amount, Calendar datum,
			String kommentar) {
		// uppdatera kalkyl posten
		all.addHappening(datum, amount);
		for (int i = 0; i < workspace.poster.size(); i++) {
			if(isGroup(i)) {
				if(((Grupp)workspace.poster.elementAt(i)).contains(benamning)){
					((Grupp)workspace.poster.elementAt(i)).addHappening(datum, amount);
				}
			} else {
				if ((((Post) workspace.poster.elementAt(i)).getName()).equals(benamning)) {
					((VärdePost) workspace.poster.elementAt(i)).addHappening(datum,
							amount, kommentar);
					log("\n\nInsättning har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(datum) + "\nGrupp:  "
							+ benamning + "\nKommentar:" + kommentar + "\n");
				}
			}
		}
		uppdateraUtskriftsPanelen();
	}

	public void utag(String benamning, double amount, Calendar datum,
			String kommentar) {
		// uppdatera kalkyl posten
		all.addHappening(datum,(-1 * amount));
		for (int i = 0; i < workspace.poster.size(); i++) {
			if(isGroup(i)) {
				if(((Grupp)workspace.poster.elementAt(i)).contains(benamning)){
					((Grupp)workspace.poster.elementAt(i)).addHappening(datum,(-1 * amount));
				}
			} else {
				if ((((Post) workspace.poster.elementAt(i)).getName()).equals(benamning)) {
					((VärdePost) workspace.poster.elementAt(i)).addHappening(datum,
							(-1 * amount), kommentar);
					log("\n\nUtag har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(datum) + "\nGrupp:  "
							+ benamning + "\nKommentar:" + kommentar + "\n");
				}
			}
		}
		uppdateraUtskriftsPanelen();
	}
	
	public void transaction(String from, String to, double amount, Calendar date, String comment) {
		all.addHappening(date, amount);
		all.addHappening(date, (-1 * amount));
		
		Grupp currentGroup;
		VärdePost currentPost;
		for (int i = 0; i < workspace.poster.size(); i++) {
			if (isGroup(i)) {
				currentGroup = (Grupp)workspace.poster.elementAt(i);	
				if (currentGroup.getName().equals(from)) {
					currentGroup.addHappening(date, (-1 * amount));
				} else if (currentGroup.getName().equals(to)) {
					currentGroup.addHappening(date, amount);
				}
			} else {
				currentPost = (VärdePost)workspace.poster.elementAt(i);
				if (currentPost.getName().equals(from)) {
					currentPost.addHappening(date, (-1 * amount), comment);
					log("\n\nUtag har registrerats. \nBelopp: "
							+ amount + " kr\nDatum: " + CalendarUtil.getShortString(date) + "\nPost:  "
							+ from + "\nKommentar:" + comment + "\n");
				} else if (currentPost.getName().equals(to)) {
					currentPost.addHappening(date, amount, comment);
					log("\n\nInsättning har registrerats. \nBelopp: "
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

		int antal = workspace.poster.size();

		if (antal == 0) {
			log("\nDet finns inga poster att visa");
			return;
			
		}
		
		log("\n\nEkonomisk situation för perioden ");
		log(CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));
		
		// *******************************Gruppnamn****************************************
		log("\nGrupp:\t");

		for (int i = 0; i < antal; i++) {
			log(
					ellipsize(((Post) workspace.poster.elementAt(i)).getName(),12)
					+ "\t");
		}
		log("Totalt");

		// *******************************Varde****************************************

		log("\nDagsvarde:\t");
		for (int i = 0; i < antal; i++) {
			log("" + Math.round(((Post) workspace.poster.elementAt(i)).getValue(to))
					+ "\t");
		}
		log("" + Math.round(all.getValue(to)));

		// ******************************* Fardelning ****************************************

		log("\nFardelning:\t");
		double tio = 10;
		for (int i = 0; i < antal; i++) {
			double pr = ((Post) workspace.poster.elementAt(i)).getValue(to)
					/ (all.getValue(to) * 0.01);
			pr = Math.round(pr * 10) / tio;
			log("" + pr + " %" + "\t");
		}

		// ******************************* Effektiv Årsränta ********************************
		log("\nRänta:\t");

		for (int i = 0; i < antal; i++) {
			double ranta = ((Post) workspace.poster.elementAt(i)).getInterest(from, to);

			log("" + Math.round(ranta * 10) / tio + " %" + "\t");
		}
		textArean
				.append("" + Math.round(10 * all.getInterest(from, to)) / tio + " %");

		// ******************************* Avkastning ****************************************
		log("\nAvkastning:\t");
		for (int i = 0; i < antal; i++) {
			log(""
					+ Math.round((workspace.poster.elementAt(i).getValue(to) - workspace.poster.elementAt(i)
							.getTotalAmount(from, to))) + "\t");
		}
		log("" + Math.round(all.getValue(to) - all.getTotalAmount(from, to)) + "\n\n");

		// *******************************Avkastning
		// slut****************************************
	}
	
	private String ellipsize(String stringToEllipsize, int length){
		if(stringToEllipsize.length() <= length + 1)
			return stringToEllipsize;
		
		return stringToEllipsize.substring(0,length) + ".";
	}

	public void skapaGrupp(String namn) {
		Grupp tempGrupp = new Grupp(namn);
		workspace.poster.addElement(tempGrupp);

	}



	public void skapaKontoPost(String namn, double belopp, Calendar date,
			String grupp) {
		KontoPost tempKontoPost = new KontoPost(namn, grupp, belopp, date);
		tempKontoPost.setValue(CalendarUtil.getTodayCalendarWithClearedClock(), belopp);

		all.addPost(tempKontoPost);
		all.addHappening(date, belopp);
		
		if (isGroup(grupp)) {
			((Grupp) workspace.poster.elementAt(getPostIndex(grupp)))
					.addPost(tempKontoPost);
			((Grupp) workspace.poster.elementAt(getPostIndex(grupp))).addHappening(date, belopp);
			workspace.poster.add(getPostIndex(grupp) + 1, tempKontoPost);
		} else {
			workspace.poster.add(0,tempKontoPost);
		}
		uppdateraUtskriftsPanelen();
	}
	

	public void createRawMaterialPost(String name, double price, double weight, Calendar date,
			String grupp) {
		RawMaterialPost tempPost = new RawMaterialPost(name, grupp, weight, price, date);
		Calendar now = CalendarUtil.getTodayCalendarWithClearedClock();
		tempPost.setValue(now, price * weight);

		all.addPost(tempPost);
		all.addHappening(date, price * weight);
		
		if (isGroup(grupp)) {
			((Grupp) workspace.poster.elementAt(getPostIndex(grupp)))
					.addPost(tempPost);
			((Grupp) workspace.poster.elementAt(getPostIndex(grupp))).addHappening(date, price * weight);
			workspace.poster.add(getPostIndex(grupp) + 1, tempPost);
		} else {
			workspace.poster.add(0,tempPost);
		}
		uppdateraUtskriftsPanelen();
	}

	public int getPostIndex(String namn) {
		for (int i = 0; i < workspace.poster.size(); i++) {
			if (workspace.poster.elementAt(i).getName().equals(namn))
				return i;
		}
		return -1;
	}

	public double round(double value) {
		double tio = 10;
		return Math.round(value * 10) / tio;
	}

	public void visaInsUtag(String postNamn) {
		rensaPanelen();
		Calendar from = workspace.showFrom;
		Calendar to = workspace.showTo;
		backButton.setVisible(true);
		int i = getPostIndex(postNamn);
		VärdePost post = (VärdePost)workspace.poster.elementAt(i);
		titleLabel.setText("Transaktioner, " + post.getName() + ", " + CalendarUtil.getShortString(from) + " - " + CalendarUtil.getShortString(to));
		

		List<Happening> happengings = workspace.poster.elementAt(i).getHappenings(from, to);
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
			log("\nDet finns inga grupper.");
			return;
		}
		for (int i = 0; i < workspace.poster.size(); i++) {
			if (workspace.poster.elementAt(i) instanceof VärdePost) {
				VärdePost postToPrint = (VärdePost)workspace.poster.elementAt(i);
				log("\n\n"
						+ "Förtydligande av Insättningar och utag för "
						+ ((String) (((Post) workspace.poster.elementAt(i)).getName()))
						+ "\n\n");
				Vector<Happening> happeningsInRange = postToPrint.createHappeningsInRange(from, to);
				for (int j = 0; j < happeningsInRange.size(); j++) {
					log( CalendarUtil.getShortString(happeningsInRange.elementAt(j).getDate())
							+ "\t");
					log(""
							+ happeningsInRange.elementAt(j).getAmount() + "\t");
					if (happeningsInRange.elementAt(j).getKommentar() == "") {
						log("Kommentar saknas\n");
					} else {
						log(happeningsInRange.elementAt(j).getKommentar() + "\n");
					}
				}// for
			}// if
		}// for

	}// metod

	public boolean finsPost(String namn) {
		for (int i = 0; i < workspace.poster.size(); i++) {
			if (((String) (((Post) workspace.poster.elementAt(i)).getName()))
					.equals(namn)) {
				return true;
			}
		}
		return false;
	}

	public boolean kollaGruppNamn(String benamning) {
		if (benamning.length() == 0) {
			log("\n" + "Du måste ange ett gruppnamn");
			return true;

		}

		for (int i = 0; i < workspace.poster.size(); i++) {
			if (((String) (((Post) workspace.poster.elementAt(i)).getName()))
					.equals(benamning)) {
				return false;
			}
		}
		log("\n" + "Gruppen " + benamning + " finns ej.");
		return true;
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
				this.showUpdateString("Loading workspace...");
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
		showUpdateString("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " - " + CalendarUtil.getShortString(workspace.showTo));
		updateTotal();
		updateAllMilestones();
		uppdateraUtskriftsPanelen();
	}

	public void addStock(String name, int count, double price, Calendar date,
			URL url, String groupName) {
		AktiePost stock = new AktiePost(name, groupName, count, price, date, url);
		stock.setValue(CalendarUtil.getTodayCalendarWithClearedClock(), price);

		all.addPost(stock);
		all.addHappening(date, count * price);

		if (isGroup(groupName)) {
			((Grupp) workspace.poster.elementAt(getPostIndex(groupName)))
			.addPost(stock);
			((Grupp) workspace.poster.elementAt(getPostIndex(groupName))).addHappening(date, count
					* price);
			workspace.poster.add(getPostIndex(groupName) + 1, stock);
		} else {
			workspace.poster.add(0,stock);
		}
		uppdateraUtskriftsPanelen();
	}

	public boolean isPostPresent(String name) {
		for (int i = 0; i < workspace.poster.size(); i++) {
			if (((String) (((Post) workspace.poster.elementAt(i)).getName()))
					.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void showUpdateString(String updateText) {
		log(updateText + "\n");
	}
	
	public void setFilterDates(Calendar from, Calendar to) {
		workspace.showFrom = from;
		workspace.showTo = to;
		showUpdateString("Viewing period: " + CalendarUtil.getShortString(workspace.showFrom) + " -> " + CalendarUtil.getShortString(workspace.showTo));
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
		for(int i = 0; i < workspace.poster.size(); i++) {
			if(isGroup(i)){
				//do nothing
			} else {
				((VärdePost)workspace.poster.elementAt(i)).addMileStone(date);
			}
		}
		//updateAllMilestones();
		//updateTotal();
		//uppdateraUtskriftsPanelen();
	}

	
	/* (non-Javadoc)
	 * @see com.roberthelmbro.economy.DataUpdateListener#removeTransaction(java.lang.String, java.util.Calendar, double)
	 */
	public void removeTransaction(String post, Calendar date, double amount) {
		((VärdePost)getPost(post)).deleteHappening(date, amount);
		updateTotal();
		uppdateraUtskriftsPanelen();
	}
	
	public void removeMilestone(String post, Calendar date) {
		((VärdePost)getPost(post)).deleteMilestone(date);
		updateTotal();
		uppdateraUtskriftsPanelen();
	}
	
	private double interestToday = 0;
	public void reportInterest (String name, double add) {
		interestToday += add;
		showUpdateString("Ränta inrapporterad");
		showUpdateString("Post: " + name);
		showUpdateString("Ränta: " + add);
		showUpdateString("Totalt hittils: " + interestToday);  
	}
	
	private void log(String message) {
		textArean.append(message);
		JViewport vp = textAreaScrollPane.getViewport();
		Rectangle visible = vp.getVisibleRect();
		visible.y = 99999999/*Just a big value*/;
		vp.scrollRectToVisible(visible);
	}
	
}// class
