package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PerkVanish {
	
	public static void vanishJoin (PerkPlayer player, PlayerJoinEvent event) {
		if (player.hasPermission("perks.vanish.view", false))
			return;
		
		Player[] players = PerkUtils.server().getOnlinePlayers();
		for (int i = 0; i < players.length; ++i)
		{
			PerkPlayer p = PerkUtils.getPlayer(players[i]);
			
			if (p == player)
				continue;
			
			if (p.isHidden()) {
				player.getPlayer().hidePlayer(p.getPlayer());
			}			
		}
		
		/*
		if (DatabaseManager.isVanished(player)) {
			player.hidePlayer();			
		}
		
		if (player.isHidden()) {
			PerkUtils.OutputToPlayer(player, "You are still hidden, well done me for making this FUCKING WORK");
		}
		*/
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("vanish")) {
								 
			if (!player.hasPermission("perks.vanish", true))
				return true;
			
			if (player.isBlacklisted(true))
				return true;
				
			if (player.isHidden()) {
				
				player.showPlayer(true);
				removePlayer(player);
			} else {
				
				player.hidePlayer(true);
				addPlayer(player);
			}
			
			return true;
		}
		
		return false;
		
	}
	
	/* Not needed yet, only when we log who is in vanish */
	
	public static void addPlayer(PerkPlayer player) {
		DatabaseManager.addVanishPlayer(player);
	}
	
	public static void removePlayer(PerkPlayer player) {
		DatabaseManager.removeVanishPlayer(player);
	}

}
