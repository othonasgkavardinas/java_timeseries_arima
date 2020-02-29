package application;

/**
 * Static fields containing the contents of 'Details' column.
 *
 * @author Othonas Gkavardinas
 */
public class Details {
	
	public static String STEP1 = 
			  "1. Autocorrelation\n"
			+ "Examine the ACF plot for any large autocorrelations across different lags.\n\n"

			+ "2. Stationarity\n"
			+ "Logarithm: Stabilizes variance.\n\n"
			+ "Differencing: Removes trends by stabilizing mean.\n\n"
			+ "Seasonal Adjustment: Removes seasonality.\n\n"
			+ "ADF test, describes if a larger order of differencing is needed.";

	public static String STEP2 = 
			  "3. Split Series\n"
			+ "Split TimeSeries in two sets\n\n"
			+ "Training Set: Set to be modeled\n\n"
			+ "Test Set: Set to be used for validation.";

	public static String STEP3 = 
			  "4. ARIMA\n"
			+ "Models consist of three parameters\n\n"
			+ "AutoRegressive(p): Steadly decaying ACF, and PACF drops after p lags.\n\n"
			+ "Differencing(d): For stationarity.\n\n"
			+ "MovingAverage(q): Steadly decaying PACF, and ACF drops after q lags.\n\n"

			+ "5. Validation\n"
			+ "Examine if residuals plot appears as random noise.\n\n"

			+ "6. Best Fitted\n"
			+ "Model with the smallest AIC.";
	
	public static String STEP4 = 
			  "7. Forecast Validation\n"
			+ "Check if Test Set values are located within 95% of confidence limits of the forecast.\n\n"

			+ "8. Forecasting\n"
			+ "Create forecasts for future values.";

}
