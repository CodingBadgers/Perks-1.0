package me.wisbycraft.perks.donator;

import org.bukkit.Location;
import org.bukkit.command.Command;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkSpawn {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
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
			
			PerkPlayer target;
			if (args.size() == 0) {
				target = player;
			} else {
				if (!player.hasPermission("perks.spawn.other", true))
					return true;
				
				target = PerkUtils.getPlayer(args.getString(0));
			}
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "Sorry that player is not online");
			}
			
			target.teleport(spawn);
			PerkUtils.OutputToPlayer(target, "Teleported to spawn");
			return true;
		}

		return false;
	}
}
