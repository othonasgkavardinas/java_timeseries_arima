package client;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Creates the contents of the user input window, for the UCR file loading.
 *
 * @author Othonas Gkavardinas
 */
public class UCRInput implements RecordInput{
	public void initializeForm(String[] labelArray, Control[] controlArray) {
		labelArray[0] = "File:";
		Button button = new Button("Select");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				final FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");

				Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					((TextField)controlArray[1]).setText(file.toString());
				}
			}
		});
		controlArray[0] = button;
		
		labelArray[1] = "Selected:";
		TextField textField = new TextField("");
		controlArray[1] = textField;
		
		labelArray[2] = "Delimeter:";
		ArrayList<String> delimeters = new ArrayList<String>();
		delimeters.add("\\t");
		ComboBox<String> comboBox = new ComboBox<String>(FXCollections.observableArrayList(delimeters));
		controlArray[2] = comboBox;
		
		labelArray[3] = "Series:";
		TextField textField2 = new TextField();
		controlArray[3] = textField2;
	}
}
