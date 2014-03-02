package com.roberthelmbro.economy;

import java.io.IOException;
import java.io.Serializable;

import com.roberthelmbro.util.FileUtil;

public class SavedData implements Serializable{
	
	static final long serialVersionUID = 1;
	
	static final String SESSION_DATA_FILE_NAME = "sesionData.bin";
	
	private static String lastSavedWorkspace = null;
	
	
	public static void setLastSavedWorkspace(String lsf){
		lastSavedWorkspace = lsf;
	}
	
	public static String getLastSavedWorkspace(){
		return lastSavedWorkspace;
	}
	
	public static boolean gotLastUsedWorkspace() {
		if(lastSavedWorkspace == null){
			return false;
		} else if (lastSavedWorkspace.length() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void save() throws IOException{
		FileUtil.writeObjectToFile(lastSavedWorkspace, SESSION_DATA_FILE_NAME);
	}
	
	public static void load(PrintUpdate statusListener) throws ClassNotFoundException, IOException{
		statusListener.showUpdateString("Loading session data");
		lastSavedWorkspace = (String)FileUtil.readObjectFromFile(statusListener, SESSION_DATA_FILE_NAME);
		
		if(lastSavedWorkspace == null){
			statusListener.showUpdateString("No workspace found");
		} else if (lastSavedWorkspace.length() == 0) {
			statusListener.showUpdateString("No workspace found");
		} else {
			statusListener.showUpdateString("Last used workspace was " + lastSavedWorkspace);
		}
	}

	public static void clear() {
		lastSavedWorkspace = null;
		
	}

}
