package me.wisbycraft.perks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PerkVanish {

	public static List<Player> invisible;
	
	public static void vanishJoin (PerkPlayer player, PlayerJoinEvent event) {
		if (!player.isHidden()) 
			return;
		
		player.hidePlayer();
		event.setJoinMessage(null);
		
	}
	
	public static void vanishBlockBreak(PerkPlayer player, BlockBreakEvent event) {
		if (!player.isHidden())
			return;
		
		PerkUtils.OutputToPlayer(player, "You cant break blocks while vanished");
		event.setCancelled(true);
		
	}
	
	public static void vanishBlockPlace(PerkPlayer player, BlockPlaceEvent event) {
		if (!player.isHidden())
		return;
	
		PerkUtils.OutputToPlayer(player, "You cant place blocks while vanished");
		event.setCancelled(true);
	}
	
	public static void vanishPlayerItemPickup(PerkPlayer player, PlayerPickupItemEvent event) {
		if (!player.isHidden())
			return;
		
		PerkUtils.OutputToPlayer(player, "You cant pick up items while vanished");
		event.setCancelled(true);
	}
	
	public static void vanishPlayerQuit (PerkPlayer player, PlayerQuitEvent event) {
		
		if (!player.isHidden())
			return;
		
		event.setQuitMessage(null);
	}
	
	public static void vanishPlayerKick (PerkPlayer player, PlayerKickEvent event) {
		
		if (!player.isHidden())
			return;
		
		event.setLeaveMessage(null);
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("vanish")) {
			 
			if (player.hasPermission("perks.vanish", true))
				return true;
			
			if (player.isHidden()) {
				
				player.showPlayer();
				removePlayer(player);
			} else if (!player.isHidden()) {
				
				player.hidePlayer();
				addPlayer(player);
			}
			
			return true;
		}
		
		return false;
		
	}
	
	public static void addPlayer(PerkPlayer player) {
		
		invisible.add(player.getPlayer());
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					PerkConfig.vanishConfig.getPath(), true));

			writer.write(player.getPlayer().getName());

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void removePlayer(PerkPlayer player) {
		
		invisible.remove(player.getPlayer());
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					PerkConfig.vanishConfig.getPath()));

			for (int i = 0; i < invisible.size(); i++) {
				writer.write(invisible.get(i).getName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
