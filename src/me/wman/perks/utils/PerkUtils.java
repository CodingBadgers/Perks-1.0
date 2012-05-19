package me.wman.perks.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.wman.perks.Perks;
import me.wman.perks.config.PerkConfig;

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
	public static DynmapAPI dynmapapi = null;
	public static MVWorldManager worldManager;
	public static boolean vaultEnabled = false;
	public static ArrayList<PerkWebChatPlayer> webChatPlayers = new ArrayList<PerkWebChatPlayer>();
	public static ArrayList<String> pluginBlacklist = new ArrayList<String>();
	
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

	public static String parseTime(long timeTillNext) {
		long elapsedTime = timeTillNext;  
	    String format = String.format("%%0%dd", 2);  
	    String seconds = String.format(format, elapsedTime % 60);  
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);  
	    String hours = String.format(format, elapsedTime / 3600);  
	    String time = "";
	    
	    boolean showHours = !hours.equalsIgnoreCase("00");
	    boolean showMinutes = showHours || !minutes.equalsIgnoreCase("00");
	    
	    time += showHours ? hours + " hours, " : "";
	    time += showMinutes ? minutes + " minutes and " : "";
	    time += seconds + " seconds";  
	    return time;  
	}
	
	// parse the date
	public static String parseDate(long time)throws Exception {
		   SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy ");
	 
		   Date resultdate = new Date(time);
		   return sdf.format(resultdate);
	} 
	
	// get the usage of the command for the player's benifit
	public static String getUsage(Command cmd) {
		return cmd.getUsage().replace("<command>", cmd.getName());
	}

	// shutdown and kick all players with the shutdown message
	public synchronized static void shutdownServer() {
		
		try {
			Player[] players = plugin.getServer().getOnlinePlayers();
			for (int i = 0; i < players.length; ++i) {
				players[i].kickPlayer(PerkConfig.shutdownMessage);
			}
		} catch (Exception ex) {
			// this try is to just stop spamming, if it fails, it will just send the default minecraft java exception to the client
		}
		
		plugin.getServer().shutdown();
	}

}