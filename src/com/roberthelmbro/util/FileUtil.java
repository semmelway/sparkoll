/**
 * @author Robert Helmbro
 */
package com.roberthelmbro.util;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

import com.roberthelmbro.economy.PrintUpdate;

public class FileUtil {

	public static Object readObjectFromFile(PrintUpdate statusListener, String filePath)
			throws IOException, ClassNotFoundException {
		File sessionData = new File(filePath);
		statusListener.showUpdateString("Reading from file: " + sessionData.getAbsolutePath());
		Object returnObject = null;
		ObjectInputStream inFil = new ObjectInputStream(new FileInputStream(
				sessionData));
		returnObject = inFil.readObject();
		inFil.close();
		statusListener.showUpdateString("Reading the file performed with success!");
		return returnObject;
	}

	public static void writeObjectToFile(Object objectToWrite, String filePath)
			throws IOException {
		ObjectOutputStream utFil = new ObjectOutputStream(new FileOutputStream(
				new File(filePath)));
		utFil.writeObject(objectToWrite);
		utFil.close();
	}

	public static ObjectAndString readObjectFromUserSpecifiedFile(Component component) {
		// String filnamn="";
		ObjectAndString returnObject = new ObjectAndString();
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(component);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filnamn=chooser.getSelectedFile().getName();
			File selectedFile = chooser.getSelectedFile();
			try {
				ObjectInputStream inFil = new ObjectInputStream(
						new FileInputStream(selectedFile));
				returnObject.object = inFil.readObject();
				returnObject.string = selectedFile.getPath();
				inFil.close();
			} catch (Exception e) {
				System.out.println("Exception" + e);
			}
		}
		return returnObject;
	}

	public static String writeObjectToUserSpecifiedFile(Component component,
			Object objectToWrite) {
		File choosedFile = null;
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(component);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filnamn=chooser.getSelectedFile().getName();
			try {
				choosedFile = chooser.getSelectedFile();
				ObjectOutputStream utFil = new ObjectOutputStream(
						new FileOutputStream(choosedFile));
				utFil.writeObject(objectToWrite);
				utFil.close();
			} catch (IOException e) {
			}
		}
		try{
		return choosedFile.getPath();
		}catch(Exception e){
			return null;
		}
	}
	
	public static class ObjectAndString{
		public Object object;
		public String string;
	}
}
