package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * A class responsible for loading timeSeries objects from UCR files.
 *
 * @author Othonas Gkavardinas
 */
public class UCRLoader implements RecordLoader {

	public double[] load(String fileName, String delimeter, boolean hasHeaderLine, int lineNo) {

		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new FileInputStream(fileName));

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening file: " + fileName);
			System.exit(0);
		}

		String line = inputStream.nextLine();
		while (lineNo!=0) {
			line = inputStream.nextLine();
			lineNo--;
		}
		
		StringTokenizer tokenizer = new StringTokenizer(line, delimeter);

		ignoreClassificationLabel(tokenizer);

		ArrayList<String> tokens =  new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}	

		inputStream.close();

		return constructObjects(tokens);
	}

	private void ignoreClassificationLabel(StringTokenizer tokenizer) {
		tokenizer.nextToken();
	}
	
	public double[] constructObjects(ArrayList<String> tokens) {

		double doubleTokens[] = new double[tokens.size()];
		for(int i=0; i<tokens.size(); i++)
			doubleTokens[i] = Double.parseDouble(tokens.get(i));

		//return TimeSeries.from(doubleTokens);
		return doubleTokens;
	}
}
