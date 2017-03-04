package de.xearox.creator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

public class Updater extends Thread{
	
	private LinkCreator frame;
	private String extVersion = "";
	private String downloadURL = "";
	private String message = "There is a new update!\n"+
							 "Do you want to download it?\n";
	
	public Updater(LinkCreator frame) {
		this.frame = frame;
		this.setUpdateData();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if(checkVersion() < 0){
			int reply = JOptionPane.showConfirmDialog(frame.getPanel(), message, "New Update Available", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	try {
	        		downloadNewVersion();
					restartApplication();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        else {
	        	Thread.currentThread().interrupt();
	        }
		}
	}
	
	private void restartApplication() throws URISyntaxException {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(LinkCreator.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void setUpdateData(){
		try {
			String source = Utilz.getPageSource("http://pastebin.com/raw/a6EGp7Vi");
			String[] strings = source.split(";");
			extVersion = strings[0].substring(strings[0].indexOf(":")+1, strings[0].length());
			downloadURL = strings[1].substring(strings[1].indexOf(":")+1, strings[1].length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int checkVersion(){
		String[] vals1 = LinkCreator.VERSION.split("\\.");
	    String[] vals2 = this.extVersion.split("\\.");
	    int i = 0;
	    // set index to first non-equal ordinal or length of shortest version string
	    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
	      i++;
	    }
	    // compare first non-equal ordinal number
	    if (i < vals1.length && i < vals2.length) {
	        int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
	        return Integer.signum(diff);
	    }
	    // the strings are equal or one string is a substring of the other
	    // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
	    return Integer.signum(vals1.length - vals2.length);
	}
	
	private void downloadNewVersion(){
		try {
			URL url = new URL(downloadURL);
			String path = LinkCreator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.replace("linkcreator.jar", "");
			File file = new File(path + "/linkcreator.jar");
			System.out.println(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			FileUtils.copyURLToFile(url, file);
			System.out.println("File saved to: " + file.getAbsolutePath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
