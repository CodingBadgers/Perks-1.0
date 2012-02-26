package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkColors;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.RankingException;

public class PerkPromote {
	
	private static PermissionGroup promote(Player sender, String name, String ladder) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		PermissionUser user = pex.getUser(name);
		PermissionUser promoter = pex.getUser(sender);
		
		try {
			PermissionGroup targetGroup = user.promote(promoter, ladder);	
			
			return targetGroup;
		} catch (RankingException ex) {
			return null;
		}
	}
	
	private static PermissionGroup promote(Player sender, String name) {
		return promote(sender, name, "default");
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("promote")) {
			if (!player.hasPermission("perks.promote", true))
				return true;
			
			if (args.length == 0) {
				PerkUtils.OutputToPlayer(player, "/promote <name> [ladder]");
				return true;
			}
			
			if (args.length == 1) {
				Player target = PerkUtils.getPlayer(args[0]).getPlayer();
				
				PermissionGroup targetGroup = promote(player.getPlayer(), args[0]);
				
				if (targetGroup == null) {
					PerkUtils.OutputToPlayer(player, "Sorry there was a error promoting " + args[0].toLowerCase());
					return true;
				}
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args[0].toLowerCase() + " has been promoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getName() + " has been promoted to " + targetGroup.getName());
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName());
					
					PerkCapes.setCape(target);
					PerkColors.addColor(target);
					return true;
				}	
			}
			
			if (args.length == 2) {
				Player target = PerkUtils.getPlayer(args[0]).getPlayer();
				String ladder = args[1].toLowerCase();
				
				PermissionGroup targetGroup = promote(player.getPlayer(), args[0], ladder);
				
				if (targetGroup == null) {
					PerkUtils.OutputToPlayer(player, "Sorry there was a error promoting " + args[0].toLowerCase());
					return true;
				}
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args[0].toLowerCase() + " has been promoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getName() + " has been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					
					PerkCapes.setCape(target);
					PerkColors.addColor(target);
					return true;
				}	
			}
			
		}
		return false;
	}
}
