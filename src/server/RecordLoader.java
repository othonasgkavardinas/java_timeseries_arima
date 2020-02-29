package server;

import java.util.ArrayList;


/**
 * A class responsible for loading timeSeries objects from files.
 *
 * @author Othonas Gkavardinas
 */
public interface RecordLoader {

	/**
	 * Loads a timeSeries from a file.
	 * 
	 * @param fileName	A String that represents the file path.
	 * @param delimeter	A String that separates values in the given file.
	 * @param hasHeaderLine	A boolean that specifies if the first line of the given file, should not be loaded.
	 * @param number	An int that represents the placement of the timeSeries in the specified file.
	 * @return	A double array that represents the values of a timeSeries.
	 */
	public double[] load(String fileName, String delimeter, boolean hasHeaderLine, int number);
	
	/**
	 * Gets the specified values of a file as String ArrayList and transforms them to a double array.
	 * 
	 * @param tokens	A String ArrayList that contains the specified values.
	 * @return	A double array representation of the given data.
	 */
	public double[] constructObjects(ArrayList<String> tokens);
}
