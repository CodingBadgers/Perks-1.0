package uk.codingbadgers.perks.admin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

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
		return player.getPlayer().getAddress().getAddress().getHostAddress();
	}
	
	private static String represent(int health) {
		String gui;
		
		switch (health) {
		case 20: case 19:
			gui = ChatColor.GREEN + "||||||||||";
			break;
		
		case 18: case 17:
			gui = ChatColor.GREEN + "|||||||||" + ChatColor.BLACK + "|";
			break;
			
		case 16: case 15:
			gui = ChatColor.GREEN + "||||||||" + ChatColor.BLACK + "||";
			break;
			
		case 14: case 13:
			gui = ChatColor.GREEN + "|||||||" + ChatColor.BLACK + "|||";
			break;
		
		case 12: case 11:
			gui = ChatColor.GREEN + "||||||" + ChatColor.BLACK + "||||";
			break;
			
		case 10: case 9:
			gui = ChatColor.GREEN + "|||||" + ChatColor.BLACK + "||||||";
			break;
			
		case 8: case 7:
			gui = ChatColor.GREEN + "||||" + ChatColor.BLACK + "|||||||";
			break;
			
		case 6: case 5:
			gui = ChatColor.GREEN + "|||" + ChatColor.BLACK + "||||||||";
			break;
			
		case 4: case 3:
			gui = ChatColor.GREEN + "||" + ChatColor.BLACK + "|||||||||";
			break;
			
		case 2: case 1:
			gui = ChatColor.GREEN + "|" + ChatColor.BLACK + "||||||||||";
			break;
			
		case 0:
			gui = ChatColor.BLACK + "|||||||||||";
			break;
			
		default:
			gui = ChatColor.BLACK + "|||||||||||";
		}
		
		return gui;
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("lookup")) {
			
			if (!player.hasPermission("perks.lookup", true))
				return true;
			
			if (args.size() !=  1) {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
				return true;
			}
			
			StringBuilder out = new StringBuilder();
			PerkPlayer target = PerkUtils.getPlayer(args.getString(0));
			PermissionManager pex = PermissionsEx.getPermissionManager();
			
			PermissionGroup[] group = null;
			String name = null;
			String displayName = null;
			int health = 0;
			int hunger = 0;
			Location loc = null;
			String firstPlayed = null;
			String lastPlayed = null;
			boolean op = false;
			
			if (target != null) {
				
				group = pex.getUser(target.getPlayer()).getGroups();
				name = target.getPlayer().getName();
				displayName = target.getPlayer().getDisplayName();
				health = target.getPlayer().getHealth();
				hunger= target.getPlayer().getFoodLevel();
				loc = target.getPlayer().getLocation();
				op = target.getPlayer().isOp();
				boolean banned = false;
				
				try {
					firstPlayed = PerkUtils.parseDate(target.getPlayer().getFirstPlayed());
				} catch (Exception ex){
					firstPlayed = ChatColor.RED + "Error";
				}
				try {
					lastPlayed = PerkUtils.parseDate(target.getPlayer().getLastPlayed());
				} catch (Exception ex){
					lastPlayed = ChatColor.RED + "Error";
				}
			
				out.append(ChatColor.GOLD + "Stats for " + name).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Display Name: " + ChatColor.WHITE + displayName).append("\n");
				out.append(ChatColor.GOLD + "Rank: " + getRankColor(group[0]) + group[0].getName()).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Op: " + ChatColor.WHITE + op).append("\n");
				out.append(ChatColor.GOLD + "IP: " + ChatColor.WHITE + getIpAddress(target)).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Health: " + ChatColor.WHITE + represent(health)).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Hunger: " + ChatColor.WHITE + represent(hunger)).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Location: " + ChatColor.WHITE + " x: " + Math.round(loc.getX()) + " y: " + Math.round(loc.getY()) + " z: " + Math.round(loc.getZ())).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "First Played: " + ChatColor.WHITE + firstPlayed).append("\n");
				out.append(ChatColor.GOLD + "Last Played: " + ChatColor.WHITE + lastPlayed).append("\n");
				if (target.getPlayer().isBanned())
					out.append(ChatColor.RED + "PLAYER IS BANNED.").append("\n");
			
			} else if (PerkUtils.server().getOfflinePlayer(args.getString(0)) != null) {
				
				OfflinePlayer oTarget = PerkUtils.server().getOfflinePlayer(args.getString(0));				
			
				if (!oTarget.hasPlayedBefore()) {
					PerkUtils.OutputToPlayer(player, "That player has not played on this server");
					return true;
				}
				
				group = pex.getUser(oTarget.getName()).getGroups();
				name = oTarget.getName();
				try {
					firstPlayed = PerkUtils.parseDate(oTarget.getFirstPlayed());
				} catch (Exception ex){
					firstPlayed = ChatColor.RED + "Error";
				}
				try {
					lastPlayed = PerkUtils.parseDate(oTarget.getLastPlayed());
				} catch (Exception ex){
					lastPlayed = ChatColor.RED + "Error";
				}
				op = oTarget.isOp();
				
				out.append(ChatColor.GOLD + "Stats for " + name).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Rank: " + getRankColor(group[0]) + group[0].getName()).append(ChatColor.WHITE).append("\n");
				out.append(ChatColor.GOLD + "Op: " + ChatColor.WHITE + op).append("\n");
				out.append(ChatColor.GOLD + "First Played: " + ChatColor.WHITE + firstPlayed).append("\n");
				out.append(ChatColor.GOLD + "Last Played: " + ChatColor.WHITE + lastPlayed).append("\n");
				if (oTarget.isBanned())
					out.append(ChatColor.RED + "PLAYER IS BANNED.").append("\n");
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
