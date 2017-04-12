package de.xearox.creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class Parser {

	public static String mode1(String input){
		List<String> encodedURLs = new ArrayList<>();
		List<String> decodedURLs = new ArrayList<>();
		
		encodedURLs = Arrays.asList(input.split("\n"));
		
		for(int i = 0 ; i < encodedURLs.size(); i++){
			String temp = encodedURLs.get(i);
			temp = temp.replace("</a><a target=\"_blank\" rel=\"nofollow\" href=\"http://igg-games.com/urls/", "");
			temp = temp.substring(0, temp.indexOf("\""));
			encodedURLs.set(i, temp);
		}
		
		
		encodedURLs.forEach(text->{
			try {
				byte[] decoded = Base64.decodeBase64(text);
				decodedURLs.add(new String(decoded, "UTF-8"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		String[] string = decodedURLs.stream().toArray(String[]::new);
		input = "";
		for(String text : string){
			try{
				input = input + (text.substring(text.lastIndexOf("xurl"), text.length()))+ "\n";
			} catch (StringIndexOutOfBoundsException e){
				
			}
		}
		input = input.replaceAll("</a>", "\n");
		input = input.replaceAll(" – <a.*?<a href", " &#8211; <a href");
		
		return input;
	}
	
	public static String mode2(ProviderEnum provider, String input){
		try{
			input = input.replaceAll("&#8211;", "");
			
			input = Parser.finalStep(provider, input);
			
			List<String> list = new ArrayList<>();
			
			String[] arr = input.split("\n");
			for(String string : arr){
				list.add(string.substring(string.indexOf("<a href=")+9, string.length()));
			}
			
			input = "";
			
			for(int i = 0; i < list.size(); i++){
				String temp = list.get(i);
				if(provider == ProviderEnum.mega){
					temp = temp.substring(temp.indexOf(provider.getReplacer().get(1)), temp.length());
				} else {
					temp = temp.substring(temp.indexOf(provider.getReplacer().get(0)), temp.length());
				}
				input = input + temp + "\n";
			}
			return input;
		} catch(StringIndexOutOfBoundsException e){
			return "";
		}
	}
	
	public static String mode3(ProviderEnum provider, String input){
		input = input.replaceAll("</a>", "</a>\n");
		input = input.replaceAll("&#8211;", "");
		List<String> urls = Arrays.asList(input.split("\n"));
		for(int index = 0; index < urls.size(); index++){
			urls.set(index, urls.get(index).replaceAll("<a target=\"_blank\" rel=\"nofollow\" href=\"", ""));
			urls.set(index, urls.get(index).replaceAll("target.*?</a>", ""));
			urls.set(index, getURL(urls.get(index)));
		}
		String output = "";
		for(int index = 0; index < urls.size(); index++){
			if(!urls.get(index).contains("http")) continue;
			output += urls.get(index) + "\n";
		}
		return mode2(provider, output);
	}
	
	private static String getURL(String input){
		try {
			if(!input.contains("http")) return "";
			input = input.replaceAll(" ", "");
			
			URL url = new URL(input);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
		    con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.connect();
			try{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} catch(Exception e){
				return con.getURL().toString();
			}
			
			return con.getURL().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String finalStep(ProviderEnum provider, String input){
		for (String placeholder : provider.getPlaceholders()) {
			String replacement = (String) provider.getReplacer()
					.get(provider.getPlaceholders().indexOf(placeholder));
			input = input.replaceAll(placeholder, replacement);
		}
		return input;
	}
	
}
