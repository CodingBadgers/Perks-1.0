package me.wisbycraft.perks.donator;

import org.bukkit.Location;
import org.bukkit.command.Command;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkSpawn {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("spawn")) {
			
			if (!player.hasPermission("perks.spawn", true))
				return true;
			
			Location spawn = null;
			
			if (PerkUtils.worldManager != null) {
				MultiverseWorld world = PerkUtils.worldManager.getMVWorld(player.getPlayer().getWorld());
				spawn = world.getSpawnLocation();
			} else {
				spawn = player.getPlayer().getWorld().getSpawnLocation();
			}
			
			if (spawn == null) {
				PerkUtils.OutputToPlayer(player, "An error has occured, please tell staff");
				return true;
			}
			
			player.teleport(spawn);
			PerkUtils.OutputToPlayer(player, "Teleported to spawn");
			return true;
		}

		return false;
	}
}
