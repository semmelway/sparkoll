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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.json.JSONException;
import org.json.JSONObject;

import com.roberthelmbro.economy.PrintUpdate;

public class FileUtil {
	
	public static void writeJsonToFile(JSONObject json, String filePath) throws URISyntaxException, IOException {
		Path path = Paths.get(new URI("file://" + filePath));
		Files.write(path, json.toString().getBytes("UTF-8"));
	}
	
	public static JSONObject readFromJsonFile(String filePath) throws JSONException,
	       URISyntaxException, UnsupportedEncodingException, IOException {
		Path path = Paths.get(new URI("file://" + filePath));
		String sJson = new String(Files.readAllBytes(path), "UTF-8");
		return new JSONObject(sJson);
		
	}

//	public static Object readObjectFromFile(PrintUpdate statusListener, String filePath)
//			throws IOException, ClassNotFoundException {
//		File sessionData = new File(filePath);
//		statusListener.showUpdateString("Reading from file: " + sessionData.getAbsolutePath());
//		Object returnObject = null;
//		ObjectInputStream inFil = new ObjectInputStream(new FileInputStream(
//				sessionData));
//		returnObject = inFil.readObject();
//		inFil.close();
//		statusListener.showUpdateString("Reading the file performed with success!");
//		return returnObject;
//	}

//	public static void writeObjectToFile(Object objectToWrite, String filePath)
//			throws IOException {
//		ObjectOutputStream utFil = new ObjectOutputStream(new FileOutputStream(
//				new File(filePath)));
//		utFil.writeObject(objectToWrite);
//		utFil.close();
//	}
	
	public static void writeStringToFile(String stringToWrite, String filePath)
			throws IOException {
		ObjectOutputStream utFil = new ObjectOutputStream(new FileOutputStream(
				new File(filePath)));
		utFil.writeObject(stringToWrite);
		utFil.close();
	}
	
	public static String readStringFromFile(PrintUpdate statusListener, String filePath)
	        throws IOException, ClassNotFoundException {
		File sessionData = new File(filePath);
		statusListener.showUpdateString("Reading from file: " + sessionData.getAbsolutePath());
		String returnObject = null;
		ObjectInputStream inFil = new ObjectInputStream(new FileInputStream(
				sessionData));
		returnObject = (String)inFil.readObject();
		inFil.close();
		statusListener.showUpdateString("Reading the file performed with success!");
		return returnObject;
}
//
//	@Deprecated
//	public static ObjectAndString readObjectFromUserSpecifiedFile(Component component) {
//		// String filnamn="";
//		ObjectAndString returnObject = new ObjectAndString();
//		JFileChooser chooser = new JFileChooser();
//		int returnVal = chooser.showOpenDialog(component);
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			// filnamn=chooser.getSelectedFile().getName();
//			File selectedFile = chooser.getSelectedFile();
//			try {
//				ObjectInputStream inFil = new ObjectInputStream(
//						new FileInputStream(selectedFile));
//				returnObject.object = inFil.readObject();
//				returnObject.string = selectedFile.getPath();
//				inFil.close();
//			} catch (Exception e) {
//				System.out.println("Exception" + e);
//			}
//		}
//		return returnObject;
//	}

	public static JsonAndPath readJsonFromUserSpecifiedFile(Component component) {
		// String filnamn="";
		JsonAndPath returnObject = new JsonAndPath();
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(component);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filnamn=chooser.getSelectedFile().getName();
			File selectedFile = chooser.getSelectedFile();
			try {
				returnObject.json = readFromJsonFile(selectedFile.getPath());
				returnObject.path = selectedFile.getPath();
			} catch (Exception e) {
				System.out.println("Exception" + e);
			}
		}
		return returnObject;
	}


//	public static String writeWorkspaceToUserSpecifiedFile(Component component,
//			Workspace workspaceToWrite) {
//		File choosedFile = null;
//		JFileChooser chooser = new JFileChooser();
//		int returnVal = chooser.showSaveDialog(component);
//
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			// filnamn=chooser.getSelectedFile().getName();
//			try {
//				choosedFile = chooser.getSelectedFile();
//				ObjectOutputStream utFil = new ObjectOutputStream(
//						new FileOutputStream(choosedFile));
//				utFil.writeObject(workspaceToWrite);
//				utFil.close();
//			} catch (IOException e) {
//			}
//		}
//		try {
//			String path = choosedFile.getPath();
//			writeJsonToFile(workspaceToWrite.getJson(), path + ".json");
//			return path;
//		} catch(Exception e) {
//			return null;
//		}
//	}
	
	public static String writeJsonToUserSpecifiedFile(Component component,
			JSONObject json) throws IOException, URISyntaxException {
		File choosedFile = null;
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(component);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filnamn=chooser.getSelectedFile().getName();

			choosedFile = chooser.getSelectedFile();
			writeJsonToFile(json, choosedFile.getPath());
			return choosedFile.getPath();

		}
		return null;
	}
	
	public static class JsonAndPath {
		public JSONObject json;
		public String path;
	}
	
	@Deprecated
	public static class ObjectAndString{
		public Object object;
		public String string;
	}
}
