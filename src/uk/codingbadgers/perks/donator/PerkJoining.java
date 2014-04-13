package uk.codingbadgers.perks.donator;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import org.bukkit.OfflinePlayer;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkJoining {

	public static boolean kickPlayer() {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		NavigableMap<Integer, PermissionGroup> sortedGroups2 = new TreeMap<Integer, PermissionGroup>(pex.getRankLadder("default")).descendingMap();
		boolean kicked = false;
		
		for (Map.Entry<Integer, PermissionGroup> entry : sortedGroups2.entrySet()) {
			if (!(entry.getKey() >=  PerkConfig.forceJoinCutOff)) 
				continue;
			
			Set<PermissionUser> users = entry.getValue().getUsers();
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
