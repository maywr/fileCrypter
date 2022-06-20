package xyz.maywr.filecrypter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author maywr
 * 20.06.2022 16:28
 */
public class FileUtils
{

	public static final String B = "bytes", KB = "KB", MB = "MB", GB = "GB";

	public static byte[] fileToByteArray( File f )
	{
		try
		{
			return Files.readAllBytes( Paths.get( f.getAbsolutePath() ) );
		} catch ( IOException e )
		{
			return null;
		}
	}

	public static void appendToFile( File f, String data, Charset charset )
	{
		try
		{
			FileOutputStream fos = new FileOutputStream( f, true );
			fos.write( data.getBytes( charset ) );
			fos.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static void writeToFile( File f, String data, Charset charset )
	{
		try
		{
			FileOutputStream fos = new FileOutputStream( f );
			fos.write( data.getBytes( charset ) );
			fos.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static void writeToFile( File f, byte[] data )
	{
		try
		{
			FileOutputStream fos = new FileOutputStream( f );
			fos.write( data );
			fos.close();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static byte[] readNBytes( File f, int n )
	{
		try
		{
			byte[] buf = new byte[ n ];
			FileInputStream fis = new FileInputStream( f );
			fis.read( buf );
			fis.close();
			return buf;
		} catch ( Exception e )
		{
			return null;
		}
	}

	public static String getSize( File f )
	{
		long len = f.length();

		if ( len >= Math.pow( 2, 30 ) )
			return String.format( "%.2f %s", len / Math.pow( 2, 30 ), GB );
		else if ( len >= Math.pow( 2, 20 ) )
			return String.format( "%.2f %s", len / Math.pow( 2, 20 ), MB );
		else if ( len >= Math.pow( 2, 10 ) )
			return String.format( "%.2f %s", len / Math.pow( 2, 10 ), KB );
		else
			return String.format( "%d %s", len, B );


	}
}
