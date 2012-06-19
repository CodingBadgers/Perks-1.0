 package me.wman.perks.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.ChatColor;

public class PerkUrlShortener {

	private static String ShortenURL(URL url) {
			
		String shorturl = url.getHost();
		
		try {
			URL tinyurl = new URL("http://tinyurl.com/api-create.php?url=" + url.getProtocol() + "://" + url.getHost());
			BufferedReader in = new BufferedReader(new InputStreamReader(tinyurl.openStream()));
			shorturl = in.readLine();
			in.close();
		} catch (IOException e) {
		}
        
		return ChatColor.DARK_AQUA + shorturl + ChatColor.RESET;
	}
	
	public static String parseMessage(String msg) {
		String returnMsg = "";
		
		String[] words = msg.split(" ");
		for (String word : words)
		{
			try {
				URL url = new URL(word);
				word = ShortenURL(url);
			} catch (MalformedURLException e) {
			}
			
			returnMsg += word + " ";
		}
		
		return returnMsg;
	}
}
