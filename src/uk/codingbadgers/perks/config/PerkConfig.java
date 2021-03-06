package uk.codingbadgers.perks.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import uk.codingbadgers.perks.admin.PerkTroll;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkConfig {
	
	public static float hungerCounter;
	public static float hungerRate;
	public static String capesURL;
	public static int capeRefresh;
	public static String shutdownMessage;
	public static int shutdownTimeout;
	public static int forceJoinCutOff;
	public static short waterDamageAmount;
	public static boolean enderPearlTeleport;
	public static long homeWarmUpTime;
	public static long buildWarmUpTime;
	public static long spawnWarmUpTime;
	public static long deathTpDelay;
	public static int afkKickTime;
	
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
	private static FileConfiguration config = null;
	
	public static boolean loadConfig () {
		
		config = PerkUtils.plugin.getConfig();
		
		try {
			config.addDefault("Server.pvp", false);
			config.addDefault("Capes.Url", "");
			config.addDefault("Hunger.rate", "0.25");
			config.addDefault("Hunger.counter", "0.0");
			config.addDefault("Shutdown.message", "The server has been shutdown");
			config.addDefault("Shutdown.defaultTimeout", 30);
			config.addDefault("ForceJoin.CutOff", 100);
			config.addDefault("Water.damageAmount", 1);
			config.addDefault("Teleport.enderPeals", true);
			config.addDefault("Teleport.home.warmUp", 10);
			config.addDefault("Teleport.build.warmUp", 10);
			config.addDefault("Teleport.spawn.warmUp", 10);
			config.addDefault("Teleport.death.dealay", 10);
			
			// database config
			config.addDefault("database.name", "database");
			config.addDefault("database.username", "username");
			config.addDefault("database.password", "password");
			config.addDefault("database.ip", "127.0.0.1");
			config.addDefault("database.port", 3306);
			
			config.addDefault("dependency.vault", true);
			config.addDefault("dependency.dynmap", true);
			config.addDefault("dependency.mobarena", true);
			config.addDefault("dependency.pvparena", true);
			config.addDefault("dependency.herochat", true);
			config.addDefault("dependency.disguisecraft", true);
			config.addDefault("dependency.survivalgames", true);
			
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
		enderPearlTeleport = config.getBoolean("Teleport.enderPeals", true);
		homeWarmUpTime = config.getInt("Teleport.home.warmUp", 10);
		buildWarmUpTime = config.getInt("Teleport.build.warmUp", 10);
		spawnWarmUpTime = config.getInt("Teleport.spawn.warmUp", 10);
		deathTpDelay = config.getInt("Teleport.death.dealay", 10);
		afkKickTime = config.getInt("Afk.KickTimeMinutes", 10);
		
		DATABASE.name = config.getString("database.name", "database");
		DATABASE.user = config.getString("database.username", "username");
		DATABASE.password = config.getString("database.password", "password");
		DATABASE.ip = config.getString("database.ip", "127.0.0.1");
		DATABASE.port = config.getInt("database.port", 3306);
		
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
	
	public static boolean isPvpServer() {
		return config.getBoolean("Server.pvp", false);
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
	
	public static boolean isDependencyEnabled(String plugin) {
		return config.getBoolean("dependency." + plugin);
	}
}
