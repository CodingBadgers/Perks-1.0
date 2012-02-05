package me.wisbycraft.perks;

import org.bukkit.command.Command;

public class PerkTeleport {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {

		PerkPlayer toPlayer = null;
		
		if (args.length == 1) {
			String playerName = args[0];
			toPlayer = PerkUtils.getPlayer(PerkUtils.server().getPlayer(playerName));
			
			if (toPlayer == null) {
				PerkUtils.OutputToPlayer(player, playerName + " isn't online.");
				return false;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("tpr")) {
			
			if (args.length != 1) {
				PerkUtils.OutputToPlayer(player, "In correct usage of command");
				return true;
			}
			
			if (!player.hasPermission("perks.teleport.tpr", true)) {
				return true;
			}
			
			toPlayer.sendTpRequest(player);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tphr")) {
			
			if (args.length != 1) {
				PerkUtils.OutputToPlayer(player, "In correct usage of command");
				return true;
			}
			
			if (!player.hasPermission("perks.teleport.tphr", true)) {
				return true;
			}
			
			toPlayer.sendTpHereRequest(player);
			
			return true;
		}
		
		// accepting and declining shouldn't have a permission as everyone needs to do it
		
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			
			player.acceptTpRequest(toPlayer);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpd")) {
			
			player.declineTpRequest(toPlayer);
			
			return true;
		}

		return false;
	}
	
}
