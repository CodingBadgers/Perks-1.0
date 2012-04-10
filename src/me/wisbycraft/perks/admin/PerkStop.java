package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkStop {
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("stop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player)sender).hasPermission("perks.serveradmin.stop", true))
					return true;
			}
			
			/*
			int time = 10;
			if (args.length == 1) {
				try {
				time = Integer.parseInt(args[0]);
				} catch(Exception ex) {
					if (sender instanceof Player)
						PerkUtils.OutputToPlayer((Player)sender, "Invalid timeout '" + args[0] + "'");
					else
						PerkUtils.ErrorConsole("Invalid timeout '" + args[0] + "'");
					return true;
				}
			}
			*/
			
			// needs moving to own thread
			/*
			PerkUtils.OutputToAll("The Server will shutdown in " + time + " seconds...");
						
			while (time > 0) {
				try {
					Thread.sleep(1000);
					time--;
					if (time <= 5 && time != 0) {
						PerkUtils.OutputToAll("The Server will shutdown in " + time + " seconds...");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			*/
			
			PerkUtils.server().shutdown();
			return true;
		}
		
		return false;
	}

}
