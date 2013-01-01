package uk.codingbadgers.perks.donator;

import org.bukkit.Location;
import org.bukkit.command.Command;

import uk.codingbadgers.perks.config.DatabaseManager;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;
import uk.codingbadgers.perks.utils.PerkWorldSpawn;


public class PerkSpawn {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("spawn")) {
			
			if (!player.hasPermission("perks.spawn.use", true))
				return true;
			
			PerkWorldSpawn spawn = DatabaseManager.getSpawn(player.getPlayer().getLocation().getWorld());
			
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
			
			target.teleport(spawn.getSpawn());
			PerkUtils.OutputToPlayer(target, "Teleported to spawn of " + spawn.getWorld().getName());
			if (target != player) 
				PerkUtils.OutputToPlayer(player, "You have teleported " + target.getPlayer().getName() + " to spawn");
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("setspawn")) {
			
			if (!player.hasPermission("perks.spawn.set", true))
				return true;
			
			Location loc = player.getPlayer().getLocation();
			
			PerkWorldSpawn spawn = new PerkWorldSpawn(loc.getWorld(), loc);
			DatabaseManager.setSpawn(spawn);
			
			PerkUtils.OutputToPlayer(player, "Spawn for " + spawn.getWorld().getName() + " has been set here");
			return true;
		}

		return false;
	}
}
