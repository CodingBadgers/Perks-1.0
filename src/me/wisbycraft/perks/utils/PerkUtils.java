package me.wisbycraft.perks.utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.wisbycraft.perks.Perks;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.dynmap.DynmapAPI;

import com.onarandombox.MultiverseCore.api.MVWorldManager;

public class PerkUtils {

	public static final Logger log = Logger.getLogger("minecraft");
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	static public Perks plugin = null;
	static public boolean spoutEnabled = false;
	public static DynmapAPI dynmapapi;
	public static MVWorldManager worldManager;
	public static boolean vaultEnabled = false;
	public static ArrayList<OfflinePlayer> blacklist = new ArrayList<OfflinePlayer>();

	static public void DebugConsole(String messsage) {
		log.log(Level.INFO, "[Perks] " + messsage + ".");
	}
	
	static public void ErrorConsole(String messsage) {
		log.log(Level.SEVERE, "[Perks] " + messsage + ".");
	}

	static public void OutputToPlayer(PerkPlayer player, String messsage) {
		player.getPlayer().sendMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + messsage + ".");
	}
	
	static public void OutputToPlayer(Player player, String messsage) {
		player.sendMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + messsage + ".");
	}
	
	static public void OutputToAll(String messsage) {
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + messsage + ".");
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
	
	static public boolean isInt(String i) {
		try {
			Integer.parseInt(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	static public boolean isFloat(String i) {
		try {
			Float.parseFloat(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	static public boolean isDouble(String i) {
		try {
			Double.parseDouble(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	static public boolean isNumeric(String i) {
		return isInt(i) || isFloat(i) || isDouble(i);
	}

	// TODO Still doesn't display the correct time
	public static String parseTime(long timeTillNext) {
		long seconds = timeTillNext;
		long minutes = timeTillNext / 60;
		long hours = timeTillNext / 60 / 60;
		long days = timeTillNext / 60 / 60 / 24;
		
		Math.floor(seconds);
		Math.floor(minutes);
		Math.floor(hours);
		Math.floor(days);
		
		hours = hours - days*24;
		minutes = minutes - (hours*60 + days*24);
		seconds = seconds - (minutes*60 + hours*60 + days*24);
		
		String time = "";
		if (days > 1) 
			time += days + " day" + (days == 1 ? "" : "s") + " ";
		if (hours > 1)
			time += hours + " hour"  + (hours == 1 ? "" : "s" ) + " ";
		if (minutes > 1)
			time += minutes + " minute"  + (minutes == 1 ? "" : "s") + " ";
		time += seconds + " second"  + (seconds == 1 ? "" : "s");
		
		return time;
	}
	
	public static String getUsage(Command cmd) {
		return cmd.getUsage().replace("<command>", cmd.getName());
	}

}