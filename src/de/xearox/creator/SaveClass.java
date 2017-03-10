package de.xearox.creator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SaveClass implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Game> games = new HashMap<String, Game>();
	
	
	public SaveClass(Map<String, Game> games) {
		this.setGames(games);
	}
	
	public Map<String, Game> getGames(){
		return games;
	}
	
	public void setGames(Map<String, Game> games){
		this.games = games;
	}
	
}
