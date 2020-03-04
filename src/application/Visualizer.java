package application;


import java.util.Arrays;

import com.github.signaflo.timeseries.TimeSeries;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import server.Server;


/**
 * A class responsible for the plots of the application.
 *
 * @author Othonas Gkavardinas
 */
public class Visualizer {
	
	private static Server server = new Server();

	/**
	 * Plots a line chart of a timeSeries.
	 * 
	 * @param timeSeries	A TimeSeries to be plotted.
	 * @return	A LineChart created by the given timeSeries.
	 */
	public static LineChart<Number, Number> plot(final TimeSeries timeSeries) {
		return plot(timeSeries, "Time Series Plot");
	}
	    
	/**
	 * Plots a line chart of a timeSeries with a specific title.
	 * 
	 * @param timeSeries	A TimeSeires to be plotted.	
	 * @param title	A String that represents the title of the chart.
	 * @return	A LineChart created by the given timeSeries.
	 */
	public static LineChart<Number, Number> plot(final TimeSeries timeSeries, final String title) {
		try {
			final NumberAxis xAxis = new NumberAxis();
			final NumberAxis yAxis = new NumberAxis();

			xAxis.setLabel("Time");
			yAxis.setLabel("Values");
			
			final LineChart<Number,Number> lineChart = 
                new LineChart<Number, Number>(xAxis,yAxis);
			
	        double[] values = server.convertToArray(timeSeries);

			lineChart.setTitle(title);
			lineChart.setLegendVisible(false);
			lineChart.setCreateSymbols(false);
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

	        for(int i=0; i<server.size(timeSeries); i++)
	        	series.getData().add(new XYChart.Data<Number, Number>(i, values[i]));
	        
			lineChart.getData().add(series);
			
			return lineChart;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Plots a timeSeries with the points, the upper and lower bounds of a forecast.
	 * 
	 * @param timeSeriesList	A TimeSeries array that contains the timeSeries and the upper and lower bounds of a forecast.
	 * @param title	A String that represents the title of the chart.
	 * @param legendLabel	A String that represents the label of the legend of the chart.
	 * @return	A LineChart that represents the given timeSeries along with its forecast.
	 */
	public static LineChart<Number, Number> plotWithForecast(TimeSeries[] timeSeriesList, final String title
																				, final String legendLabel) {
		try {
			String[] legendLabels = { "Training Set", legendLabel, "Upper Interval", "Lower Interval" };
			
			final NumberAxis xAxis = new NumberAxis();
			final NumberAxis yAxis = new NumberAxis();

			xAxis.setLabel("Time");
			yAxis.setLabel("Values");
			
			final LineChart<Number,Number> lineChart = 
                new LineChart<Number, Number>(xAxis,yAxis);
			
			double lastTrainingSetValue = 0;
			
			for(int i=0; i<timeSeriesList.length; i++){
				double[] values = server.convertToArray(timeSeriesList[i]);

				XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

				if(i==0) {
					lastTrainingSetValue = values[server.size(timeSeriesList[i])-1];
					for(int j=0; j<server.size(timeSeriesList[i]); j++)
						series.getData().add(new XYChart.Data<Number, Number>(j, values[j]));
				}
				else {
					series.getData().add(new XYChart.Data<Number, Number>(server.size(timeSeriesList[0])-1, lastTrainingSetValue));
					for(int j=0; j<server.size(timeSeriesList[i]); j++)
						series.getData().add(new XYChart.Data<Number, Number>(j+server.size(timeSeriesList[0]), values[j]));
				}
					
				series.setName(legendLabels[i]);
				lineChart.getData().add(series);
			}
			
			lineChart.setTitle(title);
			lineChart.setLegendVisible(true);
			lineChart.setCreateSymbols(false);

			return lineChart;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Plots a timeSeries with the points, the upper and lower bounds of a forecast.
	 * 
	 * @param timeSeriesList	A TimeSeries array that contains the timeSeries and the upper and lower bounds of a forecast.
	 * @param title	A String that represents the title of the chart.
	 * @param legendLabel	A String that represents the label of the legend of the chart.
	 * @return	A LineChart that represents the given timeSeries along with its forecast.
	 */
	
	/**
	 * Plots the modeled data of an ARIMA model along with unmodeled data.
	 * 
	 * @param trainingSet	A TimeSeries that represents the unmodeled data.
	 * @param modeled	A TimeSeries that represents the modeled data.
	 * @param title	A String that represents the title of the chart.
	 * @return	A LineChart that contains modeled and umodeled data.
	 */
	public static LineChart<Number, Number> plotUnmodeledAndModeled(final TimeSeries trainingSet, final TimeSeries modeled,
																				final String title) {
		try {
			String[] legendLabels = { "Training Set", "Modeled Series" };
			
			final NumberAxis xAxis = new NumberAxis();
			final NumberAxis yAxis = new NumberAxis();

			xAxis.setLabel("Time");
			yAxis.setLabel("Values");
			
			final LineChart<Number,Number> lineChart = 
                new LineChart<Number, Number>(xAxis,yAxis);
			
			TimeSeries[] timeSeriesList = new TimeSeries[] { trainingSet, modeled };
			
			for(int i=0; i<timeSeriesList.length; i++){
				double[] values = server.convertToArray(timeSeriesList[i]);

				XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

				for(int j=0; j<server.size(timeSeriesList[i]); j++)
					series.getData().add(new XYChart.Data<Number, Number>(j, values[j]));
					
				series.setName(legendLabels[i]);
				lineChart.getData().add(series);
			}
			
			lineChart.setTitle(title);
			lineChart.setLegendVisible(true);
			lineChart.setCreateSymbols(false);

			return lineChart;

		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Plots the ACF of a timeSeries.
	 * 
	 * @param timeSeries	A TimeSeries, that is used to create the ACF plot.
	 * @param k	An int that represents the maximum lag value of the ACF plot.
	 * @param title	A String that represents the title of the plot.
	 * @return	A Parent that contains a custom chart, that represents the ACF of the given timeSeries.
	 */
	@SuppressWarnings("unchecked")
	public static Parent plotAcf(TimeSeries timeSeries, final int k, String title) {
		double minVal = Double.MAX_VALUE;
		double maxVal = 0;
		
		final double[] acf = server.getAutoCorrelationUpToLag(timeSeries, k);
	    final String[] lags = new String[k + 1];
	    for (int i=0; i<lags.length; i++) {
	    	lags[i] = String.valueOf(i);

	    	if(acf[i] > maxVal)
	    		maxVal = acf[i];
	    	if(acf[i] < minVal)
	    		minVal = acf[i];
	    }
	    final double upper = (-1 / server.size(timeSeries)) + (2 / Math.sqrt(server.size(timeSeries)));
	    final double lower = (-1 / server.size(timeSeries)) - (2 / Math.sqrt(server.size(timeSeries)));
	    final double[] upperLine = new double[lags.length];
	    final double[] lowerLine = new double[lags.length];
	    for (int i = 0; i < lags.length; i++)
	    	upperLine[i] = upper;
	    for (int i = 0; i < lags.length; i++)
	    	lowerLine[i] = lower;

	    maxVal = maxVal + maxVal*0.5;
	    minVal = minVal + minVal*0.5;

	    if(maxVal < 0)
	    	maxVal = upper;
	    if(minVal > 0)
	    	minVal = lower;
	    
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(minVal);
		yAxis.setUpperBound(maxVal);
		yAxis.setTickUnit((maxVal-minVal)/20);
		xAxis.setLabel("Lag");
		yAxis.setLabel("Autocorrelation");
	   
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		for(int i=0; i<acf.length; i++)
			series.getData().add(new XYChart.Data<String, Number>(lags[i], acf[i])); 

		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		for(int i=0; i<acf.length; i++)
			series2.getData().add(new XYChart.Data<String, Number>(lags[i], upperLine[i])); 

		XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
		for(int i=0; i<acf.length; i++)
			series3.getData().add(new XYChart.Data<String, Number>(lags[i], lowerLine[i])); 

		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);  
		barChart.setTitle(title);
		barChart.setLegendVisible(false);
        barChart.setAnimated(false);
		barChart.getData().add(series);

		for(Node n: barChart.lookupAll(".chart-bar"))
            n.setStyle("-fx-bar-fill: #85a5ea;");

	    final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(title);
	    lineChart.setLegendVisible(false);
	    lineChart.setAnimated(false);
	    lineChart.setCreateSymbols(false);
	    lineChart.setAlternativeRowFillVisible(false);
	    lineChart.setAlternativeColumnFillVisible(false);
	    lineChart.setHorizontalGridLinesVisible(false);
	    lineChart.setVerticalGridLinesVisible(false);
	    lineChart.getXAxis().setVisible(false);
	    lineChart.getYAxis().setVisible(false);
		lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
	    lineChart.getData().addAll(series2, series3);
	    
	    StackPane root = new StackPane();
	    root.getChildren().addAll(barChart, lineChart);

	    return root;
	}
	
	/**
	 * Plots the PACF of a timeSeries.
	 * 
	 * @param timeSeries	A TimeSeries, that is used to create the PACF plot.
	 * @param k	An int that represents the maximum lag value of the PACF plot.
	 * @param title	A String that represents the title of the plot.
	 * @return	A Parent that contains a custom chart, that represents the PACF of the given timeSeries.
	 */
	@SuppressWarnings("unchecked")
	public static Parent plotPacf(TimeSeries timeSeries, final int k, String title) {
		double minVal = Double.MAX_VALUE;
		double maxVal = 0;
		
		final double[] acf = server.getAutoCorrelationUpToLag(timeSeries, k);
		double[] pacf = new double[k];

		pacf[0] = acf[0];

		for(int i=1; i<k; i++)
			pacf[i] = server.formPartialsForPacf(Arrays.copyOfRange(acf, 1, k+1))[i][i];
		
	    final String[] lags = new String[k];
	    for(int i=0; i<lags.length; i++) {
	    	lags[i] = String.valueOf(i+1);

	    	if(pacf[i] > maxVal)
	    		maxVal = pacf[i];
	    	if(pacf[i] < minVal)
	    		minVal = pacf[i];
	    }
	    final double upper = (-1 / server.size(timeSeries)) + (2 / Math.sqrt(server.size(timeSeries)));
	    final double lower = (-1 / server.size(timeSeries)) - (2 / Math.sqrt(server.size(timeSeries)));
	    final double[] upperLine = new double[lags.length];
	    final double[] lowerLine = new double[lags.length];
	    for (int i = 0; i < lags.length; i++)
	    	upperLine[i] = upper;
	    for (int i = 0; i < lags.length; i++)
	    	lowerLine[i] = lower;

	    maxVal = maxVal + maxVal*0.5;
	    minVal = minVal + minVal*0.5;
	    
	    if(maxVal < 0)
	    	maxVal = upper;
	    if(minVal > 0)
	    	minVal = lower;
	    
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(minVal);
		yAxis.setUpperBound(maxVal);
		yAxis.setTickUnit((maxVal-minVal)/20);
		xAxis.setLabel("Lag");
		yAxis.setLabel("Autocorrelation");
			
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		for(int i=0; i<pacf.length; i++)
			series.getData().add(new XYChart.Data<String, Number>(lags[i], pacf[i])); 

		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		for(int i=0; i<pacf.length; i++)
			series2.getData().add(new XYChart.Data<String, Number>(lags[i], upperLine[i])); 

		XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
		for(int i=0; i<pacf.length; i++)
			series3.getData().add(new XYChart.Data<String, Number>(lags[i], lowerLine[i])); 

		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);  
		barChart.setTitle(title);
		barChart.getData().add(series);
		barChart.setLegendVisible(false);
        barChart.setAnimated(false);

		for(Node n: barChart.lookupAll(".chart-bar"))
            n.setStyle("-fx-bar-fill: #85a5ea;");

	    final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle(title);
	    lineChart.setLegendVisible(false);
	    lineChart.setAnimated(false);
	    lineChart.setCreateSymbols(false);
	    lineChart.setAlternativeRowFillVisible(false);
	    lineChart.setAlternativeColumnFillVisible(false);
	    lineChart.setHorizontalGridLinesVisible(false);
	    lineChart.setVerticalGridLinesVisible(false);
	    lineChart.getXAxis().setVisible(false);
	    lineChart.getYAxis().setVisible(false);
		lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
	    lineChart.getData().addAll(series2, series3);
	    
	    StackPane root = new StackPane();
	    root.getChildren().addAll(barChart, lineChart);

	    return root;
	}
}
