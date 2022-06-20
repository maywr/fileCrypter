package xyz.maywr.filecrypter.ui;

import xyz.maywr.filecrypter.Bootstrap;
import xyz.maywr.filecrypter.crypting.FileAES;
import xyz.maywr.filecrypter.util.FileUtils;
import xyz.maywr.filecrypter.util.RandomUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author maywr
 * 20.06.2022 16:28
 */
public class Window extends JFrame
{

	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = 455, HEIGHT = 350;

	public static final Rectangle ENCRYPT_BUTTON = new Rectangle( 335, 270, 90, 25 );
	public static final Rectangle DECRYPT_BUTTON = new Rectangle( 240, 270, 90, 25 );

	public static final Rectangle FILE_PATH_LABEL = new Rectangle( 11, 15, 80, 25 );
	public static final Rectangle FILE_PATH_FIELD = new Rectangle( 10, 40, 380, 25 );
	public static final Rectangle FILE_CHOOSE_BUTTON = new Rectangle( 395, 40, 30, 25 );

	public static final Rectangle KEY_LABEL = new Rectangle( 11, 75, 160, 25 );
	public static final Rectangle KEY_FIELD = new Rectangle( 10, 100, 380, 25 );
	public static final Rectangle GENERATE_BUTTON = new Rectangle( 395, 100, 30, 25 );

	public static final Rectangle FILE_INFO_LABEL = new Rectangle( 11, 140, 80, 25 );
	public static final Rectangle FILE_INFO_AREA = new Rectangle( 11, 165, 415, 92 );


	private static final Font MONTSERRAT;

