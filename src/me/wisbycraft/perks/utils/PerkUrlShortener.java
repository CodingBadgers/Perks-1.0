package me.wisbycraft.perks.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.ChatColor;

public class PerkUrlShortener {

	public static String tinyUrl(String url) throws Exception {
		// get the tinyurl url
		URL api = new URL("http://tinyurl.com/api-create.php?url=" + url);
		
		if (api != null) {
			// read the url into the variable
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(api.openStream()));
	        url = in.readLine();
	        in.close();
		}
        
		return ChatColor.DARK_AQUA + url + ChatColor.RESET;
	}
}
