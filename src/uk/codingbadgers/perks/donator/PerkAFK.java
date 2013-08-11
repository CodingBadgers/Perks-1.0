package uk.codingbadgers.perks.donator;

import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkAFK {
		
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args){
		
		if (commandLabel.equalsIgnoreCase("afk")) {
			
			if (!player.hasPermission("perks.afk", true))
				return true;
			
			if (args.size() == 0) {
				
				if (player.isAfk()) {
					
					player.setAfk(false);
					PerkUtils.OutputToPlayer(player, "You are back");
					PerkUtils.OutputToAllExcluding(player.getPlayer().getName() + " is back", player.getPlayer());
					
				} else {
					
					
					player.setAfk(true);
                                        
                    PerkUtils.OutputToPlayer(player, "You are afk.");
                    PerkUtils.OutputToAllExcluding(player.getPlayer().getName() + " is afk", player.getPlayer());

				}
				
			}else {
				
				PerkUtils.OutputToPlayer(player, "use /afk");
			}
			
			return true;
		}
		
		return false;
	}
}
