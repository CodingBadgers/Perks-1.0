package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.exceptions.RankingException;

public class PerkPromote {
	
	private static void promote(PerkPlayer sender, Player player) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		PermissionUser user = pex.getUser(player);
		PermissionUser promoter = pex.getUser(sender.getPlayer());
		
		try {
			PermissionGroup targetGroup = user.promote(promoter, "default");
			
			PerkUtils.OutputToPlayer(sender, "The user " + player.getName() + " has been promoted to " + targetGroup.getName());
			PerkUtils.OutputToPlayer(player, "You have been promoted to " + targetGroup.getName());
		} catch (RankingException e) {
			PerkUtils.OutputToPlayer(sender, "There was a error promoting the player, check the log");
			e.printStackTrace();
			return;
		}
		
		PerkCapes.setCape(player);
		PerkColors.addColor(player);
	}
	
	private static void promote(PerkPlayer sender, Player player, String ladder) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		PermissionUser user = pex.getUser(player);
		PermissionUser promoter = pex.getUser(sender.getPlayer());
		
		try {
			PermissionGroup targetGroup = user.promote(promoter, ladder);
			
			PerkUtils.OutputToPlayer(sender, "The user " + player.getName() + " has been promoted to " + targetGroup.getName());
			PerkUtils.OutputToPlayer(player, "You have been promoted to " + targetGroup.getName());
		} catch (RankingException e) {
			PerkUtils.OutputToPlayer(sender, "There was a error promoting the player, check the log");
			e.printStackTrace();
			return;
		}
		
		PerkCapes.setCape(player);
		PerkColors.addColor(player);
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("promote")) {
			
			if (!player.hasPermission("perks.promote", true)) 
				return true;
			
			if (args.length == 1) {
				
				Player user = PerkUtils.getPlayer(args[0]);
				
				if (user != null)
					promote(player, user);
				
			} else if (args.length == 2) {
				
				Player user = PerkUtils.getPlayer(args[0]);
				if (user != null)
					promote (player, user, args[1]);

			} else {
				
				PerkUtils.OutputToPlayer(player, "use /promote <name> [ladder]");

			}
			
			return true;
			
		}
		
		return false;
	}
}
