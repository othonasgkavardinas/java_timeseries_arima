package application;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

import com.github.signaflo.timeseries.TimeSeries;

import client.ForecastFile;
import client.RecordInput;
import client.RecordInputFactory;
import client.State;
import server.Server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * A controller that contains the listeners for the JavaFX application.
 *
 * @author Othonas Gkavardinas
 */
public class MainController {

	@FXML private LineChart<Number, Number> lineChart;
	@FXML private NumberAxis xAxis;
	@FXML private NumberAxis yAxis;
	@FXML private ListView<TextFlow> listOfWindows;
	@FXML private Label currentModelStatus0;
	@FXML private Label currentModelStatus1;
	@FXML private Label currentModelStatus2;
	@FXML private Label currentModelStatus3;
	@FXML private Label details;
	@FXML private Menu preprocessingMenu;
	@FXML private Menu modelingMenu;
	@FXML private Menu forecastingMenu;
	@FXML private MenuItem originalSeriesMenuItem;
	@FXML private MenuItem plotTimeSeriesMenuItem;
	
	private State state = new State();
	private Server server = new Server();
	
	private ArrayList<Stage> listOfStages = new ArrayList<Stage>();
	private Stage userInputWindow;
	private int chartNo = 1;
	private boolean splitHappened;	

	private boolean isInvalidFile;
	private String fileName;
	
	/**
	 * Listener. Loads a file.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void openFile(ActionEvent event) {
		ToggleGroup toggleGroup = new ToggleGroup();
		RadioButton rb1 = new RadioButton("UCR file");
		RadioButton rb2 = new RadioButton("Column file");
		rb1.setToggleGroup(toggleGroup);
		rb2.setToggleGroup(toggleGroup);
		getUserInputWindow("File type", new String[] {"", ""}
			, new Control[] {rb1, rb2}, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if(rb1.isSelected()) {
						String[] labelArray = new String[4];
						Control[] controlArray = new Control[4];

						isInvalidFile = true;
						fileName = "";
						while(isInvalidFile) {
							try {
								RecordInput recordInput = RecordInputFactory.createRecordInput("UCR");
								recordInput.initializeForm(labelArray, controlArray);
								if(fileName.equals(""))
									isInvalidFile = false;
								getUserInputWindow("UCR loader", labelArray
										, controlArray, new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										fileName = ((TextField)controlArray[1]).getText();
										@SuppressWarnings("unchecked")
										String delimeterChoice = ((ComboBox<String>)controlArray[2]).getSelectionModel().getSelectedItem();
										String delimeter = "";
										if(delimeterChoice.equals("\\t"))
											delimeter = "\t";

										int lineNo = Integer.parseInt(((TextField)controlArray[3]).getText());

										state.setTimeSeries(server.loadUCR(fileName, delimeter, lineNo));

										isInvalidFile = false;
										state.setOriginalSeries(state.getTimeSeries());
										getMainPlot();
										originalSeriesMenuItem.setDisable(false);
										plotTimeSeriesMenuItem.setDisable(false);
										details.setText(Details.STEP1);
										state.initLogFile();
										state.appendLog("Load file: " + fileName);
										clearPreviousData();

										userInputWindow.close();
									}
								}, "Load", true);
							}
							catch(Exception e1) {
								if(fileName.equals(""))
									isInvalidFile = false;
							}
						}
					}
					else if(rb2.isSelected()) {
						String[] labelArray = new String[5];
						Control[] controlArray = new Control[5];

						isInvalidFile = true;
						fileName = "";
						while(isInvalidFile) {
							try {
								RecordInput recordInput = RecordInputFactory.createRecordInput("Column");
								recordInput.initializeForm(labelArray, controlArray);
								if(fileName.equals(""))
									isInvalidFile = false;
								getUserInputWindow("Column loader", labelArray
										, controlArray, new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										fileName = ((TextField)controlArray[1]).getText();
										@SuppressWarnings("unchecked")
										String delimeterChoice = ((ComboBox<String>)controlArray[2]).getSelectionModel().getSelectedItem();
										String delimeter = "";
										if (delimeterChoice.equals("\\t"))
											delimeter = "\t";
										else
											delimeter = delimeterChoice;

										int column = Integer.parseInt(((TextField)controlArray[3]).getText());

										boolean hasHeaderLine = ((CheckBox)controlArray[4]).isSelected();
										
										state.setTimeSeries(server.loadColumn(fileName, delimeter, hasHeaderLine, column));

										isInvalidFile = false;
										state.setOriginalSeries(state.getTimeSeries());
										getMainPlot();
										originalSeriesMenuItem.setDisable(false);
										plotTimeSeriesMenuItem.setDisable(false);
										details.setText(Details.STEP1);
										state.initLogFile();
										state.appendLog("Load file: " + fileName);
										clearPreviousData();

										userInputWindow.close();
									}
								}, "Load", true);
							}
							catch(Exception e1) {
								if(fileName.equals(""))
									isInvalidFile = false;
							}
						}
					}
					else if(rb2.isSelected()) {
						System.out.println("2");
					}
				}
		}, "Next", false);
	}
	
	/**
	 * Shows the plot of the main window.
	 * 
	 */
	private void getMainPlot() {
		
		xAxis.setLabel("Time");
		yAxis.setLabel("Values");

	    double[] values = server.convertToArray(state.getTimeSeries());

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

	    for(int i=0; i<server.size(state.getTimeSeries()); i++)
	    	series.getData().add(new XYChart.Data<Number, Number>(i, values[i]));

	    lineChart.getData().clear();
		lineChart.getData().add(series);
		lineChart.setCreateSymbols(false);
	    lineChart.setAnimated(false);
	    lineChart.setLegendVisible(false);
		lineChart.setVisible(true);
	}
		
