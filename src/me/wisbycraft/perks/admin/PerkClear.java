package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;


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
		
		if (commandLabel.equalsIgnoreCase("colect")) {
			
			if (!player.hasPermission("perks.clear.colecr", true))
				return true;
			
			player.colectInv();
			PerkUtils.OutputToPlayer(player, "Here is your inventory back");
			
			return true;
		}
		return false;
	}
}
