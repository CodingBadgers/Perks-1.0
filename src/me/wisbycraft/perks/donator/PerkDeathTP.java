package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.utils.PerkMobArena;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.event.entity.EntityDeathEvent;


public class PerkDeathTP {
	
	public static void OnDeath(PerkPlayer player, EntityDeathEvent event) {
	
		if (!player.hasPermission("perks.deathtp", false))
			return;
		
		if (PerkMobArena.maHandler != null && (PerkMobArena.maHandler.isPlayerInArena(player.getPlayer()) || PerkMobArena.maHandler.inRegion(player.getPlayer().getLocation())))
			return;
		
		Location deathLocation = player.getPlayer().getLocation();
		
		player.addDeathLocation(deathLocation);
		
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("death")) {

			if (!player.hasPermission("perks.deathtp", true))
				return true;
			
			if (player.canDeathTP()) {
				Location deathloc = player.getDeathLocation();
				
				if (PerkMobArena.maHandler != null && PerkMobArena.maHandler.inRegion(deathloc)) {
					PerkUtils.OutputToPlayer(player, "Sorry you can't deathtp into the arena");
					return true;
				}
				
				player.getPlayer().teleport(deathloc);
				player.resetDeath();
				PerkUtils.OutputToPlayer(player, "You have been teleported to your last death point");
			}			
			return true;
		}
				
		return false;
		
	}

}