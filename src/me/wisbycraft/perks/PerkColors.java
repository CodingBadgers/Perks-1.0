package me.wisbycraft.perks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkColors {
	
	public static void addColor (Player player) {
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		PermissionManager pex = PermissionsEx.getPermissionManager();
			
		ChatColor nameColor = null;
		if ((nameColor = getColor(pex, player)) != null)
			sPlayer.setTitle(nameColor + player.getName());
	}
	
	public static void resetColor (Player player) {
		SpoutPlayer sPlayer = (SpoutPlayer) player;
		
		sPlayer.setTitle(player.getName());
	}

	private static ChatColor getColor (PermissionManager pex, Player player) {
		PermissionUser user = pex.getUser(player);
		PermissionGroup[] group = user.getGroups();
		
		String prefix = group[0].getPrefix();
		
		if (!prefix.contains("&"))
			return null;
		
		String colorCode = prefix.substring(prefix.indexOf('&') + 1, prefix.indexOf('&') + 2);
		ChatColor color = ChatColor.getByChar(colorCode);
		
		if (color == null) {
			return ChatColor.WHITE;
		}
		
		return color;
	}
}
