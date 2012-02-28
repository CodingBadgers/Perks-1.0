package me.wisbycraft.perks.donator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;

import me.wisbycraft.perks.utils.PerkPlayer;

public class PerkSpawn {

	public static void teleportSpawn(PerkPlayer player, World world) {
		player.getPlayer().teleport(world.getSpawnLocation());
	}
	
	public static void setSpawn(Location loc) {
		World world = loc.getWorld();
		
		world.setSpawnLocation((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("spawn")) {
			
			if (!player.hasPermission("perks.spawn", true))
				return true;
			
			teleportSpawn (player, player.getPlayer().getWorld());
			return true;
		}
		
		return false;
	}
}
