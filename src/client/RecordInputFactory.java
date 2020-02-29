package client;


/**
 * Factory that creates RecordInput objects.
 *
 * @author Othonas Gkavardinas
 */
public class RecordInputFactory {
	public static RecordInput createRecordInput(String type) {
		if(type.equals("Column"))
			return new ColumnInput();
		else if(type.equals("UCR"))
			return new UCRInput();
		return null;
	}
}