	/**
	 * Clears all data, and returns the timeSeries to original.
	 * 
	 */
	private void clearPreviousData() {
		details.setText(Details.STEP1);
		splitHappened = false;
		preprocessingMenu.setDisable(false);
		modelingMenu.setDisable(true);
        disableNewArimaUntilSplit(true);
		clearModels();
	}

	/**
	 * Clears all model data.
	 * 
	 */
	private void clearModels() {
		disableItemsUntilCurrentModel(true);
		forecastingMenu.setDisable(true);
		state.setCurrentModel(null);
		currentModelStatus0.setText("");
		currentModelStatus1.setText("");
		currentModelStatus2.setText("");
		currentModelStatus3.setText("");
		state.getModels().clear();
	}

	/**
	 * Disables the 1,2,3 options of 'Modeling' menu.
	 * 
	 * @param disable	A boolean to choose if disable or enable.
	 */
	private void disableNewArimaUntilSplit(boolean disable) {
		for(int i=1; i<4; i++)
			modelingMenu.getItems().get(i).setDisable(disable);
	}

	/**
	 * Disables the 4,5,6,7,8,9 options of 'Modeling' menu. 
	 * 
	 * @param disable	A boolean to choose if disable or enable.
	 */
	private void disableItemsUntilCurrentModel(boolean disable) {
		for(int i=4; i<modelingMenu.getItems().size(); i++)
			modelingMenu.getItems().get(i).setDisable(disable);
	}
	
