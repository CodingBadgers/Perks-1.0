package me.wisbycraft.perks;

import org.bukkit.configuration.file.FileConfiguration;

public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	
	public static boolean loadConfig () {
		
		FileConfiguration config = PerkUtils.plugin.getConfig();
		
		try {
			config.addDefault("Capes.Url", "http://www.redstonetechnologic.co.cc/capes/");
			config.addDefault("Hunger.rate", "0.25");
			config.addDefault("Hunger.counter", "0.0");
			config.addDefault("Capes.RefreshTime", 5);

			config.options().copyDefaults(true);
			PerkUtils.plugin.saveConfig();
		} catch (Exception ex) {
	    	
			ex.printStackTrace();
			return false;
			
		}
		
		capesURL = config.getString("Capes.Url");
		hungerCounter = Float.parseFloat(config.getString("Hunger.counter"));
		hungerRate = Float.parseFloat(config.getString("Hunger.rate"));
		capeRefresh = Integer.parseInt(config.getString("Capes.RefreshTime"));
		
		return true;
		
	}
}
