package me.wman.perks.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import me.wman.perks.donator.PerkKits;
import me.wman.perks.utils.PerkKit;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;


public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	public static String shutdownMessage;
	public static int shutdownTimeout;
	
	public static boolean loadConfig () {
		
		PerkKits.kits.clear();
		
		FileConfiguration config = PerkUtils.plugin.getConfig();
		
		try {
			config.addDefault("Capes.Url", "http://www.redstonetechnologic.co.cc/capes/");
			config.addDefault("Hunger.rate", "0.25");
			config.addDefault("Hunger.counter", "0.0");
			config.addDefault("Capes.RefreshTime", 5);
			config.addDefault("Shutdown.message", "The server has been shutdown");
			config.addDefault("Shutdown.defaultTimeout", 30);
			
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
		shutdownMessage = config.getString("Shutdown.message");
		shutdownTimeout = config.getInt("Shutdown.timeout");
		
		File kitConfig = new File(PerkUtils.plugin.getDataFolder() + File.separator + "kits.cfg");
		if (!kitConfig.exists()) {
			createDefaultKitConfig(kitConfig);
		}
		loadKitsConfig(kitConfig);
		
		File pluginBlacklist = new File(PerkUtils.plugin.getDataFolder()+ File.separator + "plugins.cfg");
		if (!pluginBlacklist.exists()) {
			createDefaultPluginBlackList(pluginBlacklist);
		}
		loadPluginBlacklist(pluginBlacklist);
		
		return true;
		
	}
	
	private static void createDefaultPluginBlackList(File pluginBlacklist) {
		PerkUtils.log.info("Creating default kit config.");
		
		try {
			pluginBlacklist.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(pluginBlacklist.getPath()));
			
			writer.write("# plugin blacklist file, please use exact plugin names");
			writer.write("# if you dont the plugin will still be displayed in the /plugins command");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadPluginBlacklist(File pluginBlacklist) {

		if (!pluginBlacklist.exists()) {
			PerkUtils.log.log(Level.SEVERE, "Config file 'plugins.cfg' does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'plugins.cfg'");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pluginBlacklist.getPath()));
			String line = null;
			
			while((line = reader.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				
				if (line.length() == 0)
					continue;
					
				PerkUtils.pluginBlacklist.add(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
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
			
			PerkUtils.DebugConsole("Loaded " + PerkKits.kits.size() + " kits.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
					
	}
}
