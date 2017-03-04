package de.xearox.creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utilz {

	public static String getPageSource(String pageLink) throws IOException {
		URL url = new URL(pageLink);
	
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	
		StringBuffer response = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();
		return response.toString();
	}

}
