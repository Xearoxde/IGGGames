package de.xearox.creator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Callback;
import com.teamdev.jxbrowser.chromium.swing.internal.LightWeightWidget;
import com.teamdev.jxbrowser.chromium.swing.internal.events.LightWeightWidgetListener;

public class Game implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String gameurl;
	private String imagePath;
	private transient BufferedImage image;
	
	public Game() {	}
	
	public Game(String name, String gameurl, String imagePath) {
		this.name = name;
		this.gameurl = gameurl;
		this.imagePath = imagePath;
		this.createImage(imagePath);
	}
	
	public void createImage(){
		createImage(imagePath);
	}
	
	private void createImage(String imagePath){
		if(LinkCreator.runWithoutBrowser){
			try {
				URL url = new URL(imagePath);
				URLConnection con = url.openConnection();
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
				this.image = ImageIO.read(con.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println(imagePath);
			try {
				final CountDownLatch latch = new CountDownLatch(2);
				URL url = new URL(imagePath);
				
				LightWeightWidget widget = (LightWeightWidget) LinkCreator.view.getComponent(0);
				widget.addLightWeightWidgetListener(new LightWeightWidgetListener() {
		            @Override
		            public void onRepaint(Rectangle updatedRect, Dimension viewSize) {
		                // Make sure that all view content has been repainted.
		                if (viewSize.equals(updatedRect.getSize())) {
		                    latch.countDown();
		                }
		            }
		        });
				
				Browser.invokeAndWaitFinishLoadingMainFrame(LinkCreator.browser, new Callback<Browser>() {
		            @Override
		            public void invoke(Browser browser) {
		                browser.loadURL(imagePath);
		            }
		        });
				
				try {
					latch.await(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//LinkCreator.browser.loadURL(imagePath);
				LinkCreator.browser.setSize(210, 210);
				
				
				this.image = convertRenderedImage(((RenderedImage) widget.getImage()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private BufferedImage convertRenderedImage(RenderedImage img) {
	    if (img instanceof BufferedImage) {
	        return (BufferedImage)img;  
	    }   
	    ColorModel cm = img.getColorModel();
	    int width = img.getWidth();
	    int height = img.getHeight();
	    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    Hashtable properties = new Hashtable();
	    String[] keys = img.getPropertyNames();
	    if (keys!=null) {
	        for (int i = 0; i < keys.length; i++) {
	            properties.put(keys[i], img.getProperty(keys[i]));
	        }
	    }
	    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
	    img.copyData(raster);
	    return result;
	}
	
	public String getName(){
		return name;
	}
	
	public String getGameURL(){
		return gameurl;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
}
