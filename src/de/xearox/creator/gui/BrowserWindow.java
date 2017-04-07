package de.xearox.creator.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class BrowserWindow extends JPanel{
	
	
	
	public BrowserWindow(Browser browser, BrowserView browserView) {
		
		this.add(browserView);
	}
}
