package xyz.maywr.filecrypter.crypting;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

/**
 * @author maywr
 * 20.06.2022 16:28
 */
public class AES256
{

	private static final String SALT = "abcdefjgijklmnop";

	public static byte[] decrypt( byte[] input, String key )
	{
		try
		{
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec( iv );

			SecretKeyFactory factory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA256" );
			KeySpec spec = new PBEKeySpec( key.toCharArray(), SALT.getBytes(), Short.MAX_VALUE * 2, 256 );
			SecretKey tmp = factory.generateSecret( spec );
			SecretKeySpec secretKey = new SecretKeySpec( tmp.getEncoded(), "AES" );

			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
			cipher.init( Cipher.DECRYPT_MODE, secretKey, ivspec );
			return cipher.doFinal( input );
		} catch ( Exception e )
		{
			return null;
		}
	}

	public static byte[] encrypt( byte[] input, String key )
	{
		try
		{
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec( iv );

			SecretKeyFactory factory = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA256" );
			KeySpec spec = new PBEKeySpec( key.toCharArray(), SALT.getBytes(), Short.MAX_VALUE * 2, 256 );
			SecretKey tmp = factory.generateSecret( spec );
			SecretKeySpec secretKey = new SecretKeySpec( tmp.getEncoded(), "AES" );

			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
			cipher.init( Cipher.ENCRYPT_MODE, secretKey, ivspec );
			return cipher.doFinal( input );
		} catch ( Exception e )
		{
			return null;
		}
	}
}
