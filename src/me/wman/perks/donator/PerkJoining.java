package me.wman.perks.donator;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import me.wman.perks.utils.PerkUtils;

import org.bukkit.OfflinePlayer;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkJoining {

	public static boolean kickPlayer() {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		Map<Integer, PermissionGroup> groups = pex.getRankLadder("default");
		
		TreeMap<Integer, PermissionGroup> sortedGroups1 = new TreeMap<Integer, PermissionGroup>(groups);
		NavigableMap<Integer, PermissionGroup> sortedGroups2 = sortedGroups1.descendingMap();
		boolean kicked = false;
		
		for (Map.Entry<Integer, PermissionGroup> entry : sortedGroups2.entrySet()) {
			if (!(entry.getKey() >=  100)) 
				continue;
			
			PermissionUser[] users = entry.getValue().getUsers();
			for (PermissionUser user : users) {
				OfflinePlayer player = PerkUtils.server().getOfflinePlayer(user.getName());
				
				if (!player.isOnline())
					continue;
				
				if (user.has("perks.forcejoin"))
					continue;
				
				// TODO put message here
				player.getPlayer().kickPlayer("Server full");
				kicked = true;
				break;
			}
			
			if (kicked)
				break;
		}
		
		return kicked;
	}
}
