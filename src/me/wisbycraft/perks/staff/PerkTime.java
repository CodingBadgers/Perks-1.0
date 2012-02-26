package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;

public class PerkTime {

	public static void setTime(World world, long time) {
		world.setTime(time);
		PerkUtils.OutputToAll("the time is now " + world.getTime());
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("time")) {
			
			if (!player.hasPermission("perks.time", true))
				return true;
			
			if (args.length == 1 ) {
				
				long time = 0;
				
				try {
					
					time = Long.parseLong(args[0]);
				} catch (NumberFormatException ex) {
					
					PerkUtils.OutputToPlayer(player, "That time is invalid.");
					return true;
				}
				
				setTime(player.getPlayer().getWorld(), time);
				return true;
				
			} else if (args.length == 2) {
				
				long time = 0;
				
				try {
					
					time = Long.parseLong(args[0]);
				} catch (NumberFormatException ex) {
					
					PerkUtils.OutputToPlayer(player, "That time is invalid.");
					return true;
				}
				
				World world = PerkUtils.server().getWorld(args[1]);
				
				if (world == null) {
					PerkUtils.OutputToPlayer(player, "Could not find world " + args[1]);
					return true;
				}
				
				setTime(world, time);
				return true;
			
			} else {
				
				PerkUtils.OutputToPlayer(player, ChatColor.RED + "/time <time> [world]");
				return true;
			}
		}
		
		return false;
	}
	
}
