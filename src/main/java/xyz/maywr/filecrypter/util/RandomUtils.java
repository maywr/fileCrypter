package xyz.maywr.filecrypter.util;

import java.util.Base64;
import java.util.Random;

/**
 * @author maywr
 * 20.06.2022 19:11
 */
public class RandomUtils
{
	public static String getRandomString( int byteLen )
	{
		byte[] salt = new byte[ byteLen ];
		new Random().nextBytes( salt );
		return Base64.getEncoder().encodeToString( salt )
		             .replace( "\\", "" )
		             .replace( "/", "" )
		             .replace( "=", "" )
		             .replace( "+", "" );
	}
}
