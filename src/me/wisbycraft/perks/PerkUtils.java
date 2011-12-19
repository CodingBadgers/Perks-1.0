package me.wisbycraft.perks;

import org.bukkit.entity.Player;

public class PerkUtils {

        static public void DebugConsole(String messsage) {
                System.out.println("[Perks] " + messsage + ".");
        }
	static public PerkPlayerArray perkPlayers = new PerkPlayerArray();
	

        static public void OutputToPlayer(PerkPlayer player, String messsage) {
                player.getPlayer().sendMessage("\247b[Perks] \247f" + messsage + ".");
        }
	
	static PerkPlayer getPlayer(Player player) {
		return perkPlayers.getPlayer(player);
	}
}