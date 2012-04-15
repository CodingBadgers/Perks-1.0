package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.World;
import org.bukkit.command.Command;


public class PerkHomeAndBuild {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (cmd.getName().equalsIgnoreCase("home")) {

			if (!player.hasPermission("perks.home", true))
				return true;
			
			if (args.size() == 0) {
				DatabaseManager.gotoHome(player.getPlayer());
			} else if (args.size() == 1) {
				World world = PerkUtils.server().getWorld(args.getString(0));
				if (world == null) {
					PerkUtils.OutputToPlayer(player, "Sorry that world does not exist");
					return true;
				}
				DatabaseManager.gotoHome(player.getPlayer(), world);
			} else {
				PerkUtils.OutputToPlayer(player, "use /home [world]");
			}
			
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
