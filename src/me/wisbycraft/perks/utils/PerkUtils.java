package me.wisbycraft.perks.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.wisbycraft.perks.Perks;

import org.bukkit.ChatColor;
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
	public static ArrayList<PerkWebChatPlayer> webChatPlayers = new ArrayList<PerkWebChatPlayer>();

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
		long elapsedTime = timeTillNext;  
	    String format = String.format("%%0%dd", 2);  
	    String seconds = String.format(format, elapsedTime % 60);  
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);  
	    String hours = String.format(format, elapsedTime / 3600);  
	    String time =  hours + ":" + minutes + ":" + seconds;  
	    return time;  
	}
	
	public static String parseDate(long time)throws Exception {
		   SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy ");
	 
		   Date resultdate = new Date(time);
		   return sdf.format(resultdate);
	} 
	
	public static String getUsage(Command cmd) {
		return cmd.getUsage().replace("<command>", cmd.getName());
	}

	public static void shutdownServer() {
		plugin.getServer().shutdown();
	}

}