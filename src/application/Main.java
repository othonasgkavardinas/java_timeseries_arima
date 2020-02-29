package application;
	

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * The main JavaFX class which opens the main window.
 *
 * @author Othonas Gkavardinas
 */
public class Main extends Application {
	
	private static Stage primaryStage;
	private static MainController mainController;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("TimeSeries Application");
			Main.primaryStage = primaryStage;
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Main.fxml"));
			Parent root = (Parent)loader.load();
			mainController = (MainController)loader.getController();

			Scene scene  = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
       
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
			
			Main.primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Platform.exit();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return primaryStage;
	}
	
	public static MainController getMainController() {
		return mainController;
	}
	
}
