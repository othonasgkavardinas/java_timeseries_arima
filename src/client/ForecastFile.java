package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ForecastFile {

	public static void saveForecastInFile(double[] values) {
		String dateTime = String.valueOf(System.currentTimeMillis());
		File file = new File("forecasts/forecast" + dateTime);	
		
		try {
			if(!file.createNewFile())
				System.out.println("File already exists.");
			else {
				FileWriter fileWriter = new FileWriter(file, true);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				for(double value: values)
					printWriter.print(value + "\t");
				printWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
