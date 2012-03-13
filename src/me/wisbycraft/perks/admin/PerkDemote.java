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

public class PerkDemote {
	
	private static PermissionGroup demote(Player sender, String name, String ladder) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		PermissionUser user = pex.getUser(name);
		PermissionUser promoter = pex.getUser(sender);
		
		try {
			PermissionGroup targetGroup = user.demote(promoter, ladder);	
			
			return targetGroup;
		} catch (RankingException ex) {
			return null;
		}
	}
	
	private static PermissionGroup demote(Player sender, String name) {
		return demote(sender, name, "default");
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("demote")) {
			if (!player.hasPermission("perks.demote", true))
				return true;
			
			if (args.length == 0) {
				PerkUtils.OutputToPlayer(player, "/demote <name> [ladder]");
				return true;
			}
			
			if (args.length == 1) {
				PerkPlayer target = PerkUtils.getPlayer(args[0]);
				
				PermissionGroup targetGroup = demote(player.getPlayer(), args[0]);
				
				if (targetGroup == null) {
					PerkUtils.OutputToPlayer(player, "Sorry there was a error demoting " + args[0].toLowerCase());
					return true;
				}
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args[0].toLowerCase() + " has been demoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been demoted to " + targetGroup.getName());
					PerkUtils.OutputToPlayer(target, "You have been demoted to " + targetGroup.getName());
					
					PerkCapes.setCape(target.getPlayer());
					PerkColors.addColor(target.getPlayer());
					return true;
				}	
			}
			
			if (args.length == 2) {
				PerkPlayer target = PerkUtils.getPlayer(args[0]);
				String ladder = args[1].toLowerCase();
				
				PermissionGroup targetGroup = demote(player.getPlayer(), args[0], ladder);
				
				if (targetGroup == null) {
					PerkUtils.OutputToPlayer(player, "Sorry there was a error demoting " + args[0].toLowerCase());
					return true;
				}
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args[0].toLowerCase() + " has been demoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been demoted to " + targetGroup.getName() + " on ladder " + ladder);
					PerkUtils.OutputToPlayer(target, "You have been demoted to " + targetGroup.getName() + " on ladder " + ladder);
					
					PerkCapes.setCape(target.getPlayer());
					PerkColors.addColor(target.getPlayer());
					return true;
				}	
			}
			
		}
		return false;
	}
}