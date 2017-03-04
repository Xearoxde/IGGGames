package de.xearox.creator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Game {
	private String name;
	private String gameurl;
	private String imagePath;
	private BufferedImage image;
	
	public Game() {	}
	
	public Game(String name, String gameurl, String imagePath) {
		this.name = name;
		this.gameurl = gameurl;
		this.imagePath = imagePath;
		this.createImage(imagePath);
	}
	
	private void createImage(String imagePath){
		try {
			URL url = new URL(imagePath);
			this.image = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
