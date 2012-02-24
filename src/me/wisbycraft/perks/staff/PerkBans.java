package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.utils.BannedPlayer;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkBans {

	public static void banPlayer(Player staff, String target, String reason ) {		
		DatabaseManager.addBannedPlayer(new BannedPlayer(target, reason, staff));
 	}
	
	public static void banPlayer(Player staff, Player target, String reason) {
		if(target.isOnline()) {
			target.kickPlayer("You have been banned!");
		}
		banPlayer(staff, target.getName(), reason);
	}
	
	public static boolean isBanned(Player player) {
		for (int i = 0; i<DatabaseManager.bans.size(); i++) {
			if (DatabaseManager.bans.get(i).getName() == player.getName()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("ban")) {
			
			if (!player.hasPermission("perks.ban", true))
				return true;
			
			PerkPlayer target = PerkUtils.getPlayer(PerkUtils.getPlayer(args[0]));
			
			String reason = "";
    		
    		for (int i = 1; i<args.length; i++) {
    			
    			reason += args[i] + " ";
    			
    		}
    		
    		reason.trim();
    		
			if (target == null ) {
				PermissionUser user = PermissionsEx.getPermissionManager().getUser(args[0]);
				
				if (user.has("perks.ban.exempt") && player.hasPermission("perks.ban.overide", false)) {
					PerkUtils.OutputToPlayer(player, "You cannot ban " + args[0]);
					return true;
				}
					
				banPlayer(player.getPlayer(), args[0], reason);
				PerkUtils.OutputToPlayer(player, args[0] + " has been banned.");
				return true;
			}
			
			if (target.hasPermission("perks.ban.exempt", false) && !player.hasPermission("perks.ban.overide", false)){
				PerkUtils.OutputToPlayer(player, "You cannot ban " + target.getPlayer().getName());
				return true;
			}
			
			banPlayer(player.getPlayer(), target.getPlayer(), reason);
			PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has been banned.");
			return true;
		}
		return false;
	}
}
