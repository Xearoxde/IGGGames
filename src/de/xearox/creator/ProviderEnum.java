package de.xearox.creator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ProviderEnum {
	
	
	
	mega(0,"Mega.co.nz", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=", "s%3A%2F%2F","%2F%23%21","%21","\" target=\"_blank\">.*?\n")), new ArrayList<String>(Arrays.asList("", "https://", "/#!","!","\n"))),
	openload(1,"Openload.co", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=s://", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("https://", "\n"))),
	kumpulBagi(2,"KumpulBagi", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=://dis", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("http://dis", "\n"))),
	upFile(3,"UpFile", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=://upfile", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("http://upfile", "\n"))),
	downAce(4,"DownACE", new ArrayList<String>(Arrays.asList("", "")), new ArrayList<String>(Arrays.asList("", ""))),
	go4Up(5,"Go4Up", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=://go4up", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("http://go4up", "\n"))),
	uploaded(6,"Uploaded", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=://ul.to", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("http://ul.to", "\n"))),
	upToBox(7,"Uptobox", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=://uptobox", "\" rel.*?\n")), new ArrayList<String>(Arrays.asList("http://uptobox", "\n"))),
	googleDrive(8,"Google Drive", new ArrayList<String>(Arrays.asList("&#8211;.*?xurl=s://", "&amp;export=download.*?\n")), new ArrayList<String>(Arrays.asList("https://", "\n")));
	
	private final int id;
	private final String providerName;
	private final List<String> placeholders;
	private final List<String> replacer;

	private ProviderEnum(int ID, String providerName, List<String> placeholders, List<String> replacer) {
		this.id = ID;
		this.providerName = providerName;
		this.placeholders = placeholders;
		this.replacer = replacer;
		
	}
	
	public int getID(){
		return id;
	}
	
	public String getProviderName(){
		return providerName;
	}
	
	public List<String> getPlaceholders() {
		return placeholders;
	}
	
	public List<String> getReplacer(){
		return replacer;
	}
	
	public static ProviderEnum getProviderByName(String providerName){
		for(ProviderEnum e : ProviderEnum.values()){
			if(e.getProviderName().equalsIgnoreCase(providerName)) return e;
		}
		return null;
	}
	
}
