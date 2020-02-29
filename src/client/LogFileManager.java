package client;

/**
 * A class that utilizes a log file.
 *
 * @author Othonas Gkavardinas
 */
public class LogFileManager {
	
	private LogFile logFile;
	
	/**
	 * Sets logFileManager's logFile to a new LogFile.
	 * 
	 */
	public void initLog() {
		logFile = new LogFile();
	}
	
	/**
	 * Appends to logFileManager's logFile.
	 * 
	 * @param text	A String to be appended in logFile.
	 */
	public void appendLog(String text) {
		logFile.append(text);
	}
	
}
