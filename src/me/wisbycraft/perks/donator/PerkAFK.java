package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;


public class PerkAFK {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args){
		
		if (commandLabel.equalsIgnoreCase("afk")) {
			
			if (!player.hasPermission("perks.afk", true))
				return true;
			
			if (args.length == 0) {
				
				if (player.isAfk()) {
					
					player.setAfk(false);
					PerkUtils.OutputToPlayer(player, "You are back");
					PerkUtils.OutputToAll(player.getPlayer().getName() + " is back");
				} else {
					
					player.setAfk(true);
					PerkUtils.OutputToPlayer(player, "You are afk.");
					PerkUtils.OutputToAll(player.getPlayer().getName() + " is afk");
				}
				
			}else {
				
				PerkUtils.OutputToPlayer(player, "use /afk");
			}
			
			return true;
		}
		
		return false;
	}
}
