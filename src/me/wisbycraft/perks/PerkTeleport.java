package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PerkTeleport {

	public static void Teleport(PerkPlayer player, PlayerTeleportEvent event) {
		
		
	}

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {

		if (args.length != 1) {
			PerkUtils.OutputToPlayer(player, "In correct usage of command");
			return true;
		}
		
		String playerName = args[0];
		PerkPlayer toPlayer = PerkUtils.getPlayer(PerkUtils.server().getPlayer(playerName));
		
		if (toPlayer == null) {
			PerkUtils.OutputToPlayer(player, playerName + " isn't online.");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpr")) {
			
			if (!player.hasPermission("perks.teleport.tpr", true))
				return true;
			
			toPlayer.sendTpRequest(player);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			
			if (!player.hasPermission("perks.teleport.tpa", true))
				return true;
			
			player.acceptTpRequest(toPlayer);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpd")) {
			
			if (!player.hasPermission("perks.teleport.tpa", true))
				return true;
			
			player.declineTpRequest(toPlayer);
			
			return true;
		}
		
		return false;
	}
	
}
