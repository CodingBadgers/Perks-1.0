package uk.codingbadgers.perks.admin;

import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkGroupSet {

	private static final PermissionManager pex = PermissionsEx.getPermissionManager();
	
	private static void setGroup(PermissionUser user, PermissionGroup group) {
		// remove from all other groups
		for (PermissionGroup currentgroup : user.getGroups()) user.removeGroup(currentgroup);
		// add to new group
		user.addGroup(group);
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("groupset")) {
			
			// check perms
			if (!player.hasPermission("perks.groupset", true))
				return true;
			
			// check args length
			if (args.size() < 2) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			// target and target group needed
			PermissionUser target = pex.getUser(args.getString(0));
			PermissionGroup group = pex.getGroup(args.getString(1));
			
			// NPE checks
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "Sorry " + args.getString(0) + " is not online");
				return true;
			}
			
			if (group == null) {
				PerkUtils.OutputToPlayer(player, "Sorry that group doesn't exist");
				return true;
			}
			
			// ranks for each of the groups needing to be checked
			int targetRank = target.getGroups()[0].getRank();
			int playerRank = pex.getUser(player.getPlayer()).getGroups()[0].getRank();
			int targetGroupRank = group.getRank();
			
			// rank checks
			if (targetRank <= playerRank) {
				PerkUtils.OutputToPlayer(player, "Sorry you cannot set the group of the user");
				return true;
			}
			
			if (targetGroupRank <= playerRank) {
				PerkUtils.OutputToPlayer(player, "Sorry you cannot set to this group");
				return true;
			}
			
			setGroup(target, group);
			PerkUtils.OutputToPlayer(player, "You have set " + PerkUtils.getPlayer(args.getString(0)).toString() + " group to " + group.getName());
			PerkUtils.OutputToPlayer(PerkUtils.getPlayer(args.getString(0)), player.toString() + " has set your group to " + group.getName());
			return true;
		}
		
		return false;
	}
}
