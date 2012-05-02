package me.wman.perks.donator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import me.wman.perks.utils.PerkArgSet;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

public class PerkSpawn {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("spawn")) {
			
			if (!player.hasPermission("perks.spawn.use", true))
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
		
		if (commandLabel.equalsIgnoreCase("setspawn")) {
			
			if (!player.hasPermission("perks.spawn.set", true))
				return true;
			
			Location spawn = player.getPlayer().getLocation();
			
			if (PerkUtils.worldManager != null) {
				
				MultiverseWorld world = PerkUtils.worldManager.getMVWorld(spawn.getWorld());
				
				world.setSpawnLocation(spawn);
			} else {
				World world = spawn.getWorld();
				
				world.setSpawnLocation((int)spawn.getX(), (int)spawn.getY(), (int)spawn.getZ());
			}
			
			PerkUtils.OutputToPlayer(player, "Spawn for " + spawn.getWorld().getName() + " has been set here");
			return true;
		}

		return false;
	}
}
