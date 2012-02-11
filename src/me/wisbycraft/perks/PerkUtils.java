package me.wisbycraft.perks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PerkUtils {

	public static final Logger log = Logger.getLogger("minecraft");
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	static public Perks plugin = null;
	static public boolean spoutEnabled = false;
	
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

	static PerkPlayer getPlayer(Player player) {
		if (player == null)
			return null;
		
		return perkPlayers.getPlayer(player);
	}
	
	static Server server() {
		return plugin.getServer();
	}
	
	static Player getPlayer(String name) {
		return plugin.getServer().getPlayer(name);
	}
}