	/**
	 * Listener. Changes the current timeSeries the loaded one.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void loadOriginalTs(ActionEvent event) {
		state.setTimeSeries(state.getOriginalSeries());
		getMainPlot();
		
		state.appendLog("Load original TimeSeries");
		
		clearPreviousData();
	}

	/**
	 * Listener. Closes the application and every window it opens.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void closeApp(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Listener. Plots the current timeSeries.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plot(ActionEvent event) {
		plotAction(state.getTimeSeries(), "TimeSeries - Plot");
	}


	/**
	 * Listener. Plots a specified ACF of the current timeSeries.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plotAcf(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("ACF plot", new String[] {"Lag:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int lag = Integer.parseInt(textField.getText());
                	try {
                		Parent stackPane = Visualizer.plotAcf(state.getTimeSeries(), lag,
                				"ACF ("+lag+") - Plot "+(chartNo++));
                		createStage(stackPane, null);
                		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                	} catch(Exception e) {
                		e.printStackTrace();
                	}
                }
            }
        }, "Insert", false);
	}
	
	/**
	 * Listener. Plots the logarithm of the current timeSeries.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plotLogarithm(ActionEvent event) {
		plotAction(server.transformLogarithm(state.getTimeSeries()), "Logarithm - Plot");
	}
	
	/**
	 * Listener. Plots a specified difference of the current timeSeries.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plotDiff(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("Differencing", new String[] {"Order:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int orderOfDifference = Integer.parseInt(textField.getText());
                	plotAction(server.transformDifference(state.getTimeSeries(), orderOfDifference),
                			"Differencing (" + orderOfDifference + ") - Plot");
                	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                }
            }
        }, "Insert", false);
	}

	/**
	 * Listener. Plots a specified seasonality of the current timeSeries, and the current timeSeries minus seasonality.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plotSeasonal(ActionEvent event) {
		TextField textField1 = new TextField();
		TextField textField2 = new TextField();
		getUserInputWindow("Seasonal adjustment", new String[] {"Period length", "Seasonal width"}
						, new Control[] {textField1, textField2}, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(isPositiveInteger(textField1.getText()) && isPositiveInteger(textField2.getText())) {
                	int periodLength = Integer.parseInt(textField1.getText());
                	int seasonalWidth = Integer.parseInt(textField2.getText());

					TimeSeries seasonality = server.getSeasonality(state.getTimeSeries(), periodLength, seasonalWidth);
					plotAction(seasonality, "Seasonality - Plot");	
					plotAction(server.minus(state.getTimeSeries(), seasonality), "TimeSeries without seasonality - Plot");
					
					((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
				}	
			}
		}, "Insert", false);
	}

	/**
	 * Listener. Plots the current timeSeries mean-adjusted.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void ts_plotDemean(ActionEvent event) {
		plotAction(server.transformDemean(state.getTimeSeries()), "Demean - Plot");
	}

	/**
	 * Listener. Specifies that the preprocessing phase is done.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void done_preprocessing(ActionEvent event) {
		details.setText(Details.STEP2);

		state.appendLog("Done preprocessing!");

		preprocessingMenu.setDisable(true);
		modelingMenu.setDisable(false);
	}

	/**
	 * Changes the labels of 'Details' column to Step3.
	 * 
	 */
	public void setStep3OnModelingMenuShowing() {
		if(splitHappened)
			details.setText(Details.STEP3);
	}

	
	/**
	 * Listener. Splits the current timeSeries to a training and a test set.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_splitTs(ActionEvent event) {
		TextField textField = new TextField();
		textField.setText("80");
		getUserInputWindow("Split series", new String[] {"Training set\n percentage:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	double percentage = (Integer.parseInt(textField.getText()))/100.0;
                	TimeSeries[] sets = server.splitTimeSeries(state.getTimeSeries(), percentage);
                	state.setTrainingSet(sets[0]);
                	state.setTestSet(sets[1]);
                	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                
                	details.setText(Details.STEP3);
                	splitHappened = true;
                	state.appendLog("Split series " + (int)(percentage*100) + "%");
                	disableNewArimaUntilSplit(false);
                	clearModels();
                	currentModelStatus0.setText("Training " + textField.getText() + "%");
                }
            }
        }, "Insert", false);
	}

	/**
	 * Listener. Plots the ACF of training set.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void trainingSet_plotAcf(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("ACF plot", new String[] {"Lag:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int lag = Integer.parseInt(textField.getText());
                	try {
                		Parent stackPane = Visualizer.plotAcf(state.getTrainingSet(), lag,
                				"ACF ("+lag+") - Plot "+(chartNo++));
                		createStage(stackPane, null);
                		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                	} catch(Exception e) {
                		e.printStackTrace();
                	}
                }
            }
        }, "Insert", false);
	}
	
	/**
	 * Listener. Plots the PACF of training set.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void trainingSet_plotPacf(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("PACF plot", new String[] {"Lag:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int lag = Integer.parseInt(textField.getText());
                	try {
                		Parent stackPane = Visualizer.plotPacf(state.getTrainingSet(), lag,
                				"PACF ("+lag+") - Plot "+(chartNo++));
                		createStage(stackPane, null);
                		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                	} catch(Exception e) {
                		e.printStackTrace();
                	}
                }
            }
        }, "Insert", false);
	}

	/**
	 * Listener. Creates an ARIMA model, and sets it to current model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_createArima(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("New ARIMA model", new String[] {"Orders (p, d, q):"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	int[] orders = new int[3];
                if((orders = getArimaOrders(textField.getText())) != null) {
                	state.setCurrentModel(server.createArima(state.getTrainingSet(), orders));
                	state.getModels().put("ARIMA ("+orders[0]+","+orders[1]+","+orders[2]+")", state.getCurrentModel());
                	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                	displayModelStatus();

                	disableItemsUntilCurrentModel(false);
                	
                	state.appendLog("New ARIMA model (" + orders[0] + "," + orders[1] + "," + orders[2] + ")");
                	forecastingMenu.setDisable(false);
                }
                else
                	System.out.println("Failure");
            }
        }, "Create", false);
	}
	
	/**
	 * Extracts the three ARIMA orders as integers from a String.
	 * 
	 * @param input	A string to be extracted.
	 */
	private int[] getArimaOrders(String input) {
		int[] orders = new int[3];
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(input);
		for(int i=0; i<3; i++) {
			if(m.find())
				orders[i] = Integer.parseInt(m.group());
			else
				return null;
		}
		return orders;
	}
	
