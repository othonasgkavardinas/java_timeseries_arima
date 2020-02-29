package server;


/**
 * Factory that creates RecordLoader objects.
 *
 * @author Othonas Gkavardinas
 */
public class RecordLoaderFactory {
	public static RecordLoader createRecordLoader(String type) {
		if(type.equals("Column"))
			return new ColumnLoader();
		else if(type.equals("UCR"))
			return new UCRLoader();
		return null;
	}
}
