package com.roberthelmbro.economy;
/**
 * @author Robert Helmbro
 */
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
		FileUtil.writeStringToFile(lastSavedWorkspace, SESSION_DATA_FILE_NAME);
	}
	
	public static void load(KalkylUI statusListener) throws ClassNotFoundException, IOException{
		statusListener.log("Loading session data");
		lastSavedWorkspace = FileUtil.readStringFromFile(statusListener, SESSION_DATA_FILE_NAME);
		
		if(lastSavedWorkspace == null){
			statusListener.log("No workspace found");
		} else if (lastSavedWorkspace.length() == 0) {
			statusListener.log("No workspace found");
		} else {
			statusListener.log("Last used workspace was " + lastSavedWorkspace);
		}
	}

	public static void clear() {
		lastSavedWorkspace = null;
		
	}

}
