package me.wman.perks.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import me.wman.perks.utils.PerkArgSet;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

public class PerkTroll {

	private static void sendMessageAsPlayer(PerkPlayer player, PerkArgSet args){ 
		PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
		
		if (target == null) {
			PerkUtils.OutputToPlayer(player, "That player is not online");
			return;
		}
		
		String tempArgs = "";
		for (int i = 1; i < args.size(); i++) {
			tempArgs += args.getString(i) + " ";
		}
		tempArgs = tempArgs.trim();
		
		target.getPlayer().chat(tempArgs);
	}
	
	private static void fakeOp(PerkPlayer player, PerkArgSet args) {
		PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
		
		if (target == null) {
			PerkUtils.OutputToPlayer(player, "That player is not online");
			return;
		}
		
		// message them 
		target.getPlayer().sendMessage(ChatColor.YELLOW + "You are now op!");
		PerkUtils.OutputToPlayer(player, "You have fake opped " + target.getPlayer().getName());
		
		// set them to no group and add them to Op
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(target.getPlayer());
		for (PermissionGroup group : user.getGroups()) user.removeGroup(group);
		user.addGroup(PermissionsEx.getPermissionManager().getGroup("op"));
		
		// deop them
		target.getPlayer().setOp(false);
	}
	
	private static void rename(PerkPlayer player, PerkArgSet args) {
		PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
		
		if (target == null) {
			PerkUtils.OutputToPlayer(player, "That player is not online");
			return;
		}
		
		target.getPlayer().setDisplayName(args.getString(1));
		PerkUtils.OutputToPlayer(player, "You have renamed " + target.getPlayer().getName() + " to " + args.getString(1));
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("fakeop")) {
			
			if (!player.hasPermission("perks.troll.fakeop", true))
				return true;
			
			if (args.size() != 1) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			fakeOp(player, args);
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("rename")) {
			
			if (!player.hasPermission("perks.troll.rename", true))
				return true;
			
			if (args.size() != 2) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			rename(player, args);
			return true;
		}

		if (commandLabel.equalsIgnoreCase("speak")) {
			
			if (!player.hasPermission("perks.troll.speak", true))
				return true;
			
			if (args.size() < 2) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			sendMessageAsPlayer(player, args);
			return true;
		}
		return false;
	}
}
