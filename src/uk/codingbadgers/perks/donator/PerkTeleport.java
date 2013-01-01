 package uk.codingbadgers.perks.donator;


import org.bukkit.Location;
import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkTeleport {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {

		
		if (cmd.getName().equalsIgnoreCase("tpr") || 
			cmd.getName().equalsIgnoreCase("tphr") ||
			cmd.getName().equalsIgnoreCase("tpd") ||
			cmd.getName().equalsIgnoreCase("tpa"))
		{
			PerkPlayer toPlayer = null;
			
			if (args.size() == 1) {
				String playerName = args.getString(0);
				toPlayer = PerkUtils.getPlayer(PerkUtils.server().getPlayer(playerName));
				
				if (toPlayer == null || toPlayer.isVanished()) {
					PerkUtils.OutputToPlayer(player, playerName + " isn't online.");
					return true;
				}
			}
			
			if (cmd.getName().equalsIgnoreCase("tpr")) {
				
				if (args.size() != 1) {
					PerkUtils.OutputToPlayer(player, cmd.getUsage());
					return true;
				}
				
				if (!player.hasPermission("perks.teleport.tpr", true)) {
					return true;
				}
				
				toPlayer.sendTpRequest(player);
				
				return true;
			}
			
			if (cmd.getName().equalsIgnoreCase("tphr")) {
				
				if (args.size() != 1) {
					PerkUtils.OutputToPlayer(player, cmd.getUsage());
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
		}
		
		if (cmd.getName().equalsIgnoreCase("tp")) {
			
			if (!player.hasPermission("perks.teleport.tp", true))
				return true;
			
			if (args.size() != 1) {
				PerkUtils.OutputToPlayer(player, cmd.getUsage());
				return true;
			}
			
			Location loc = null;
			
			// Handle coordinates
	        if (args.getString(0).matches("^[\\-0-9\\.]+,[\\-0-9\\.]+,[\\-0-9\\.]+(?:.+)?$")) {

	            String[] arg = args.getString(0).split(":");
	            String[] parts = args.getString(0).split(",");
	            double x = 0, y = 0, z = 0;

	            try {
	                x = Double.parseDouble(parts[0]);
	                y = Double.parseDouble(parts[1]);
	                z = Double.parseDouble(parts[2]);
	            } catch (NumberFormatException e) {
	            	PerkUtils.OutputToPlayer(player, "");
	            }

	            if (arg.length > 1) {
	                loc = new Location(PerkUtils.server().getWorld(arg[1]), x, y, z);
	            } else {
	                loc = new Location(player.getPlayer().getWorld(), x, y, z);
	            }

	            player.teleport(loc);
				
				PerkUtils.OutputToPlayer(player, "You have been teleported");
				
				return true;
	        }
			
	        PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "That player is not online");
				return true;
			}
			
			loc = target.getPlayer().getLocation();
			
			player.teleport(loc);
			
			PerkUtils.OutputToPlayer(player, "You have been teleported");
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tphere") || cmd.getName().equalsIgnoreCase("s") || cmd.getName().equalsIgnoreCase("bring")) {
			
			if (!player.hasPermission("perks.teleport.tphere", true))
				return true;
			
			if (args.size() != 1) {
				PerkUtils.OutputToPlayer(player, cmd.getUsage());
				return true;
			}
			
			PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "That player is not online");
				return true;
			}
			
			player.teleportHere(target);
			
			PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been teleported to you.");
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("put")) {
			
			if (!player.hasPermission("perks.teleport.put", true))
				return true;
			
			if (args.size() != 1) {
				PerkUtils.OutputToPlayer(player, cmd.getUsage());
				return true;
			}
			
			PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "That player is not online");
				return true;
			}
			
			target.getPlayer().teleport(player.getPlayer().getTargetBlock(null, 300).getLocation());
			return true;
		}

		return false;
	}

}