	/**
	 * Listener. Loads an ARIMA model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_loadArima(ActionEvent event) {
		ListView<String> listView = new ListView<String>();
		ArrayList<String> items = new ArrayList<String>();
		for(String key: state.getModels().keySet())
			items.add(key + "\tAIC: " + ((int)server.getAic(state.getModels().get(key)))
					+ ((state.getModels().get(key)==state.getCurrentModel()) ? "\t(current)" : ""));
		ObservableList<String> data = FXCollections.observableArrayList(items);
        listView.setItems(data);		

		getUserInputWindow("Load ARIMA model", new String[] {""}, new Control[] {listView}, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!(listView.getSelectionModel().getSelectedItem() == null)) {
					int offset = listView.getSelectionModel().getSelectedItem().indexOf(")");
					String searchString = listView.getSelectionModel().getSelectedItem().substring(0, offset+1);
					state.setCurrentModel(state.getModels().get(searchString));
					displayModelStatus();
					
					state.appendLog("Load model: " + listView.getSelectionModel().getSelectedItem());
				}	
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
			}
		}, "Load", false);
	}

	/**
	 * Displays the status of the current model in the bottom bar.
	 * 
	 */
	private void displayModelStatus() {
		int endOfArima = state.getCurrentModel().order().toString().indexOf(")") + 1;
		currentModelStatus1.setText(state.getCurrentModel().order().toString().substring(0, endOfArima));
		currentModelStatus2.setText("Variance: " + ((int)state.getCurrentModel().sigma2()) +
				"\nLog Likelihood: " + ((int)state.getCurrentModel().logLikelihood()) +
				"\nAIC: " + ((int)server.getAic(state.getCurrentModel())));
		currentModelStatus3.setText(state.getCurrentModel().coefficients().toString());
	}
	
	/**
	 * Listener. Deletes an ARIMA model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_deleteArima(ActionEvent event) {
		ListView<String> listView = new ListView<String>();
		ArrayList<String> items = new ArrayList<String>();
		for(String key: state.getModels().keySet())
			items.add(key + "\tAIC: " + ((int)server.getAic(state.getModels().get(key)))
					+ ((state.getModels().get(key)==state.getCurrentModel()) ? "\t(current)" : ""));
		ObservableList<String> data = FXCollections.observableArrayList(items);
        listView.setItems(data);		

		getUserInputWindow("Delete ARIMA model", new String[] {""}, new Control[] {listView}, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int offset = listView.getSelectionModel().getSelectedItem().indexOf(")");
				String searchString = listView.getSelectionModel().getSelectedItem().substring(0, offset+1);
				if(state.getModels().get(searchString) != state.getCurrentModel()) {
					state.getModels().remove(searchString);
					state.appendLog("Delete model: " + listView.getSelectionModel().getSelectedItem());
				}
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
			}
		}, "Delete", false);
	}

	/**
	 * Listener. Plots the fitted data of the current model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_plot(ActionEvent event) {
		plotAction(server.getModelFittedSeries(state.getCurrentModel()), "Model Fitted TimeSeries - Plot");
	}

	/**
	 * Listener. Plots the fitted data of the current model, along with the non-fitted data.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_plotWithOriginal(ActionEvent event) {
		LineChart<Number, Number> lineChart = Visualizer.plotUnmodeledAndModeled(server.getModelNonFittedSeries(state.getCurrentModel())
													, server.getModelFittedSeries(state.getCurrentModel())
													, "Modeled vs unmodeled - Plot " + (chartNo++)); 
		createStage(lineChart, null);
		
	}
	
	/**
	 * Listener. Plots the the residuals of the current model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_plotResiduals(ActionEvent event) {
		plotAction(server.getModelResiduals(state.getCurrentModel()), "Model residuals - Plot");
	}
	
	/**
	 * Listener. Plots the the ACF of the residuals of the current model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void model_plotAcfOfResiduals(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("ACF of residuals plot", new String[] {"Lag:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int lag = Integer.parseInt(textField.getText());
                	try {
                		Parent stackPane = Visualizer.plotAcf(server.getModelResiduals(state.getCurrentModel()), lag,
                				"ACF ("+lag+") of residuals - Plot "+(chartNo++));
                		createStage(stackPane, null);
                		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                	} catch(Exception e) {
                		e.printStackTrace();
                	}
                }
            }
        }, "Insert", false);
	}
	
	/**
	 * Changes the labels of 'Details' column to Step4.
	 * 
	 */
	public void setStep4OnForecastingMenuShowing() {
		details.setText(Details.STEP4);
	}

