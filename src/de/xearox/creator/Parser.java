package de.xearox.creator;

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
