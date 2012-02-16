package me.wisbycraft.perks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	
	public static File vanishConfig;
	
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
		
		vanishConfig = new File(PerkUtils.plugin.getDataFolder() + File.pathSeparator + "vanish.txt");
		if (!vanishConfig.exists())
			createDefaultVanished(vanishConfig);
		loadVanishedConfig(vanishConfig);
 		
		capesURL = config.getString("Capes.Url");
		hungerCounter = Float.parseFloat(config.getString("Hunger.counter"));
		hungerRate = Float.parseFloat(config.getString("Hunger.rate"));
		capeRefresh = Integer.parseInt(config.getString("Capes.RefreshTime"));
		
		return true;
		
	}
	
	public static void loadVanishedConfig(File vanishConfig) {

		if (!vanishConfig.exists()) {
			PerkUtils.ErrorConsole("Config file Motd.cfg does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'Motd.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					vanishConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line.trim();
				
				if (line.startsWith("#") || line.length() == 0)
					continue;
				
				PerkVanish.invisible.add(PerkUtils.getPlayer(line));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createDefaultVanished(File vanishConfig) {
		
		try {
			vanishConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					vanishConfig.getPath()));

			writer.write("#This will list all invisable players\n");
			writer.write("# Please dont edit this file, it will just get errors \n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
