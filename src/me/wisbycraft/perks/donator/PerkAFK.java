package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Location;
import org.bukkit.command.Command;


public class PerkAFK {

	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args){
		
		if (commandLabel.equalsIgnoreCase("afk")) {
			
			if (!player.hasPermission("perks.afk", true))
				return true;
			
			if (args.size() == 0) {
				
				if (player.isAfk()) {
					
					player.setAfk(false);
					PerkUtils.OutputToPlayer(player, "You are back");
					PerkUtils.OutputToAll(player.getPlayer().getName() + " is back");
				} else {
					
					
					player.setAfk(true);
                    
                    // if a players y coord is greater than the highest block at there location, lower them
                    double y = player.getPlayer().getWorld().getHighestBlockAt(player.getPlayer().getLocation()).getLocation().getY() + 1;
                    if (player.getPlayer().getLocation().getY() > y) {
                            player.teleport(new Location(player.getPlayer().getLocation().getWorld(),
                                            player.getPlayer().getLocation().getX(), 
                                            y, 
                                            player.getPlayer().getLocation().getZ()));
                    }
                    
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
