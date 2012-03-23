package me.wisbycraft.perks.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import me.wisbycraft.perks.donator.PerkKits;
import me.wisbycraft.perks.utils.PerkKit;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;


public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	
	public static boolean loadConfig () {
		
		PerkKits.kits.clear();
		
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
		
		File kitConfig = new File(PerkUtils.plugin.getDataFolder() + "/" + "kits.cfg");
		if (!kitConfig.exists()) {
			createDefaultKitConfig(kitConfig);
		}
		loadKitsConfig(kitConfig);
		
		File blacklist = new File(PerkUtils.plugin.getDataFolder() + "/" + "blacklist.cfg");
		if (!blacklist.exists()) {
			createDefaultBlacklist(blacklist);
		}
		loadBlacklist(blacklist);
		
		return true;
		
	}
	
	public static void createDefaultKitConfig(File kitConfig) {
		
		PerkUtils.log.info("Creating default kit config.");
		
		try {
			kitConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(kitConfig.getPath()));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadKitsConfig(File kitConfig) {
		
		if (!kitConfig.exists()) {
			PerkUtils.log.log(Level.SEVERE, "Config file 'Kit.cfg' does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'Kit.cfg'");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(kitConfig.getPath()));

			String name = null;
			String timeoutString = null;
			ArrayList<ItemStack> newKit = null;
						
			String line = null;
			while ((line = reader.readLine()) != null) {
				
				line = line.trim();
				
				if (line.startsWith("#"))
					continue;
				
				if (line.length() == 0 && newKit != null) {
					int timeout = Integer.parseInt(timeoutString);
					PerkKit kit = new PerkKit(name, timeout, newKit);
					PerkKits.kits.add(kit);
					newKit = null;
				}
				
				if (line.startsWith("[")) {
					name = line.substring(line.indexOf('[') + 1, line.indexOf('='));
					timeoutString = line.substring(line.indexOf('=') + 1, line.indexOf(']'));
					newKit = new ArrayList<ItemStack>();
					continue;
				}
				
				int amount = 1;
				Material material = null;
				
				if (line.indexOf(",") != -1) {
					
					String ammountString = line.substring(line.indexOf(',') + 1);
					
					if (PerkUtils.isNumeric(ammountString)) {
						amount = Integer.parseInt(ammountString);
						line = line.substring(0, line.indexOf(','));
					}
				}
				
				if (PerkUtils.isNumeric(line)) {
					if (Material.getMaterial(Integer.parseInt(line)) == null) {
						PerkUtils.ErrorConsole("Could not find item with id " + line);
						continue;
					}
					
					material = Material.getMaterial(Integer.parseInt(line));
				}
				
				if (material == null)
					continue;
				
				ItemStack item = new ItemStack(material, amount);
				newKit.add(item);
			}
			
			if (newKit != null) {
				int timeout = Integer.parseInt(timeoutString);
				PerkKit kit = new PerkKit(name, timeout, newKit);
				PerkKits.kits.add(kit);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
					
	}
	
	public static void createDefaultBlacklist(File blacklist) {
		
		PerkUtils.log.info("Creating blacklist.");
		
		try {
			blacklist.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(blacklist.getPath()));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadBlacklist(File blacklist) {
		
		if (!blacklist.exists()) {
			PerkUtils.log.log(Level.SEVERE, "Config file 'blacklist.cfg' does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'blacklist.cfg'");
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(blacklist.getPath()));
			
			OfflinePlayer blackListedPlayer = null;
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0)
					continue;
				
				if (PerkUtils.server().getOfflinePlayer(line) == null)
					continue;
				
				blackListedPlayer = PerkUtils.server().getOfflinePlayer(line);
				PerkUtils.blacklist.add(blackListedPlayer);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
