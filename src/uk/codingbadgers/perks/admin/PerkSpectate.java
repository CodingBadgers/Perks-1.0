package uk.codingbadgers.perks.admin;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkSpectate {

	public static boolean isBeingFolowed(PerkPlayer player) {
		
		for (int i = 0; i < PerkUtils.perkPlayers.size(); i++) {
			if (PerkUtils.perkPlayers.get(i).getFolowing() == player) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args)  {
		
		if (commandLabel.equalsIgnoreCase("spectate")) {
			
			if (args.size() > 1 && args.size() <= 2) {
				PerkUtils.OutputToPlayer(player, cmd.getUsage());
				return true;
			}
			
			if (!player.hasPermission("perks.spectate", true))
				return true;
						
			if (player.isSpectating()) {
				
				Player following = player.getFolowing().getPlayer();
				
				// reset your inventory
				player.getPlayer().getInventory().clear();
				player.getPlayer().getInventory().setContents(player.getInventory());
				
				// teleport them to there start location
				player.teleport(player.getStartLocation());
				
				// reset spectating variables
				player.setSpectating(false);
				player.setSpecatingPlayer(null);
				player.setSpectatingInventory(null);
				player.setStartLocation(null);
				
				// show the player
				player.showPlayer(true);
				player.getPlayer().showPlayer(following);
				return true;
			}
			
			PerkPlayer target = null;
			if (args.size() > 0)
				target = PerkUtils.getPlayer(args.getString(0));
			
			if (target == player) {
				PerkUtils.OutputToPlayer(player, "You cant spectate yourself.");
				return true;
			}
			
			// Set the spectating variables
			player.setSpectating(true);
			player.setSpecatingPlayer(target);
			player.setSpectatingInventory();
			player.setStartLocation();
			
			target.setStalker(player);
			
			// set the players inv to that of the other players.
			player.getPlayer().getInventory().setContents(target.getPlayer().getInventory().getContents());
			
			// teleport them to the other player
			player.teleport(target.getPlayer().getLocation());
			player.hidePlayer(true);
			player.getPlayer().hidePlayer(target.getPlayer());
			return true;
		}
		return false;
	}
	
}
