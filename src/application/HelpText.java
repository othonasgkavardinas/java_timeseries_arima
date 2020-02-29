package application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that holds the contents of 'Help' window.
 *
 * @author Othonas Gkavardinas
 */
public class HelpText {
	private HashMap<String, String> choicesTexts;
	private ArrayList<String> userChoices;

	public HelpText() {
		userChoices = new ArrayList<String>();
		
		userChoices.add("File");
		userChoices.add("\tLoad data");
		userChoices.add("\tPlot TimeSeries");
		userChoices.add("\tLoad original");
		userChoices.add("\tClose");
		userChoices.add("Preprocessing");
		userChoices.add("\tPlot ACF");
		userChoices.add("\tLogarithm");
		userChoices.add("\tDifferencing");
		userChoices.add("\tSeasonal Adjustment");
		userChoices.add("\tExtra:Demean");
		userChoices.add("\tDone!");
		userChoices.add("Modeling");
		userChoices.add("\tSplit series");
		userChoices.add("\tPlot ACF(Modeling)");
		userChoices.add("\tPlot PACF");
		userChoices.add("\tNew ARIMA");
		userChoices.add("\tLoad ARIMA");
		userChoices.add("\tDelete ARIMA");
		userChoices.add("\tPlot series");
		userChoices.add("\tPlot with original");
		userChoices.add("\tPlot residuals");
		userChoices.add("\tPlot ACF of residuals");
		userChoices.add("Forecasting");
		userChoices.add("\tValidate");
		userChoices.add("\tForecast");
		userChoices.add("UI.Details");
		userChoices.add("UI.Model Status");
		userChoices.add("UI.Plots");
		userChoices.add("Log File");
		
		choicesTexts = new HashMap<String, String>();
		
		choicesTexts.put("File", "");
		choicesTexts.put("\tLoad data",
			"Choose a data format.\n\n"
			+ "1. For UCR files, select input file, "
			+ "delimeter that seperates the different"
			+ " values and the row in which the "
			+ "TimeSeries exist.\n\n"
			+ "2. For Column files, select input file, "
			+ "delimeter that seperates the different"
			+ " values, the column in which the "
			+ "TimeSeries exist and specify if there "
			+ "is a header file in the first line of "
			+ "file.");
		choicesTexts.put("\tPlot TimeSeries",
			"Plot TimeSeries in a new window.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tLoad original",
			"Load original TimeSeries, as loaded "
			+ "from the file.");	
		choicesTexts.put("\tClose",
			"Closes the application.");	
		
		choicesTexts.put("Preprocessing", "");
		choicesTexts.put("\tPlot ACF",
			"Input the higher lag value to specify how many"
			+ " lags should be shown in the ACF plot of"
			+ " the TimeSeries.\n\n"
			+ "This is a bar chart, where if there are any"
			+ " values higher than the upper and"
			+ " lower borders shown, the series is"
			+ " autocorrelated.\n\n"
			+ "Also, a special use of this plot is"
			+ " that if there is a repeating pattern"
			+ " on lag values, the series has seasonality.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tLogarithm",
			"Plot the logarithm of original "
			+ "TimeSeries to a new Window.\n\n"
			+ "You can change original TimeSeries to the transformed, by pressing 'Change timeSeries to this'.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tDifferencing",
			"Input the order of choice and "	
			+ "plot the difference of "
			+ "TimeSeries to a new Window.\n\n"
			+ "Usually, order 1 or 2 is enough to make a TimeSeries stationary.\n\n"
			+ "You can change original TimeSeries to the transformed, by pressing 'Change timeSeries to this'.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tSeasonal Adjustment",
			"Input the length of a seasonal period in the given TimeSeries "
			+ "and the seasonal width and "
			+ "plot the seasonality of TimeSeries and "
			+ "the seasonaly adjusted TimeSeries.\n\n"
			+ "You can change original TimeSeries to the transformed, by pressing 'Change timeSeries to this'.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tExtra:Demean",
			"Plot the TimeSeries, with its mean to zero.\n\n"	
			+ "You can change original TimeSeries to the transformed, by pressing 'Change timeSeries to this'.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tDone!",
			"Before pressing 'Done!' you should check "
			+ "if the TimeSeries appears stationary in "
			+ "its plot, and in the ACF plot.\n\n"
			+ "The timeSeries is stationary, if it"
			+ " has fixed mean and variance and no seasonality.\n\n"
			+ "After done is pressed, the preprocessing"
			+ " phase is over, and no changes can be made"
			+ " into the TimeSeries, unless "
			+ "'File->Load original' is pressed.");	

		choicesTexts.put("Modeling", "");
		choicesTexts.put("\tSplit series", 
			"Insert a number in (0, 100] to specify "
			+ "the percentage of the TimeSeries that should "
			+ "be stored in a training set, to be modeled. "
			+ "Usually an 80% of the TimeSeries is used. "
			+ "The data not selected for training, will be"
			+ " stored in a test set. This set is going to"
			+ " be used for validation.\n\n"
			+ "When split is pressed, any previously created"
			+ " models will be deleted.\n\n"
			+ "When you split a TimeSeries, 'Model Status' "
			+ "shows the selected Training Percentage.");
		choicesTexts.put("\tPlot ACF (Modeling)",
			"Input the higher lag value to specify how many"
			+ " lags should be shown in the ACF plot of"
			+ " the training set.\n\n"
			+ "This is a bar chart, where if "
			+ "the values drop after q lags"
			+ " MA(q) should be selected for the ARIMA model. "
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tPlot PACF",
			"Input the higher lag value to specify how many"
			+ " lags should be shown in the PACF plot of"
			+ " the training set.\n\n"
			+ "This is a bar chart, where if"
			+ " the values drop after p lags,"
			+ " AR(p) should be selected for the ARIMA model.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tNew ARIMA", 
			"Input orders p, d, q for AR(p) I(d), MA(q) "
			+ "to create a new ARIMA model. AR(p) describes"
			+ " autocorrelation, I(d) describes differencing "
			+ "and MA(q) describes moving average.\n\n"
			+ "The new model, will become current model "
			+ "and also will be stored in a model list"
			+ " to be loaded or deleted in the future.");	
		choicesTexts.put("\tLoad ARIMA",
			"Load an ARIMA model, created by this application."
			+ " When clicked, a list will appear, showing "
			+ "all models created for this TimeSeries, and "
			+ "their AIC value for comparison. The smaller the"
			+ " AIC, the better.\n\n"
			+ "If current TimeSeries is selected, "
			+ "nothing happens.\n\n"
			+ "When you load a model, 'Model Status' "
			+ "is showing the details of the model.");	
		choicesTexts.put("\tDelete ARIMA",
			"Delete an ARIMA model, created by this application."
			+ " When clicked, a list will appear, showing "
			+ "all models created for this TimeSeries, and"
			+ "their AIC value for comparison. The smaller the"
			+ " AIC, the better.\n\n"
			+ "If current TimeSeries is selected, "
			+ "nothing happens.");
		choicesTexts.put("\tPlot series",
			"Plots ARIMA fitted series.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tPlot with original",
			"Shows ARIMA fitted series and "
			+ "non fitted series, together on a plot.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tPlot residuals",
			"Plots the residuals of the current ARIMA model.\n\n"
			+ "Usually, if the residuals plot has zero "
			+ "mean and fixed variation the current "
			+ "model can be used for forecasting.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tPlot ACF of residuals",
			"Plots ACF of the residuals of the current ARIMA model.\n\n"
			+ "If there is no autocorrelation, the model fits well.");
		
		choicesTexts.put("Forecasting", "");
		choicesTexts.put("\tValidate",
			"Plots modeled data, testSet, and "
			+ "the upper and lower bounds of forecasting "
			+ "for as many values as the length of testSet.\n\n"
			+ "The validation process tests if every value "
			+ "of the test set is between the 95% of the "
			+ "upper and lower bounds. The result of this "
			+ "test appears in the title of the window "
			+ "that holds the new plot. If the forecast passes the test "
			+ "there is a (Pass) word, and otherwise a (Fail).\n\n"
			+ "The 95% is strict a strict bound, so, sometimes "
			+ "you can make assumptions for the forecast"
			+ " by just inspecting the plot.\n\n"	
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");
		choicesTexts.put("\tForecast",
			"Input how many values to be forecasted, by the current model.\n\n"	
			+ "When you press 'insert', a plot is shown, where "
			+ "there are the modeled data, the testSet, and "
			+ "the upper and lower bounds of forecasting.\n\n"
			+ "The forecast results are saved in a file "
			+ "inside 'forecasts' folder.\n\n"
			+ "You can save this plot as a PNG image by pressing 'save PNG'.");

		choicesTexts.put("UI.Details",
			"This column is showing some instructions, in the form of steps"
			+ ", that are needed for an ARIMA forecast process.\n\n"
			+ "These steps are dynamically shown as you use the application.");
		choicesTexts.put("UI.Model Status",
			"This bar is showing important details of the current model.\n\n"
			+ "If a model is not specified, this bar is empty.");	
		choicesTexts.put("UI.Plots", 
				"Plots are shown as new windows.\n\n"
				+ "These windows, are shown on the right bar of the application.\n\n"
				+ "You can close an open window, by pressing 'x' label near its name on the right bar.\n\n"
				+ "You can close all open windows, by pressing 'Close All' botton on the right bar of the application.\n\n"
				+ "Every window, uses a descrete sequence number at the end of it's title.");
		choicesTexts.put("Log File",
				"For every use of this application, a log file is created in the 'logs' folder.");
	}

	public ArrayList<String> getUserChoices() {
		return userChoices;
	}
	
	public String getDescription(String key) {
		return choicesTexts.get(key);
	}
}
