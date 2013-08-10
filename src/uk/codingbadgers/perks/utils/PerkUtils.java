package uk.codingbadgers.perks.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.slipcor.pvparena.PVPArena;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.dynmap.DynmapAPI;

import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

import uk.codingbadgers.perks.Perks;
import uk.codingbadgers.perks.config.PerkConfig;

public class PerkUtils {

	public static final Logger log = Logger.getLogger("minecraft");
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	static public Perks plugin = null;
	public static DynmapAPI dynmapapi = null;
	public static PVPArena pvparena = null;
	public static boolean vaultEnabled = false;
	public static ArrayList<String> pluginBlacklist = new ArrayList<String>();
	public static DisguiseCraftAPI disguiseCraftApi = null;
	
	static public void DebugConsole(String message) {
		log.log(Level.INFO, "[Perks] " + message + ".");
	}
	
	static public void ErrorConsole(String message) {
		log.log(Level.SEVERE, "[Perks] " + message + ".");
	}

	static public void OutputToPlayer(PerkPlayer player, String message) {
		player.getPlayer().sendMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + message + ".");
	}
	
	static public void OutputToPlayer(Player player, String message) {
		player.sendMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + message + ".");
	}
	
	static public void OutputToAll(String message) {
		plugin.getServer().broadcastMessage(ChatColor.AQUA + "[Perks] " + ChatColor.RESET + message + ".");
	}
	
	static public void OutputToAllExcluding(String message, Player player) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p != player) {
				OutputToPlayer(p, message);
			}
		}
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
	
	static private boolean isInt(String i) {
		try {
			Integer.parseInt(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	static private boolean isFloat(String i) {
		try {
			Float.parseFloat(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}

	static private boolean isDouble(String i) {
		try {
			Double.parseDouble(i);
		} catch(NumberFormatException ex) {
			return false;
		}
		
		return true;
	}
	
	// check if a number i numeric or not
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
	    boolean showMinutes = !minutes.equalsIgnoreCase("00");
	    boolean showSeconds = !seconds.equalsIgnoreCase("00");

	    final String hoursStr = Integer.parseInt(hours) == 1 ? "hour" : "hours";
	    final String minutesStr = Integer.parseInt(minutes) == 1 ? "minute" : "minutes";
	    final String secondsStr = Integer.parseInt(seconds) == 1 ? "second" : "seconds";
	    
	    time += showHours ? hours + " " + hoursStr + ", " : "";
	    time += showMinutes ? minutes + (showSeconds ? " " + minutesStr + " and " : " " + minutesStr) : "";
	    time += showSeconds ? seconds + " " + secondsStr : "";  
	    return time;  
	}
	
	// parse the date
	public static String parseDate(long time)throws Exception {
		   SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
	 
		   Date resultdate = new Date(time);
		   return sdf.format(resultdate);
	} 
	
	// get the usage of the command for the player's benifit
	public static String getUsage(Command cmd) {
		return cmd.getUsage().replace("<command>", cmd.getName());
	}

	// shutdown and kick all players with the shutdown message
	public synchronized static void shutdownServer() {
		
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ma force end");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "pa castlewars forcestop");
				
		// force the worlds to save just incase it didn't
		for (World world : plugin.getServer().getWorlds()) {
			world.save();
		}
		
		Player[] players = server().getOnlinePlayers();
		for (Player pl : players) {
			try {
				pl.kickPlayer(PerkConfig.shutdownMessage);
			} catch (Exception ex) {
			}
		}
		
		PerkUtils.server().getScheduler().scheduleSyncDelayedTask(PerkUtils.plugin, new Runnable() {
			   public void run() {
				   plugin.getServer().shutdown();
			   }
		}, 2L);
		
	}

	public static void OutputToStaff(String string) {
		
		PerkPlayerArray players = perkPlayers;
		
		for (PerkPlayer player : players) {
			if (player.hasPermission("perks.vanish.showJoin", false))
				player.getPlayer().sendMessage(string);
		}
	}

}