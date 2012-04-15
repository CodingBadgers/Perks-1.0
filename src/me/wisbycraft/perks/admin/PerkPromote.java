package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkColors;
import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.RankingException;

public class PerkPromote {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("promote")) {
			if (!player.hasPermission("perks.promote", true))
				return true;
			
			if (args.size() == 0) {
				PerkUtils.OutputToPlayer(player, "/promote <name> [ladder]");
				return true;
			}
			
			if (args.size() == 1) {
				PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
				
				PermissionManager pex = PermissionsEx.getPermissionManager();
				PermissionUser user = pex.getUser(args.getString(0));
				PermissionUser promoter = pex.getUser(player.getPlayer());
				PermissionGroup targetGroup;
				
				try {
					targetGroup = user.promote(promoter, "default");	
				} catch (RankingException ex) {
					PerkUtils.OutputToPlayer(player, ex.getMessage());
					return true;
				}
				
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args.getString(0).toLowerCase() + " has been promoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been promoted to " + targetGroup.getName());
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName());
					
					PerkCapes.setCape(target.getPlayer());
					PerkColors.addColor(target.getPlayer());
					return true;
				}	
			}
			
			if (args.size() == 2) {
				PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
				
				PermissionManager pex = PermissionsEx.getPermissionManager();
				PermissionUser user = pex.getUser(args.getString(0));
				PermissionUser promoter = pex.getUser(player.getPlayer());
				PermissionGroup targetGroup;
				String ladder = args.getString(2);
				
				try {
					targetGroup = user.promote(promoter, ladder);	
				} catch (RankingException ex) {
					PerkUtils.OutputToPlayer(player, ex.getMessage());
					return true;
				}
				
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args.getString(0).toLowerCase() + " has been promoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					
					PerkCapes.setCape(target.getPlayer());
					PerkColors.addColor(target.getPlayer());
					return true;
				}	
			}
			
		}
		return false;
	}
}
