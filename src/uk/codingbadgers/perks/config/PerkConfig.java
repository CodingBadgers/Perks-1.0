package uk.codingbadgers.perks.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import uk.codingbadgers.perks.admin.PerkTroll;
import uk.codingbadgers.perks.donator.PerkKits;
import uk.codingbadgers.perks.utils.PerkKit;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	public static String shutdownMessage;
	public static int shutdownTimeout;
	public static int forceJoinCutOff;
	
	public static ArrayList<String> perksInfo = new ArrayList<String>();
	
	public static class DatabaseSettings {
		public String name;
		public String user;
		public String ip;
		public String password;
		public int port;
		
		public DatabaseSettings() {}
	}
	
	public static DatabaseSettings DATABASE = new DatabaseSettings();
	public static short waterDamageAmount;
	
	public static boolean loadConfig () {
		
		PerkKits.kits.clear();
		
		FileConfiguration config = PerkUtils.plugin.getConfig();
		
		try {
			config.addDefault("Capes.Url", "");
			config.addDefault("Hunger.rate", "0.25");
			config.addDefault("Hunger.counter", "0.0");
			config.addDefault("Shutdown.message", "The server has been shutdown");
			config.addDefault("Shutdown.defaultTimeout", 30);
			config.addDefault("ForceJoin.CutOff", 100);
			config.addDefault("Water.damageAmount", 1);
			
			// database config
			config.addDefault("database.name", "database");
			config.addDefault("database.username", "username");
			config.addDefault("database.password", "password");
			config.addDefault("database.ip", "127.0.0.1");
			config.addDefault("database.port", 3306);
			
			config.options().copyDefaults(true);
			PerkUtils.plugin.saveConfig();
		} catch (Exception ex) {
	    	
			ex.printStackTrace();
			return false;
		
		}
 		
		capesURL = config.getString("Capes.Url", "");
		hungerCounter = Float.parseFloat(config.getString("Hunger.counter", "0.25"));
		hungerRate = Float.parseFloat(config.getString("Hunger.rate", "0.0"));
		shutdownMessage = config.getString("Shutdown.message", "The server has been shutdown");
		shutdownTimeout = config.getInt("Shutdown.defaultTimeout", 30);
		forceJoinCutOff = config.getInt("ForceJoin.CutOff", 100);
		waterDamageAmount = (short)config.getInt("Water.damageAmount", 1);
		
		DATABASE.name = config.getString("database.name", "database");
		DATABASE.user = config.getString("database.username", "username");
		DATABASE.password = config.getString("database.password", "password");
		DATABASE.ip = config.getString("database.ip", "127.0.0.1");
		DATABASE.port = config.getInt("database.port", 3306);
		
		// load kit config
		File kitConfig = new File(PerkUtils.plugin.getDataFolder() + File.separator + "kits.cfg");
		if (!kitConfig.exists()) {
			createDefaultKitConfig(kitConfig);
		}
		loadKitsConfig(kitConfig);
		
		// load plugin blacklist
		File pluginBlacklist = new File(PerkUtils.plugin.getDataFolder()+ File.separator + "plugins.cfg");
		if (!pluginBlacklist.exists()) {
			createDefaultPluginBlackList(pluginBlacklist);
		}
		loadPluginBlacklist(pluginBlacklist);
		
		// load command blacklist
		File commandBlacklist = new File(PerkUtils.plugin.getDataFolder() + File.separator + "commands.cfg");
		if (!commandBlacklist.exists()) {
			createDefaultCommandBlackList(commandBlacklist);
		}
		loadCommandBlackList(commandBlacklist);
		
		// load command blacklist
		File perksHelpConfig = new File(PerkUtils.plugin.getDataFolder() + File.separator + "perksHelp.cfg");
		if (!perksHelpConfig.exists()) {
			createPerksHelpConfig(perksHelpConfig);
		}
		loadPerksHelpConfig(perksHelpConfig);
	
		return true;
		
	}
	
	private static void loadPerksHelpConfig(File perksHelpConfig) {
		if (!perksHelpConfig.exists()) {
			PerkUtils.log.log(Level.SEVERE, "Config file 'perksHelp.cfg' does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'perksHelp.cfg'");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(perksHelpConfig.getPath()));
			String line = null;
			
			while((line = reader.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				
				if (line.length() == 0)
					continue;
					
				perksInfo.add(line);
			}
			
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void createPerksHelpConfig(File perksHelpConfig) {
		PerkUtils.log.info("Creating default command whitelist");
		
		try {
			perksHelpConfig.createNewFile();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(perksHelpConfig.getPath()));
			
			writer.write("# Message to be printed out whenever a player uses /perks\n");
			writer.write("1 - /fly - lets you fly like a bird\n");
			writer.write("2 - /tpr <name> - Sends a teleport request to a player\n");
			writer.write("3 - Unlimited air under water when wearing a gold helmet\n");
			writer.write("4 - You're hunger decreases at a much slower rate\n");
			writer.write("5 - /death - to teleport to your last death location\n");
			
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}

	private static void loadCommandBlackList(File commandBlacklist) {
		
		if (!commandBlacklist.exists()) {
			PerkUtils.log.log(Level.SEVERE, "Config file 'commands.cfg' does not exist");
			return;
		}

		PerkUtils.log.log(Level.INFO, "Loading config file 'commands.cfg'");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(commandBlacklist.getPath()));
			String line = null;
			
			while((line = reader.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				
				if (line.length() == 0)
					continue;
					
				PerkTroll.commandBlacklist.add(line);
			}
			
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void createDefaultCommandBlackList(File commandBlacklist) {
		PerkUtils.log.info("Creating default command whitelist");
		
		try {
			commandBlacklist.createNewFile();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(commandBlacklist.getPath()));
		
			writer.write("# command whitelist file, please use exact command names and aliases");
			writer.write("# these commands are only whitelisted for /sendCommand");
		
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		
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
			
			reader.close();
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
			
			reader.close();
			
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
