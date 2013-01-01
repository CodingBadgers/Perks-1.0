package uk.codingbadgers.perks.donator;


import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;


public class PerkFlying {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
				
		// toggles fly mode
		if (cmd.getName().equalsIgnoreCase("fly") || cmd.getName().equalsIgnoreCase("mc")) {
			
			// all the following commands require this permission
			if (!player.hasPermission("perks.fly", true))
				return true;
			
			if (player.isFlying()) {
				player.setFlying(false);
			} else {
				player.setFlying(true);
			}
			
			return true;
		}
		
		// turns fly off, for jail mainly
		if (cmd.getName().equalsIgnoreCase("land")) {
			
			if (!player.hasPermission("perks.fly", true))
				return true;
			
			player.setFlying(false);
			
			return true;
		}
		return false;
	}
}
