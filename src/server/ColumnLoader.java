package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A class responsible for loading timeSeries objects from Column files.
 *
 * @author Othonas Gkavardinas
 */
public class ColumnLoader implements RecordLoader {

	public double[] load(String fileName, String delimeter, boolean hasHeaderLine, int column) {
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Problem opening file: " + fileName);
			System.exit(0);
		}

		if(hasHeaderLine)
			inputStream.nextLine();

		String line = "";

		ArrayList<String> tokens =  new ArrayList<String>();
		
		String[] toks;
		while (inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			toks = line.split(delimeter);
			tokens.add(toks[column]);
		}		


		inputStream.close();
		return constructObjects(tokens);
	}
	
	public double[] constructObjects(ArrayList<String> tokens) {
		double[] doubleTokens = new double[tokens.size()];
		for(int i=0; i<tokens.size(); i++)
			doubleTokens[i] = Double.parseDouble(tokens.get(i));
		
		return doubleTokens;
	}
}
