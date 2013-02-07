package uk.codingbadgers.perks.donator;

import org.bukkit.World;
import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkHomeAndBuild {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (cmd.getName().equalsIgnoreCase("home")) {

			if (!player.hasPermission("perks.home", true))
				return true;
			
			if (args.size() == 0) {
				player.gotoHome();
			} else if (args.size() == 1) {
				World world = getWorld(args.getString(0));
				if (world == null) {
					PerkUtils.OutputToPlayer(player, "Sorry that world does not exist");
					return true;
				}
				player.gotoHome(world);
			} else {
				PerkUtils.OutputToPlayer(player, cmd.getUsage());
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
			
			player.gotoBuild();
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

	private static World getWorld(String string) {
		for (World world : PerkUtils.server().getWorlds()) {
			if (world.getName().contains(string))
				return world;
		}
		return null;
	}

}
