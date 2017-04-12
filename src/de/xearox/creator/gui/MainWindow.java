package de.xearox.creator.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import de.xearox.creator.LinkCreator;
import de.xearox.creator.SendStatistic;
import de.xearox.creator.Utilz;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
	
	private LinkCreator instance;
	
	public MainWindow(Browser browser, BrowserView browserView, LinkCreator instance) {
		this.instance = instance;
		setupWindow(browser, browserView);
	}
	
	private void setupWindow(Browser browser, BrowserView browserView){
		LinkCreatorWindow linkCreatorWindow = new LinkCreatorWindow();
//		BrowserWindow browserWindow = new BrowserWindow(browser, browserView);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String entry = instance.getProperties().getProperty("createcachefile");
				if(entry != null){
					if(entry.equals("0")){
						return;
					}
				}
				instance.createCacheFile(linkCreatorWindow);
				if(!LinkCreator.runWithoutBrowser) browser.stop();
			}
		});
		setTitle("IGG-GAMES Download Link Creator - Created by Xearox - Version "+LinkCreator.VERSION);
		setDefaultCloseOperation(3);
		setBounds(100, 100, 1005, 741);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 782, 565);
        getContentPane().add(tabbedPane);
		
        if(!LinkCreator.runWithoutBrowser) tabbedPane.addTab("Browser", null, browserView, null);
        tabbedPane.addTab("LinkCreator", null, linkCreatorWindow, null);
		
		
		
        
        
        instance.checkAndLoadChacheFile(linkCreatorWindow);
		if(instance.getProperties().getProperty("newversion").equals("1")){
			String message = "Do you want see the changelog?";
			int reply = JOptionPane.showConfirmDialog(this, message, "View Changelog?", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	try {
					JOptionPane.showMessageDialog(this, Utilz.createChangelogView());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Utilz.logger(this, e1);
				}
	        }
	        instance.getProperties().setProperty("newversion", "0");
		}
		
		
		new SendStatistic().run();
		String entry = instance.getProperties().getProperty("sendstatistic");
		if(entry != null){
			if(entry.equals("1")){
//				new SendStatistic().run();
			}
		}
	}

}