	/**
	 * Listener. Validates the current model, by forecasting test set.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void validate(ActionEvent event) {

		TimeSeries[] forecastValues = server.createModelForecast(state.getCurrentModel(), server.size(state.getTestSet()));
		//TimeSeries forecastPointEstimates = forecastValues[0];
		TimeSeries forecastLowerPredictionInterval = forecastValues[1];
		TimeSeries forecastUpperPredictionInterval = forecastValues[2];
		
		boolean pass = server.validateModel(state.getTestSet(), forecastValues);

		TimeSeries[] timeSeriesList = { state.getTrainingSet(), state.getTestSet()
											, forecastUpperPredictionInterval, forecastLowerPredictionInterval };
		LineChart<Number, Number> lineChart = Visualizer.plotWithForecast(timeSeriesList, "Model validation ("
									+ ((pass)?"Pass":"Fail") + ") - Plot " + (chartNo++), "Test Set");
		createStage(lineChart, null);
	}
	
	/**
	 * Listener. Forecasts a given number of values, using the current model.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void forecast(ActionEvent event) {
		TextField textField = new TextField();
		getUserInputWindow("New forecast", new String[] {"Number of values:"}, new Control[] {textField}, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isPositiveInteger(textField.getText())) {
                	int noOfValues = Integer.parseInt(textField.getText());

                	TimeSeries[] forecastValues = server.createModelForecast(state.getCurrentModel(), noOfValues);
                	TimeSeries forecastPointEstimates = forecastValues[0];
                	TimeSeries forecastLowerPredictionInterval = forecastValues[1];
                	TimeSeries forecastUpperPredictionInterval = forecastValues[2];
		
                	ForecastFile.saveForecastInFile(server.convertToArray(forecastPointEstimates));
                	
                	TimeSeries[] timeSeriesList = { state.getTrainingSet(), forecastPointEstimates
                										, forecastUpperPredictionInterval, forecastLowerPredictionInterval };
                	LineChart<Number, Number> lineChart = Visualizer.plotWithForecast(timeSeriesList
                										, "Model forecast - Plot "+(chartNo++), "Point Estimates");
                	createStage(lineChart, null);
                }
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        }, "Insert", false);
	}

	/**
	 * Creates a window that asks for user input.
	 * 
	 * @param title	A String that represents the title of the window.
	 * @param labelArray	A String array that contains the labels for every input element.
	 * @param controlArray	A Control array that contains every input element.
	 * @param handler	An EventHandler that is used after 'done' button is pressed.
	 * @param buttonText	A String that represents the 'done' button text.
	 * @param doWait	A boolean that specifies if the window should remain open.
	 */
	private void getUserInputWindow(String title, String[] labelArray, Control[] controlArray,
			EventHandler<ActionEvent> handler, String buttonText, boolean doWait) {
	
		if(userInputWindow != null)
			userInputWindow.close();

		userInputWindow = new Stage();
		userInputWindow.setTitle(title);
	
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		for(int i=0; i<labelArray.length; i++) {
			Label label = new Label(labelArray[i]);
			label.setWrapText(true);
			grid.add(label, 0, i);
			grid.add(controlArray[i], 1, i);
		}

		Button btn = new Button(buttonText);
		btn.setOnAction(handler);
		grid.add(btn, 0, 1);	
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, labelArray.length+1);
		
		Scene scene = new Scene(grid, 300, 250);
		userInputWindow.setScene(scene);

