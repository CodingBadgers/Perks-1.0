package me.wman.perks.donator;

import me.wman.perks.config.PerkConfig;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkCapes {
	
	public static void setCape (Player player) {
		
		if (!PerkUtils.spoutEnabled)
			return;
		
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		PermissionGroup[] groups = pex.getUser(player).getGroups();
		String rankName = groups[0].getName().toLowerCase();
		String URL = PerkConfig.capesURL + rankName + ".png";
		
		try {
			sPlayer.checkUrl(URL);
		} catch (UnsupportedOperationException ex) {
			PerkUtils.ErrorConsole("URL for the cape for rank: " + rankName + " is incorrect, Error log:");
			ex.printStackTrace();
			return;
		}
		
		sPlayer.setCape(URL);
		sPlayer.setCapeFor(sPlayer, URL);
	}
	
	public static void removeCape (Player player) {
		
		if (!PerkUtils.spoutEnabled)
			return;
		
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		
		sPlayer.resetCape();
	}
	
}
