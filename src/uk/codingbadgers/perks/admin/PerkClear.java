package uk.codingbadgers.perks.admin;


import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkClear {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("clear")) {
			
			boolean all = false;
			if (args.hasFlag("a"))
				all = true;
			if (args.size()== 0) {
				
				if (!player.hasPermission("perks.clear.own", true))
					return true;

				player.clearInv(all);

				PerkUtils.OutputToPlayer(player, "Your inventory has been cleared.");
			}
			
			if (args.size() == 1) {
				
				if (!player.hasPermission("perks.clear.other", true))
					return true;
				
				PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
				
				if (target == null) {
					PerkUtils.OutputToPlayer(player, "That player is not online");
					return true;
				}
				
				target.clearInv(all);
				PerkUtils.OutputToPlayer(player, "You have cleared " + target.getPlayer().getName() + "'s inventory.");
				PerkUtils.OutputToPlayer(target, "Your inventory has been cleared by " + player.getPlayer().getName());
			}
			
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("collect")) {
			
			if (!player.hasPermission("perks.clear.collect", true))
				return true;
			
			player.colectInv();
			PerkUtils.OutputToPlayer(player, "Here is your inventory back");
			
			return true;
		}
		return false;
	}
}
