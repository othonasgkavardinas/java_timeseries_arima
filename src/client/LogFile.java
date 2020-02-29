package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The representation of a file, used for logs.
 *
 * @author Othonas Gkavardinas
 */
public class LogFile {

	private File file;
	private int lineNo;

	/**
	 * Creates and opens a log file for writing.
	 * 
	 */
	public LogFile() {
		lineNo = 0;

		String dateTime = String.valueOf(System.currentTimeMillis());
		file = new File("logs/log" + dateTime);	
		
		try {
			if(!file.createNewFile())
				System.out.println("File already exists.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Appends to a log file
	 * 
	 * @param text	A String to be appended.
	 */
	public void append(String text) {
		try {
			FileWriter fileWriter = new FileWriter(file, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println((lineNo++) + "\t" + text);
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}