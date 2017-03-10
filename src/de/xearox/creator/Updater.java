package de.xearox.creator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
		if(new File(Utilz.getExecutionPath(this)+File.separator+"updater.jar").exists()){
			new File(Utilz.getExecutionPath(this)+File.separator+"updater.jar").delete();
		}
		if(checkVersion() < 0){
			int reply = JOptionPane.showConfirmDialog(frame.getPanel(), message, "New Update Available", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	try {
	        		Utilz.copyFileFromJarToOutside("/updater.jar", Utilz.getExecutionPath(this)+File.separator+"updater.jar");
					startUpdater();
					System.exit(0);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					Utilz.logger(this, e);
				}
	        }
	        else {
	        	Thread.currentThread().interrupt();
	        }
		}
	}
	
	private void startUpdater() throws URISyntaxException {
		
		String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		String currentJarPath = Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File currentJar = new File(Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		currentJarPath = currentJarPath.replace(currentJar.getName(), "updater.jar");
		currentJar = new File(currentJarPath);
		
		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());
		command.add(downloadURL);
		System.out.println(command);

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Utilz.logger(this, e);
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
			Utilz.logger(this, e);
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
	
	
}
