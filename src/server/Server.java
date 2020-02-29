package server;

import com.github.servicenow.ds.stats.stl.SeasonalTrendLoess;
import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;

public class Server {
	/**
	 * Loads a TimeSeries from a UCR file. 
	 * 
	 * @param fileName	A String that contains full path of the file.
	 * @param delimeter	A String that separates the values of TimeSeries.
	 * @param lineNo	An int that specifies the line that should be read.
	 * @return	A TimeSeries that contains the values from the file.
	 */
	public TimeSeries loadUCR(String fileName, String delimeter, int lineNo) {
		RecordLoader recordLoader = RecordLoaderFactory.createRecordLoader("UCR");
		return TimeSeries.from(recordLoader.load(fileName, delimeter, false, lineNo));
	}

	/**
	 * Loads a TimeSeries from a Column file. 
	 * 
	 * @param fileName	A String that contains full path of the file.
	 * @param delimeter	A String that separates the values of TimeSeries.
	 * @param hasHeaderLine	A boolean that specifies if the first line should be read.
	 * @param column An int that specifies the column that should be read.
	 * @return	A TimeSeries that contains the values from the file.
	 */
	public TimeSeries loadColumn(String fileName, String delimeter, boolean hasHeaderLine, int column) {
		RecordLoader recordLoader = RecordLoaderFactory.createRecordLoader("Column");
		return TimeSeries.from(recordLoader.load(fileName, delimeter, hasHeaderLine, column));
	}

	/**
	 * Transforms TimeSeries with the logarithm transformation.
	 * 
	 * @param timeSeries	A TimeSeries to be transformed.
	 * @return	A TimeSeries transformed.
	 */
	public TimeSeries transformLogarithm(TimeSeries timeSeries) {
		return timeSeries.transform(0);
	}
	
	/**
	 * Transforms TimeSeries with the given order of differencing.
	 * 
	 * @param timeSeries	A TimeSeries to be transformed.
	 * @param orderOfDifference	An int that specifies the order of differencing.
	 * @return	A TimeSeries transformed.
	 */
	public TimeSeries transformDifference(TimeSeries timeSeries, int orderOfDifference) {
		return timeSeries.difference(orderOfDifference);
	}
	
	/**
	 * Extracts the seasonality from a TimeSeries.
	 * 
	 * @param timeSeries	A TimeSeries to be examined.
	 * @param periodLength	An int that shows the length of the time Period.
	 * @param seasonalWidth	An int that specifies the number of times seasonal component should appear on the result.
	 * @return	A TimeSeries that contains the seasonal component of the input TimeSeries, as many times as specified.
	 */
	public TimeSeries getSeasonality(TimeSeries timeSeries, int periodLength, int seasonalWidth) {
		double[] values = timeSeries.asArray();
		SeasonalTrendLoess.Builder builder = new SeasonalTrendLoess.Builder();
		SeasonalTrendLoess smoother = builder.
				setPeriodLength(periodLength).    	// Data has a period of 12
				setSeasonalWidth(seasonalWidth). 	// Monthly data smoothed over 35 years
				setNonRobust().         			// Not expecting outliers, so no robustness iterations
				buildSmoother(values);

		SeasonalTrendLoess.Decomposition stl = smoother.decompose();
		return TimeSeries.from(stl.getSeasonal());
	}
	
	/**
	 * The value-by-value subtraction between two TimeSeries. 
	 * 
	 * @param timeSeries1	A TimeSeries that is used as the first operand in subtraction.
	 * @param timeSeries2	A TimeSeries that is used as the second operand in subtraction.
	 * @return	A TimeSeries that is used as the result of the subtraction.
	 */
	public TimeSeries minus(TimeSeries timeSeries1, TimeSeries timeSeries2) {
		return timeSeries1.minus(timeSeries2);
	}

	/**
	 * Nullifies the mean of a given TimeSeries.
	 * 
	 * @param timeSeries	A TimeSeries to be transformed.
	 * @return	A TimeSeries transformed.
	 */
	public TimeSeries transformDemean(TimeSeries timeSeries) {
		return timeSeries.demean();
	}
	
	/**
	 * Converts a TimeSeries object to a double array.
	 * 
	 * @param timeSeries	A TimeSeries to be converted.
	 * @return	A double array that represents the values of the given timeSeries.
	 */
	public double[] convertToArray(TimeSeries timeSeries) {
		return timeSeries.asArray();
	}
	
	/**
	 * Calculates the size of a TimeSeries object. 
	 * 
	 * @param timeSeries	A TimeSeries to be measured.
	 * @return	An int that represents the size of the given timeSeries.
	 */
	public int size(TimeSeries timeSeries) {
		return timeSeries.size();
	}
	
	/**
	 * Calculates the autocorrelations up to the given lag. 
	 * 
	 * @param timeSeries	A TimeSeries to be measured.
	 * @param k	An int that represents the max autocorrelation value to be calculated.
	 * @return	A double array that holds all the autocorrelations up to the given lag in order.
	 */
	public double[] getAutoCorrelationUpToLag(TimeSeries timeSeries, int k) {
		return timeSeries.autoCorrelationUpToLag(k);
	}
	
