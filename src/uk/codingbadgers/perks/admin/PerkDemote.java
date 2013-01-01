package uk.codingbadgers.perks.admin;


import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.RankingException;
import uk.codingbadgers.perks.donator.PerkCapes;
import uk.codingbadgers.perks.donator.PerkColors;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkDemote {
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("demote")) {
			if (!player.hasPermission("perks.demote", true))
				return true;
			
			if (args.size() == 0) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			if (args.size() == 1) {
				PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
				
				PermissionManager pex = PermissionsEx.getPermissionManager();
				PermissionUser user = pex.getUser(args.getString(0));
				PermissionUser demoter = pex.getUser(player.getPlayer());
				PermissionGroup targetGroup;
				
				try {
					targetGroup = user.demote(demoter, "default");	
				} catch (RankingException ex) {
					PerkUtils.OutputToPlayer(player, ex.getMessage());
					return true;
				}
				
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args.getString(0).toLowerCase() + " has been demoted to " + targetGroup.getName());
					return true;
				} else {
					
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been demoted to " + targetGroup.getName());
					PerkUtils.OutputToPlayer(target, "You have been demoted to " + targetGroup.getName());
					
					PerkCapes.setCape(target.getPlayer());
					PerkColors.addColor(target.getPlayer());
					return true;
				}	
			}
			
			if (args.size() == 2) {
				PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
				
				PermissionManager pex = PermissionsEx.getPermissionManager();
				PermissionUser user = pex.getUser(args.getString(0));
				PermissionUser demoter = pex.getUser(player.getPlayer());
				PermissionGroup targetGroup;
				String ladder = args.getString(2);
				
				try {
					targetGroup = user.demote(demoter, ladder);	
				} catch (RankingException ex) {
					PerkUtils.OutputToPlayer(player, ex.getMessage());
					return true;
				}
				
				if (target == null) {
					
					PerkUtils.OutputToPlayer(player, args.getString(0).toLowerCase() + " has been demoted to " + targetGroup.getName());
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
