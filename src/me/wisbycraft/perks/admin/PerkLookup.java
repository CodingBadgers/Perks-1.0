package me.wisbycraft.perks.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkLookup {

	private static ChatColor getRankColor(PermissionGroup group) {
		
		String prefix = group.getPrefix();
		if (prefix.indexOf("&") != -1) {
			String code = prefix.substring(prefix.indexOf("&") + 1, prefix.indexOf('&') + 2);
			
			if (code == null) 
				return ChatColor.WHITE;
		
			return ChatColor.getByChar(code);
		}
		
		return ChatColor.WHITE;
	}
	
	private static String getIpAddress(PerkPlayer player) {
		return player.getPlayer().getAddress().getHostString();
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("lookup")) {
			
			if (!player.hasPermission("perks.lookup", true))
				return true;
			
			if (args.length !=  1) {
				PerkUtils.OutputToPlayer(player, "use /lookup <player>");
				return true;
			}
			
			StringBuilder out = new StringBuilder();
			PerkPlayer target = PerkUtils.getPlayer(args[0]);
			PermissionManager pex = PermissionsEx.getPermissionManager();
			
			PermissionGroup[] group = null;
			String name = null;
			int health = 0;
			int hunger = 0;
			Location loc = null;
			// boolean banned; <- will be used when we come to writing the new admin plugin
			
			/* 
			 * Need to add more to this at some point 
			 * 
			 * Way to late now though :P
			 */
			
			if (target != null) {
				
				group = pex.getUser(target.getPlayer()).getGroups();
				name = target.getPlayer().getName();
				health = target.getPlayer().getHealth();
				hunger= target.getPlayer().getFoodLevel();
				loc = target.getPlayer().getLocation();
			
				out.append(ChatColor.GOLD + "Stats for " + name).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Rank: " + getRankColor(group[0]) + group[0].getName()).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "IP: " + ChatColor.WHITE + getIpAddress(target)).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Health: " + ChatColor.WHITE + health).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Hunger: " + ChatColor.WHITE + hunger).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Location: " + ChatColor.WHITE + " x: " + loc.getX() + " y: " + loc.getY() + " z: " + loc.getZ()).append(ChatColor.WHITE).append("\n");
				
			} else if (PerkUtils.server().getOfflinePlayer(args[0]) != null) {
				
				OfflinePlayer oTarget = PerkUtils.server().getOfflinePlayer(args[0]);
				
				group = pex.getUser(oTarget.getName()).getGroups();
				name = oTarget.getName();
			
				if (!oTarget.hasPlayedBefore()) {
					PerkUtils.OutputToPlayer(player, "That player has not played on this server");
					return true;
				}
				
				out.append(ChatColor.GOLD + "Stats for " + name).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Rank: " + getRankColor(group[0]) + group[0].getName()).append(ChatColor.WHITE).append("\n");
			}
			
			String[] lines = out.toString().split("\n");

            for (String line : lines) {
               player.getPlayer().sendMessage(line);
            }
            
            return true;
		}
		return false;
	}
	
}