	/**
	 * Splits a TimeSeries into a training set and a test set.
	 * 
	 * @param timeSeries	A TimeSeries to be split.
	 * @param percentage	A double that specifies the percentage of the original TimeSeries that should be contained in training set. 
	 * @return	A TimeSeries array containing training set and test set.
	 */
	public TimeSeries[] splitTimeSeries(TimeSeries timeSeries, double percentage) {
		int tsSize = timeSeries.size();
		int trainingSetSize = (int)(percentage*tsSize);
		TimeSeries trainingSet = timeSeries.slice(0, trainingSetSize-1);
		TimeSeries testSet = timeSeries.slice(trainingSetSize, tsSize-1);
		return new TimeSeries[] {trainingSet, testSet};
	}

	/**
	 * Creates an ARIMA Model. 
	 * 
	 * @param trainingSet	A TimeSeries that is going to be modeled.
	 * @param orders	An int array that contains the p d and q orders.
	 * @return	An Arima model.
	 */
	public Arima createArima(TimeSeries trainingSet, int[] orders) {
		return Arima.model(trainingSet, ArimaOrder.order(orders[0], orders[1], orders[2]));
	}
	
	/**
	 * Gets the fitted series of an ARIMA model.
	 * 
	 * @param currentModel	An Arima model to be examined.	
	 * @return	A TimeSeries that contains the fitted values.
	 */
	public TimeSeries getModelFittedSeries(Arima currentModel) {
		return currentModel.fittedSeries();
	}

	/**
	 * Gets the non-fitted series of an ARIMA model. 
	 * 
	 * @param currentModel	An Arima model to be examined.
	 * @return	A TimeSeries that contains the non-fitted values.
	 */
	public TimeSeries getModelNonFittedSeries(Arima currentModel) {
		return currentModel.observations();
	}
	
	/**
	 * Gets the residual series of an ARIMA model.
	 * 
	 * @param currentModel	An Arima model to be examined.
	 * @return	A TimeSeries that contains the residuals of the given model.
	 */
	public TimeSeries getModelResiduals(Arima currentModel) {
		return currentModel.predictionErrors();
	}

	/**
	 * Creates a new forecast for the given model.
	 * 
	 * @param currentModel	An Arima model to be examined.
	 * @param futureValues	An int that specifies how many future values should be predicted.
	 * @return	A TimeSeries array that contains the point estimates, upper interval and lower interval of the forecast.
	 */
	public TimeSeries[] createModelForecast(Arima currentModel, int futureValues) {
		Forecast forecast = currentModel.forecast(futureValues);
		return new TimeSeries[] { forecast.pointEstimates(), forecast.lowerPredictionInterval(), forecast.upperPredictionInterval() };
	}
	
	/**
	 * Validates a model by comparing test set and forecasted values.
	 * 
	 * @param testSet	A TimeSeries that contains the test set.
	 * @param forecastValues	A TimeSeries array that contains the point estimates, upper interval and lower interval of the forecast.
	 * @return	A boolean value that specifies if the validation was successful.
	 */
	public boolean validateModel(TimeSeries testSet, TimeSeries[] forecastValues) {
		//TimeSeries forecastPointEstimates = forecastValues[0];
		TimeSeries forecastLowerPredictionInterval = forecastValues[1];
		TimeSeries forecastUpperPredictionInterval = forecastValues[2];

		for(int i=0; i<testSet.size(); i++) {
			double wholePredictionArea =  forecastUpperPredictionInterval.at(i) - forecastLowerPredictionInterval.at(i);
			double fragmentOfPredictionArea = 0.05 * wholePredictionArea;
			if((forecastUpperPredictionInterval.at(i) - fragmentOfPredictionArea) < testSet.at(i))
				return false;
			if((forecastLowerPredictionInterval.at(i) + fragmentOfPredictionArea) > testSet.at(i))
				return false;
		}
		return true;
	}
	
	/**
	 * Akaike information criterion for an ARIMA model
	 *
	 * @param arima	An Arima model.
	 * @return	A double that represents aic of the given ARIMA model.
	 */
	public double getAic(Arima arima) {
		return arima.aic();
	}
	
	public double[][] formPartialsForPacf(double[] r) {
	// source:
	// https://github.com/XuMeng-NTU/CSC489/blob/master/Weka/src/weka/filters/timeseries/PACF.java	
	//
		//Using the Durban-Leverson
		int p = r.length;
		double[][] phi = new double[p][p];
		double numerator,denominator;
		phi[0][0]=r[0];

		for(int k=1;k<p;k++){
			//Find diagonal k,k
			//Naive implementation, should be able to do with running sums
			numerator=r[k];
			for(int i=0;i<k;i++)
				numerator-=phi[i][k-1]*r[k-1-i];
			denominator=1;
			for(int i=0;i<k;i++)
				denominator-=phi[k-1-i][k-1]*r[k-1-i];
			phi[k][k]=numerator/denominator;
			//Find terms 1,k to k-1,k
			for(int i=0;i<k;i++)
				phi[i][k]=phi[i][k-1]-phi[k][k]*phi[k-1-i][k-1];
		}
		return phi;
	}
}
