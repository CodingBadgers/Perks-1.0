package me.wisbycraft.perks;

import org.bukkit.command.Command;

public class PerkHomeAndBuild {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("home")) {

			if (!player.hasPermission("perks.home", true))
				return true;
			
			DatabaseManager.gotoHome(player.getPlayer());
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("sethome")) {

			if (!player.hasPermission("perks.home", true))
				return true;
			
			player.setHomeLocation(null, true);
			return true;
		}
			
		if (cmd.getName().equalsIgnoreCase("build")) {

			if (!player.hasPermission("perks.build", true))
				return true;
			
			DatabaseManager.gotoBuild(player.getPlayer());
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("setbuild")) {

			if (!player.hasPermission("perks.build", true))
				return true;
			
			player.setBuildLocation(null, true);
			return true;
		}
		
		return false;
		
	}

}
