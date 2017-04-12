package de.xearox.creator;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

import javax.crypto.NoSuchPaddingException;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import de.xearox.creator.gui.LinkCreatorWindow;
import de.xearox.creator.gui.MainWindow;

public class LinkCreator {
	
//	public static final String VERSION = LinkCreator.class.getClass().getPackage().getImplementationVersion();
	public static final String VERSION = "1.6.5-WIN";
	
	public static Browser browser = null;
	public static BrowserView view;
	public static boolean runWithoutBrowser = true;
	public static final String IGGURL = "http://www.igg-games.com";
	
	private Properties prop;
	private static LinkCreator INSTANCE;
	
	public static final String changelogURL = "http://pastebin.com/raw/y51jHcb5";

	public static void main(String[] args) {
		new LinkCreator();
	}
	
	public LinkCreator instance(){
		return INSTANCE;
	}
	
	public Properties getProperties(){
		return prop;
	}
	
	public LinkCreator() {
		if(!LinkCreator.runWithoutBrowser)
		try{
			browser = new Browser(BrowserType.LIGHTWEIGHT);
			view = new BrowserView(browser);
			browser.loadURL(IGGURL);
		} catch(Exception e){
			System.out.println("Run without browser");
			LinkCreator.runWithoutBrowser = true;
		}
		LinkCreator.INSTANCE = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow mainWindow = new MainWindow(browser, view, LinkCreator.INSTANCE);
					mainWindow.setVisible(true);
					new Updater(INSTANCE, mainWindow).run();
				} catch (Exception e) {
					Utilz.logger(this, e);
				}
			}
		});
		propertyHandling();
		copyFiles();
	}
	
	private void copyFiles(){
		if(!new File(Utilz.getExecutionPath(this)+File.separator+"start.bat").exists()){
			Utilz.copyFileFromJarToOutside(this,"/start.bat", Utilz.getExecutionPath(this)+File.separator+"start.bat");
		}
		if(!new File(Utilz.getExecutionPath(this)+File.separator+"start.sh").exists()){
			Utilz.copyFileFromJarToOutside(this,"/start.sh", Utilz.getExecutionPath(this)+File.separator+"start.sh");
		}
	}
	
	private void propertyHandling(){
		if(checkPropFile()){
			loadProperties();
		} else {
			createProperties();
		}
	}
	
	private boolean checkPropFile(){
		return new File("linkcreator.conf").exists();
	}
	
	private void loadProperties(){
		try {
			InputStream is = new FileInputStream(new File("linkcreator.conf"));
			prop = new Properties(System.getProperties());
			prop.load(is);
			is.close();
			if(!prop.getProperty("version").equalsIgnoreCase(VERSION)){
				setProperty("version", VERSION);
				setProperty("newversion", "1");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Utilz.logger(this, e);
		}
	}
	
	public void setProperty(String key, String value){
		try {
			Writer writer = new FileWriter(new File("linkcreator.conf"));
			prop.setProperty(key, value);
			prop.store(writer, "LinkCreatorConfig");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createProperties(){
		try {
	    	Writer writer = new FileWriter(new File("linkcreator.conf"));
			prop = new Properties(System.getProperties());
			prop.setProperty("version", VERSION);
			prop.setProperty("newversion", "1");
			prop.setProperty("sendstatistic", "1");
			prop.setProperty("createcachefile", "1");
			prop.store(writer, "LinkCreatorConfig");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Utilz.logger(this, e);
		}
	}
	
	public void checkAndLoadChacheFile(LinkCreatorWindow window){
		String entry = prop.getProperty("createcachefile");
		if(entry != null){
			if(entry.equals("0")){
				return;
			}
		}
		String path = LinkCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replace("linkcreator.jar", "");
		File file = new File(path + "/cache.dat");
		if(file.exists()){
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
				SaveClass save = (SaveClass)Utilz.decrypt(is);
				Map<String, Game> games = save.getGames();
				for(Game game : games.values()){
					game.createImage();
				}
				window.setGames(games);
				window.fillList();
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
				// TODO Auto-generated catch block
				Utilz.logger(this, e);
			}
		}
		
	}
	
	public void createCacheFile(LinkCreatorWindow window){
		try {
			
			if(window.getGames().isEmpty()){
				return;
			}
			String path = LinkCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.replace("linkcreator.jar", "");
			File file = new File(path + "/cache.dat");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			SaveClass save = new SaveClass(window.getGames());
			
			try {
				Utilz.encrypt(save, out);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				Utilz.logger(this, e);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				Utilz.logger(this, e);
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				Utilz.logger(this, e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Utilz.logger(this, e);
		}
	}
	
	
	

}
