package me.wisbycraft.perks;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.event.entity.EntityDeathEvent;

public class PerkDeathTP {
	
	public static void OnDeath(PerkPlayer player, EntityDeathEvent event) {
	
		if (!player.hasPermission("perks.deathtp", false))
			return;
		
		Location deathLocation = player.getPlayer().getLocation();
		
		player.addDeathLocation(deathLocation);
		
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("death")) {

			if (!player.hasPermission("perks.deathtp", true))
				return true;
			
			if (player.canDeathTP()) {
				player.getPlayer().teleport(player.getDeathLocation());
				player.resetDeath();
				PerkUtils.OutputToPlayer(player, "You have been teleported to your last death point");
				return true;
			}			
		}
				
		return false;
		
	}

}
