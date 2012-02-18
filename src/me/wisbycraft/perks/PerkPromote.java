package me.wisbycraft.perks;

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
			if (!player.hasPermission("Perks.promote", true))
				return true;
			
			if (args.length == 0) {
				PerkUtils.OutputToPlayer(player, "/promote <name> [ladder]");
				return true;
			}
			
			if (args.length == 1) {
				Player target = PerkUtils.getPlayer(args[0]);
				
				PermissionGroup targetGroup = promote(player.getPlayer(), args[0]);
				
				if (target == null) {
					
					return true;
				} else {
					
					// FIXME causes NPE??
					PerkUtils.OutputToPlayer(player, target.getName() + " has been promoted to " + targetGroup.getName());
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName());
					return true;
				}	
			}
			
			if (args.length == 2) {
				Player target = PerkUtils.getPlayer(args[0]);
				String ladder = args[1].toLowerCase();
				
				PermissionGroup targetGroup = promote(player.getPlayer(), args[0], ladder);
				
				if (target == null) {
					return true;
				} else {
					PerkUtils.OutputToPlayer(player, target.getName() + " has been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					PerkUtils.OutputToPlayer(target, "You have been promoted to " + targetGroup.getName() + " on ladder " + ladder);
					return true;
				}
			}
			
		}
		return false;
	}
}
