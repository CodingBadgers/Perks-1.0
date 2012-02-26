package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.utils.PerkPlayer;

import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerJoinEvent;

public class PerkVanish {
	
	public static void vanishJoin (PerkPlayer player, PlayerJoinEvent event) {
		if (!player.isHidden()) 
			return;
		
		player.hidePlayer();
		event.setJoinMessage(null);
		
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("vanish")) {
								 
			if (!player.hasPermission("perks.vanish", true))
				return true;
				
			if (player.isHidden()) {
				player.showPlayer();
			} else {
				player.hidePlayer();
			}
			
			return true;
		}
		
		return false;
		
	}
	
	public static void addPlayer(PerkPlayer player) {
		
	}
	
	public static void removePlayer(PerkPlayer player) {
		
		
	}
}
