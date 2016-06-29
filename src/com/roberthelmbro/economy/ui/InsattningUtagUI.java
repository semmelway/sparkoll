package com.roberthelmbro.economy.ui;
/**
 * @author Robert Helmbro
 */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;



import java.util.Calendar;

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
    private JLabel adjustValueL = new JLabel("Justera värde");
    private JLabel datumL= new JLabel("Datum(åååå-mm-dd)");
    private JLabel kommentarL = new JLabel("Kommentar");
    private JLabel meddelandeL = new JLabel("Meddelande");

    // RadioButtons
    private ButtonGroup insUtagSel = new ButtonGroup();
    private JRadioButton insattningR = new JRadioButton();
    private JRadioButton utagR = new JRadioButton();
    private JRadioButton adjustValueR = new JRadioButton();

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

    int fieldWidth = 180;
    int fieldHeight = 20;

    int vertDist = 10;
    int horDist = 20;

    int leftColumnWidth = 80;

    public InsattningUtagUI(KalkylUI k,String klickadPost) throws IOException,
    ClassNotFoundException {
        kalkyl=k;

        setTitle("Registrera insättning/utag");
        Container c= getContentPane();
        c.setLayout(null);
        setSize(600,320);

        int x = leftColumnWidth;
        int y = vertDist;

        // **************** Left columns ********
        c.add(insattningL);
        insattningL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(utagL);
        utagL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(benamningL);
        benamningL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(beloppL);
        beloppL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(adjustValueL);
        adjustValueL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(datumL);
        datumL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(kommentarL);
        kommentarL.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(meddelandeL);
        meddelandeL.setBounds(x, y, fieldWidth * 3, fieldHeight);

        // **************** Buttons ****************
        y += vertDist + fieldHeight;
        c.add(avbrytB);
        avbrytB.setBounds(x, y, fieldWidth, fieldHeight);
        avbrytB.addActionListener(this);

        x += fieldWidth + horDist;
        c.add(sparastangB);
        sparastangB.setBounds(x, y, fieldWidth, fieldHeight);
        sparastangB.addActionListener(this);

        // **************** Right column ************
        y = vertDist;
        insUtagSel.add(insattningR);
        c.add(insattningR);
        insattningR.addActionListener(this);
        insattningR.setBounds(x, y, fieldHeight, fieldHeight);

        y += vertDist + fieldHeight;
        insUtagSel.add(utagR);
        c.add(utagR);
        utagR.addActionListener(this);
        utagR.setBounds(x, y, fieldHeight, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(benomningT);
        benomningT.setBounds(x, y, fieldWidth, fieldHeight);
        benomningT.setText(klickadPost);

        y += vertDist + fieldHeight;
        c.add(beloppT);
        beloppT.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(adjustValueR);
        adjustValueR.setBounds(x, y, fieldHeight, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(datumT);
        datumT.setBounds(x, y, fieldWidth, fieldHeight);

        y += vertDist + fieldHeight;
        c.add(kommentarT);
        kommentarT.setBounds(x, y, fieldWidth, fieldHeight);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==avbrytB) {
            this.setVisible(false);
        } else if (e.getSource()==sparastangB) {
            if (insattningR.isSelected()) {
                this.ins();
                return;
            } else if(utagR.isSelected()) {
                this.utag();
                return;
            } else {
                meddelandeL.setText("Du måste ange insättning eller utag.");
            }
        }
    }//metod(actionPerformed)

    public void ins() {
        if (ParseCheckerTools.checkDate(datumT.getText()) != null) {
            meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
            return;
        }
        if (ParseCheckerTools.checkDouble(beloppT.getText()) != null) {
            meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
            return;
        }
        if (!kalkyl.isPostName(benomningT.getText())) {
            meddelandeL.setText("Du måste ange korrekt gruppnamn.");
            return;
        }

        String post = benomningT.getText();
        double value = ParseUtil.parseDouble(beloppT.getText());
        Calendar date = CalendarUtil.parseString(datumT.getText());

        kalkyl.ins(post, value, date, kommentarT.getText());
        if (adjustValueR.isSelected()) {
            kalkyl.adjustValue(post, value, date);
        }
        meddelandeL.setText("Insättning sparad.");
        this.setVisible(false);
    }

    public void utag() {
        if (ParseCheckerTools.checkDate(datumT.getText()) != null) {
            meddelandeL.setText(ParseCheckerTools.checkDate(datumT.getText()));
            return;
        }
        if (ParseCheckerTools.checkDouble(beloppT.getText()) != null) {
            meddelandeL.setText(ParseCheckerTools.checkDouble(beloppT.getText()));
            return;
        }
        if (!kalkyl.isPostName(benomningT.getText())) {
            meddelandeL.setText("Du måste ange korrekt gruppnamn.");
            return;
        }

        String post = benomningT.getText();
        double value = ParseUtil.parseDouble(beloppT.getText());
        Calendar date = CalendarUtil.parseString(datumT.getText());

        kalkyl.utag(post, value, date, kommentarT.getText());
        if (adjustValueR.isSelected()) {
            kalkyl.adjustValue(post, -1 * value, date);
        }

        meddelandeL.setText("Utag sparat.");
        this.setVisible(false);
    }

    public static void main(String[] args) {
        try {
            new InsattningUtagUI(null, "Dummy");
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}//class

