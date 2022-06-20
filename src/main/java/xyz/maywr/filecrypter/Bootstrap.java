package xyz.maywr.filecrypter;

import com.formdev.flatlaf.FlatDarkLaf;
import xyz.maywr.filecrypter.ui.Window;


/**
 * @author maywr
 * 20.06.2022 16:28
 */
public class Bootstrap
{
	public static final String VERSION = "1.0";
	public static final String TITLE = String.format( "%s - %s", "fileCrypter", VERSION );

	private static Window window;

	public static void main( String[] args ) throws Exception
	{
		FlatDarkLaf.setup();
		window = new Window().open();
	}
}
