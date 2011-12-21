package me.wisbycraft.perks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PerkUtils {

	private static final Logger log = Logger.getLogger("minecraft");
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	static public Perks plugin = null;
	
	static public void DebugConsole(String messsage) {
		log.log(Level.INFO, "[Perks] " + messsage + ".");
	}
	
	static public void ErrorConsole(String messsage) {
		log.log(Level.SEVERE, "[Perks] " + messsage + ".");
	}

	static public void OutputToPlayer(PerkPlayer player, String messsage) {
		player.getPlayer().sendMessage("\247b[Perks] \247f" + messsage + ".");
	}

	static PerkPlayer getPlayer(Player player) {
		return perkPlayers.getPlayer(player);
	}
	
	static Server server() {
		return plugin.getServer();
	}
}