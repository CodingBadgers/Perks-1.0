package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;


public class PerkClear {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("clear")) {
			
			if (args.length == 0) {
				
				if (!player.hasPermission("perks.clear.own", true))
					return true;
				
				player.clearInv();
			}
			
			if (args.length == 1) {
				
				if (!player.hasPermission("perks.clear.other", true))
					return true;
				
				PerkPlayer target = PerkUtils.getPlayer(args[0]);
				
				if (target == null) {
					PerkUtils.OutputToPlayer(player, "That player is not online");
					return true;
				}
				
				target.clearInv();
			}
			
			return true;
		}
		return false;
	}
}
