package client;

import javafx.scene.control.Control;

/**
 * Creates the contents of the user input window, for the file loading.
 *
 * @author Othonas Gkavardinas
 */
public interface RecordInput {
	/**
	 * Initializes a form, that asks important data for file loading.
	 * 
	 * @param labelArray	A String array that contains the labels of the elements to be initialized.
	 * @param controlArray	A Control array that contains the elements to be initialized.
	 */
	public void initializeForm(String[] labelArray, Control[] controlArray);
}