	static
	{
		try
		{
			GraphicsEnvironment.getLocalGraphicsEnvironment()
			                   .registerFont( Font.createFont( Font.TRUETYPE_FONT, Window.class.getClassLoader()
			                                                                                   .getResourceAsStream( "font.ttf" ) ) );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		MONTSERRAT = new Font( "Montserrat SemiBold", Font.PLAIN, 12 );

	}

	public Window()
	{
		super( Bootstrap.TITLE );
		this.setBounds( SCREEN_SIZE.width / 2 - ( WIDTH / 2 ), SCREEN_SIZE.height / 2 - ( HEIGHT / 2 ), WIDTH, HEIGHT );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		this.setLayout( null );
		this.setIcon( "logo.png" );
		this.setResizable( false );

		JButton encryptButton = new JButton( "encrypt" );
		encryptButton.setBounds( ENCRYPT_BUTTON );
		this.add( encryptButton );

		JButton decryptButton = new JButton( "decrypt" );
		decryptButton.setBounds( DECRYPT_BUTTON );
		this.add( decryptButton );

		JLabel filePathLabel = new JLabel( "file path:" );
		filePathLabel.setBounds( FILE_PATH_LABEL );
		this.add( filePathLabel, 16 );

		JTextField filePathField = new JTextField();
		filePathField.setBounds( FILE_PATH_FIELD );
		this.add( filePathField, 14 );

		JButton fileChooseButton = new JButton( "..." );
		fileChooseButton.setBounds( FILE_CHOOSE_BUTTON );
		fileChooseButton.setToolTipText( "choose a file" );
		this.add( fileChooseButton );

		JLabel keyLabel = new JLabel( "secret key:" );
		keyLabel.setBounds( KEY_LABEL );
		this.add( keyLabel, 16 );

		JTextField keyField = new JTextField();
		keyField.setBounds( KEY_FIELD );
		this.add( keyField, 14 );

		JButton generateButton = null;
		try
		{
			generateButton = new JButton( new ImageIcon( ImageIO.read( getClass().getClassLoader()
			                                                                     .getResourceAsStream( "generate.png" ) ) ) );
			generateButton.setBounds( GENERATE_BUTTON );
			generateButton.setToolTipText( "generate a random key" );
			this.add( generateButton );
		} catch ( Exception e )
		{
			e.printStackTrace();
		}

		JLabel fileInfoLabel = new JLabel( "file info:" );
		fileInfoLabel.setBounds( FILE_INFO_LABEL );
		this.add( fileInfoLabel, 16 );

		JTextArea fileInfoArea = new JTextArea();
		fileInfoArea.setBounds( FILE_INFO_AREA );
		fileInfoArea.setLineWrap( true );
		fileInfoArea.setEditable( false );
		fileInfoArea.setBackground( new Color( 0x46494b, false ) );
		fileInfoArea.setWrapStyleWord( true );
		this.add( fileInfoArea );

		generateButton.addActionListener( e -> keyField.setText( RandomUtils.getRandomString( 15 ) ) );

		fileChooseButton.addActionListener( e ->
		{
			NativeJFileChooser nfileChooser = new NativeJFileChooser( System.getProperty( "user.home" ) );
			int option = nfileChooser.showOpenDialog( null );
			if ( option == NativeJFileChooser.APPROVE_OPTION )
			{
				File file = nfileChooser.getSelectedFile();
				filePathField.setText( file.getAbsolutePath() );

				String fileInfo =
						"---------------------------------------------\n" +
						"name : " + file.getName() + "\n" +
						"size: " + FileUtils.getSize( file ) + "\n" +
						"encrypted?: " + FileAES.isEncrypted( file ) + "\n" +
						"last modified: " + new Date( file.lastModified() ) + "\n" +
						"---------------------------------------------\n";

				fileInfoArea.setText( fileInfo );
			}
		} );

		encryptButton.addActionListener( e ->
		{
			if ( keyField.getText().isEmpty() )
			{
				JOptionPane.showMessageDialog( null, "enter a key", Bootstrap.TITLE, JOptionPane.ERROR_MESSAGE );
				return;
			}
			File f = new File( filePathField.getText() );
			FileUtils.writeToFile( f, FileAES.encrypt( f, keyField.getText() ) );
			fileInfoArea.setText( fileInfoArea.getText().replace( "encrypted?: false", "encrypted?: true" ) );
			JOptionPane.showMessageDialog( this, "successfully encrypted " + f.getName(), Bootstrap.TITLE, JOptionPane.INFORMATION_MESSAGE );
		} );

		decryptButton.addActionListener( e ->
		{
			if ( keyField.getText().isEmpty() )
			{
				JOptionPane.showMessageDialog( null, "enter a key", Bootstrap.TITLE, JOptionPane.ERROR_MESSAGE );
				return;
			}
			File f = new File( filePathField.getText() );
			if ( !FileAES.isEncrypted( f ) )
			{
				JOptionPane.showMessageDialog( null, "the chosen file is not encrypted", Bootstrap.TITLE, JOptionPane.ERROR_MESSAGE );
				return;
			}
			byte[] decrypted = FileAES.decrypt( f, keyField.getText() );
			if ( decrypted == null )
			{
				JOptionPane.showMessageDialog( null, "the key is wrong", Bootstrap.TITLE, JOptionPane.ERROR_MESSAGE );
				return;
			}
			FileUtils.writeToFile( f, decrypted );
			fileInfoArea.setText( fileInfoArea.getText().replace( "encrypted?: true", "encrypted?: false" ) );
			JOptionPane.showMessageDialog( this, "successfully decrypted " + f.getName(), Bootstrap.TITLE, JOptionPane.INFORMATION_MESSAGE );
		} );
	}

	public Window open()
	{
		this.setVisible( true );
		return this;
	}

	private void setIcon( String resourceName )
	{
		try
		{
			this.setIconImage( ImageIO.read( getClass().getClassLoader().getResource( resourceName ) ) );
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public Component add( Component comp )
	{
		comp.setFont( MONTSERRAT );
		return super.add( comp );
	}

	public Component add( Component comp, int fontSize )
	{
		comp.setFont( MONTSERRAT.deriveFont( ( float ) fontSize ) );
		return super.add( comp );
	}
}
