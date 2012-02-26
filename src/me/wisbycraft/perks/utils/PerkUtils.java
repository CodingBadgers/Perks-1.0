package me.wisbycraft.perks.utils;

import java.util.logging.Level;
import java.util.logging.Logger;


import me.wisbycraft.perks.Perks;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.dynmap.DynmapAPI;

public class PerkUtils {

	public static final Logger log = Logger.getLogger("minecraft");
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	static public Perks plugin = null;
	static public boolean spoutEnabled = false;
	public static DynmapAPI dynmapapi;

	static public void DebugConsole(String messsage) {
		log.log(Level.INFO, "[Perks] " + messsage + ".");
	}
	
	static public void ErrorConsole(String messsage) {
		log.log(Level.SEVERE, "[Perks] " + messsage + ".");
	}

	static public void OutputToPlayer(PerkPlayer player, String messsage) {
		player.getPlayer().sendMessage("\247b[Perks] \247f" + messsage + ".");
	}
	
	static public void OutputToPlayer(Player player, String messsage) {
		player.sendMessage("\247b[Perks] \247f" + messsage + ".");
	}
	
	static public void OutputToAll(String messsage) {
		plugin.getServer().broadcastMessage("\247b[Perks] \247f" + messsage + ".");
	}

	public static PerkPlayer getPlayer(Player player) {
		if (player == null)
			return null;
		
		return perkPlayers.getPlayer(player);
	}
	
	public static Server server() {
		return plugin.getServer();
	}
		
	static public PerkPlayer getPlayer(String name) {
		return perkPlayers.getPlayer(server().getPlayer(name));
	}
	
	static public boolean isNumeric(String i) {
		try {
			Integer.parseInt(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	
}