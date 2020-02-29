package client;


import java.util.HashMap;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.model.arima.Arima;


/**
 * This data class contains the data that represent a state of this application.
 *
 * @author Othonas Gkavardinas
 */
public class State {
	private TimeSeries timeSeries;
	private TimeSeries trainingSet;
	private TimeSeries testSet;
	private HashMap<String, Arima> models = new HashMap<String, Arima>();
	private Arima currentModel;
	private TimeSeries originalSeries;
	private String fileName;
	private LogFileManager logFileManager;
	
	public State() {
		logFileManager = new LogFileManager();
	}
	
	public void initLogFile() {
		this.logFileManager.initLog();
	}

	public void appendLog(String text) {
		this.logFileManager.appendLog(text);
	}
	
	public void setTimeSeries(TimeSeries timeSeries) {
		this.timeSeries = timeSeries;
	}

	public TimeSeries getTimeSeries() {
		return timeSeries;
	}

	public void setTrainingSet(TimeSeries trainingSet) {
		this.trainingSet = trainingSet;
	}

	public TimeSeries getTrainingSet() {
		return trainingSet;
	}

	public void setTestSet(TimeSeries testSet) {
		this.testSet = testSet;
	}

	public TimeSeries getTestSet() {
		return testSet;
	}

	public void setModels(HashMap<String, Arima> models) {
		this.models = models;
	}

	public HashMap<String, Arima> getModels() {
		return models;
	}

	public void setCurrentModel(Arima currentModel) {
		this.currentModel = currentModel;
	}

	public Arima getCurrentModel() {
		return currentModel;
	}

	public void setOriginalSeries(TimeSeries originalSeries) {
		this.originalSeries = originalSeries;
	}

	public TimeSeries getOriginalSeries() {
		return originalSeries;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public LogFileManager getLogFileManager() {
		return logFileManager;
	}
}
