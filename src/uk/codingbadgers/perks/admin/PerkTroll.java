package uk.codingbadgers.perks.admin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkTroll {

	public static ArrayList<String> commandBlacklist = new ArrayList<String>();

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
		
		if (tempArgs.startsWith("/")) {
			PerkUtils.OutputToPlayer(player, "Sorry you cannot use commands as other players");
			return;
		}
		
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
	
	private static void sendCommand(PerkPlayer player, PerkArgSet args) {
		PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
		
		if (target == null) {
			PerkUtils.OutputToPlayer(player, "That player is not online");
			return;
		}
		
		String command = "";
		for (int i = 1; i < args.size(); i++) {
			command += args.getString(i) + " ";
		}
		command = command.trim();
		
		if (!isBlacklisted(command)) {
			PerkUtils.OutputToPlayer(player, "You cannot use that command as another player");
			return;
		}
		
		PerkUtils.server().dispatchCommand(target.getPlayer(), command);
	}
	
	private static boolean isBlacklisted(String command) {
		return commandBlacklist.contains(command.substring(0, command.indexOf(' ')));
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
		
		if (commandLabel.equalsIgnoreCase("sendCommand") || commandLabel.equalsIgnoreCase("sc")) {
			if (!player.hasPermission("perks.troll.command", true))
				return true;
			
			if (args.size() < 2) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			sendCommand(player, args);
			return true;
		}
		return false;
	}
}
