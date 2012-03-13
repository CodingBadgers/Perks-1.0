package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.config.PerkConfig;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkCapes {
	
	public static void setCape (Player player) {
		
		if (!PerkUtils.spoutEnabled)
			return;
		
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		PermissionGroup[] groups = pex.getUser(player).getGroups();
		String rankName = groups[0].getName().toLowerCase();
		String URL = PerkConfig.capesURL + rankName + ".png";
		
		try {
			sPlayer.checkUrl(URL);
		} catch (UnsupportedOperationException ex) {
			PerkUtils.ErrorConsole("URL for the cape for rank: " + rankName + " is incorrect, Error log:");
			ex.printStackTrace();
			return;
		}
		
		sPlayer.setCape(URL);
		sPlayer.setCapeFor(sPlayer, URL);
	}
	
	public static void removeCape (Player player) {
		
		if (!PerkUtils.spoutEnabled)
			return;
		
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		
		sPlayer.resetCape();
	}
	
	public static boolean onCommand(PerkPlayer sender, Command cmd, String commandLabel, String[] args) {
				
		if (commandLabel.equalsIgnoreCase("capes")) {
			
			if (!PerkUtils.spoutEnabled)
				return true;
			
			if (args.length < 1 || args.length > 2) {
				PerkUtils.OutputToPlayer(sender, ChatColor.RED + "/capes <update/remove> [player]");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("update")) {
				
				if (args.length == 1) {
					
					if (!sender.hasPermission("perks.capes.admin.all", true)) {
						return true;
					}
					
					Player[] players = PerkUtils.plugin.getServer().getOnlinePlayers();
					
					for (int i = 0; i<players.length; i++) {
						setCape(players[i]);
						PerkColors.addColor(players[i]);
					}
					
					PerkUtils.OutputToPlayer(sender, "Capes and Colors Updated");
					return true;
				}
				
				if (args.length == 2) {
					
					if (!sender.hasPermission("perks.capes.admin.single", true)) {
						return true;
					}
					
					Player players = PerkUtils.getPlayer(args[1]).getPlayer();
					
					setCape(players);
					PerkColors.addColor(players);
					
					PerkUtils.OutputToPlayer(sender, "Capes and Colors Updated for " + players.getName().toLowerCase());
					return true;
				}
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				
				if (args.length != 2) {
					PerkUtils.OutputToPlayer(sender, ChatColor.RED + "/capes remove [player]");
					return true;
				}
				
				Player player = PerkUtils.getPlayer(args[1]).getPlayer();
				
				setCape(player);
				PerkColors.resetColor(player);
				
				PerkUtils.OutputToPlayer(sender, "Cape and colors removed for that player");
				return true;
				
			}
			
			return true;
		}
		
		return false;
	}
}