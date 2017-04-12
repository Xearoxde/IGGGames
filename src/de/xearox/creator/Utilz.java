package de.xearox.creator;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.io.FileUtils;

public class Utilz {
	
	
	private static final byte[] key = "MyDifficultPassw".getBytes();
//	private static final String transformation = "AES/ECB/PKCS5Padding";
	private static final String transformation = "AES";
	
	
	public static void copyFileFromJarToOutside(LinkCreator instance, String inputPath, String destPath){
		URL inputUrl = Utilz.class.getClass().getResource(inputPath);
		File dest = new File(destPath);
		try {
			FileUtils.copyURLToFile(inputUrl, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger(instance, e);
		}
	}
	
	public static void encrypt(Serializable object, OutputStream ostream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
	    try {
	        // Length is 16 byte
	        SecretKeySpec sks = new SecretKeySpec(key, transformation);

	        // Create cipher
	        Cipher cipher = Cipher.getInstance(transformation);
	        cipher.init(Cipher.ENCRYPT_MODE, sks);
	        SealedObject sealedObject = new SealedObject(object, cipher);

	        // Wrap the output stream
	        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
	        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
	        outputStream.writeObject(sealedObject);
	        outputStream.close();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    }
	}

	public static Object decrypt(InputStream istream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
	    SecretKeySpec sks = new SecretKeySpec(key, transformation);
	    Cipher cipher = Cipher.getInstance(transformation);
	    cipher.init(Cipher.DECRYPT_MODE, sks);

	    CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
	    ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
	    SealedObject sealedObject;
	    try {
	        sealedObject = (SealedObject) inputStream.readObject();
	        inputStream.close();
	        return sealedObject.getObject(cipher);
	    } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static String getPageSource(String pageLink, boolean updater) throws IOException {
		if(LinkCreator.runWithoutBrowser){
			URL url = new URL(pageLink);
			
		    HttpURLConnection con = null;
		    
		    try{
		    	con = (HttpsURLConnection)url.openConnection();
		    } catch (ClassCastException e){
		    	con = (HttpURLConnection)url.openConnection();
		    }
		    con.setRequestProperty("User-Agent", "Mozilla/5.0");
		    BufferedReader br =	new BufferedReader(new InputStreamReader(con.getInputStream()));

			String input;
			StringBuffer response = new StringBuffer();
			while ((input = br.readLine()) != null){
				response.append(input + "\n");  
			}
			br.close();
			return response.toString();
		} else {
			LinkCreator.browser.loadURL(pageLink);
			final CountDownLatch latch = new CountDownLatch(2);
			try {
				latch.await(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return LinkCreator.browser.getHTML();
		}
		
	}
	
	public static void logger(Object obj, String message){
		try {
			String workingDir = getExecutionPath(obj);
			File file = new File(workingDir+File.separator+"linkcreator.log");
			if(file.exists()){
				try {
				    Files.write(Paths.get("linkcreator.log"), (message+"\n").getBytes(), StandardOpenOption.APPEND);
				}catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
			} else {
				FileWriter writer = new FileWriter(file);
				writer.write(message);
				writer.flush();
				writer.close();
			}
			System.out.println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void logger(Object obj, Exception exception){
		try {
			String message = exception.getClass().getName() + "\n";
			for(StackTraceElement stack : exception.getStackTrace())
				message += "\tat " + stack.getClassName() + "." + stack.getMethodName()+ "(" + stack.getFileName() + ":" + stack.getLineNumber() + ")\n";
			
			String workingDir = getExecutionPath(obj);
			File file = new File(workingDir+File.separator+"linkcreator.log");
			if(file.exists()){
				try {
				    Files.write(Paths.get("linkcreator.log"), (message+"\n").getBytes(), StandardOpenOption.APPEND);
				}catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
			} else {
				FileWriter writer = new FileWriter(file);
				writer.write(message);
				writer.flush();
				writer.close();
			}
			System.out.println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static JPanel optionMenu(LinkCreator instance){
		JPanel panel = new JPanel();
		JCheckBox sendStatisticChkBox = new JCheckBox("Send Statistic?");
		String entry = instance.getProperties().getProperty("sendstatistic");
		if(entry != null) sendStatisticChkBox.setSelected(entry.equals("1") ? true : false);
		sendStatisticChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.setProperty("sendstatistic", sendStatisticChkBox.isSelected() ? "1" : "0");
			}
		});
		
		JCheckBox createCacheFileChkBox = new JCheckBox("Create Cache File?");
		entry = instance.getProperties().getProperty("createcachefile");
		if(entry != null) createCacheFileChkBox.setSelected(entry.equals("1") ? true : false);
		createCacheFileChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.setProperty("createcachefile", createCacheFileChkBox.isSelected() ? "1" : "0");
			}
		});
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(sendStatisticChkBox);
		panel.add(createCacheFileChkBox);
		
		return panel;
	}
	
	public static JEditorPane createChangelogView() throws IOException{
		String input = Utilz.getPageSource(LinkCreator.changelogURL, true);
		String[] lines = input.split(";");
		String message = "";
		for(String string : lines){
			message = message + string +"<br>";
		}
		
		// for copying style
	    JLabel label = new JLabel();
	    Font font = label.getFont();

	    // create some css from the label's font
	    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
	    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
	    style.append("font-size:" + font.getSize() + "pt;");

	    // html content
	    JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">" //
	            + "<h2>Changelog</h2>"
	            + "<p>"+message+"</p>"
	            + "<p>If you want to join my Discord Channel, <a href=\"https://discord.gg/WyXzUS6\">just click here</a></p>"
	            + "<p>You can report all bugs on my Discord Channel, if you find some :P <br>"
	            + "or if you have some suggestions ;-)</p>"
	            + "</body></html>");

	    // handle link events
	    ep.addHyperlinkListener(new HyperlinkListener()
	    {
	        @Override
	        public void hyperlinkUpdate(HyperlinkEvent e)
	        {
	            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Utilz.logger(this, e1);
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						Utilz.logger(this, e1);
					}
	        }
	    });
	    ep.setEditable(false);
	    ep.setBackground(label.getBackground());

	    // show
	    return ep;
	}
	
	public static String getExecutionPath(Object obj){
	    String absolutePath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	    absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
	    absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
	    return absolutePath;
	}
}
