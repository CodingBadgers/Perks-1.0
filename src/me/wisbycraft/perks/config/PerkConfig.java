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
import me.wisbycraft.perks.utils.Kit;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;


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
	
	public static void createDefaultKitsConfig(File kitConfig) {
		
		try {
			kitConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(kitConfig.getPath()));

			writer.write("# This is the kits config file");
			writer.write("[kit]\n");
			writer.write("[name = default]\n");
			writer.write("[timeout = 200]\n");
			writer.write("wooden_pick\n");
			writer.write("torch,16\n");
			writer.write("[/kit]\n");

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

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;
				
				if (!line.equalsIgnoreCase("[kit]")) {
					return;
				}
				
				ArrayList<ItemStack> newKit = new ArrayList<ItemStack>();
				String name = null;
				String timeout = null;
				
				while ((line = reader.readLine()) != null) {
					
					if (line.startsWith("[name = ")) {
						name = line.substring(line.indexOf('=') + 1, line.indexOf(']'));
						continue;
					}
					
					if (line.startsWith("[timeout = ")) {
						timeout = line.substring(line.indexOf('=') + 2, line.indexOf(']'));
						continue;
					}
					
					if (line.equalsIgnoreCase("[/kit]"))
						break;
					
					ItemStack item;
					
					if (PerkUtils.isNumeric(line)) {
						
						if (Material.getMaterial(Integer.parseInt(line)) == null) {
							PerkUtils.ErrorConsole("Could not find item with id " + line);
							continue;
						}
						
						item = new ItemStack(Material.getMaterial(Integer.parseInt(line)));				
					} else {
						
						String itemName = line.toUpperCase();
						if (Material.getMaterial(itemName) == null) {
							PerkUtils.ErrorConsole("Could not find item with id " + line);
							continue;
						}
						
						item = new ItemStack(Material.getMaterial(line.toUpperCase()));
					}
					
					newKit.add(item);
				}
				
				Kit kit = new Kit(name, Integer.parseInt(timeout), newKit);
				PerkKits.kits.add(kit);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
}