		if(doWait)
			userInputWindow.showAndWait();
		else
			userInputWindow.show();	
	}

	/**
	 * Creates a new plot in a new window.
	 * 
	 * @param ts	A TimeSeries to be plotted.
	 * @param name	The name of the window that is going to contain the plot.
	 */
	private void plotAction(TimeSeries ts, String name) {
		try {
			LineChart<Number, Number> lineChart = Visualizer.plot(ts, name+" "+(chartNo++));
			createStage(lineChart, ts);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new window (stage) and changes current timeSeries to ts if specified.
	 * 
	 * @param parent	A Parent that represents contents of the new window.	
	 * @param ts	A TimeSeries that becomes the current timeSeries if specified.
	 */
	private void createStage(Parent parent, TimeSeries ts) {
		Stage stage = new Stage();
		BorderPane borderPane = new BorderPane();
		ButtonBar buttonBar = new ButtonBar();
		borderPane.setBottom(buttonBar);
		borderPane.setCenter(parent);
		
		String title = "No Text";
		try {
	    	title = ((Chart)parent).getTitle();
	    
	    	int length = "Model".length();
	    	if(!title.substring(0, length).equals("Model")
	    			&& (!title.substring(0, length).equals("ACF p"))
	    			&& (!title.substring(0, length).equals("PACF "))
	    			&& (!title.substring(0, length).equals("Seaso"))) {
	    		Button btn1 = new Button("Change timeSeries to this");
	    		btn1.setOnAction(new EventHandler<ActionEvent>() {
	    			@Override
	    			public void handle(ActionEvent e) {
	    				state.setTimeSeries(ts);
	    				getMainPlot();
	    				
	    				state.appendLog("Change TimeSeries to this");
	    			}
	    		});

	    		buttonBar.getButtons().add(btn1);
	    	}
		}
		catch(ClassCastException E) {
	    	title = ((Chart)((StackPane)parent).getChildren().get(0)).getTitle();
		}
	
		Button btn2 =  new Button("Screenshot (PNG)");
		buttonBar.getButtons().add(btn2);

		stage.setTitle(title);
		state.appendLog(title);
	        
		stage.setOnHiding(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {

				Stage stage = (Stage)(event.getSource());
				
				int index = -1;
				for(int i=0; i<listOfStages.size(); i++)
					if(listOfStages.get(i).equals(stage))
						index = i;

				listOfWindows.getItems().remove(index);
				listOfStages.remove(index);
			}
		});
		
	    Scene scene  = new Scene(borderPane, 800, 600);
	    stage.setScene(scene);

		btn2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				saveAsPng(scene);
			}
		});

	    TextFlow flow = new TextFlow();
	    Text text1 = new Text(title);
	    Text text2 = new Text("  ");
	    Text text3 = new Text("x");
	    text3.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	@Override
	    	public void handle(MouseEvent event) {
	    		int index = listOfWindows.getSelectionModel().getSelectedIndex(); 
	    		listOfStages.get(index).close();
	    	}
	    });
	    text3.setStyle("-fx-font-weight: bold");
	    flow.getChildren().addAll(text1, text2, text3);

	    text2.setStyle("-fx-font-weight: bold");
	    listOfStages.add(stage);
	    listOfWindows.getItems().add(flow);

	    stage.show();
	}

	/**
	 * Saves a plot to a PNG file.
	 * 
	 * @param scene	A Scene object that contains the plot.
	 */
	private void saveAsPng(Scene scene) {
		WritableImage image = scene.snapshot(null);
		String dateTime = String.valueOf(System.currentTimeMillis());
        File file = new File("images/img"+dateTime+".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Checks if a String represents to a positive Integer.
	 * 
	 * @param strNum	A String to be checked.
	 * @return	A boolean that answers if the validation passes.
	 */
	private boolean isPositiveInteger(String strNum) {
		Pattern pattern = Pattern.compile("\\d+");
		if (strNum == null)
			return false; 
		return pattern.matcher(strNum).matches();
	}

	/**
	 * Listener. Closes all chart windows.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void closeAllWindows(ActionEvent event) {
		while(listOfStages.size()!=0)
			listOfStages.get(0).close();
	}
	
	/**
	 * Listener. Creates and shows the 'Help' window.
	 * 
	 * @param event	An ActionEvent that describes a listener.
	 */
	public void showHelp(ActionEvent event) {
		
		userInputWindow = new Stage();
		userInputWindow.setTitle("Help");

		HelpText helpText = new HelpText();

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		ListView<String> listView = new ListView<String>();
		TextArea textArea = new TextArea();
		textArea.setPrefHeight(400);
		textArea.setPrefWidth(300);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		ArrayList<String> items = new ArrayList<String>();
		for(String choice: helpText.getUserChoices())
			items.add(choice);
		ObservableList<String> data = FXCollections.observableArrayList(items);
        listView.setItems(data);		


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	textArea.setText(helpText.getDescription(newValue));
            }
        });

		grid.add(listView, 0, 0);
		grid.add(textArea, 1, 0);

		Scene scene = new Scene(grid, 500, 500);
		userInputWindow.setScene(scene);

		userInputWindow.show();	
	}
}
