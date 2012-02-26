package me.wisbycraft.perks.donator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkList {

	public static void showOnlineList(PerkPlayer sender) {
		
		PermissionManager pex = PermissionsEx.getPermissionManager();
		Player[] online = PerkUtils.server().getOnlinePlayers();
		
		 	StringBuilder out = new StringBuilder();
		
		 		List<Player> hidden = new ArrayList<Player>();
	            Map<String, List<Player>> groups = new HashMap<String, List<Player>>();

	            for (Player player : online) {
	                PermissionGroup[] playerGroups = pex.getUser(player.getName()).getGroups();
	                String group = playerGroups.length > 0 ? playerGroups[0].getName() : "Default";

	                if (PerkUtils.getPlayer(player).isHidden() && !sender.hasPermission("perks.list.showvanished", false)){
	                	hidden.add(player);
	                }
 	                
	                if (groups.containsKey(group)) {
	                    groups.get(group).add(player);
	                } else {
	                    List<Player> list = new ArrayList<Player>();
	                    list.add(player);
	                    groups.put(group, list);
	                }
	                
	            }

	            // This applies mostly to the console, so there might be 0 players
    	        // online if that's the case!
    	        if (online.length == 0) {
    	            sender.getPlayer().sendMessage("0 players are online.");
    	            return;
    	        }
    	        
    	        if (online.length == PerkUtils.server().getMaxPlayers()) {
    	        	// display current users online and max users
    	        	out.append(ChatColor.GOLD + PerkUtils.server().getServerName() + ": " + ChatColor.GRAY + "Online " + ChatColor.GOLD + "(" + ChatColor.GOLD);
    	        	out.append(online.length - hidden.size());
    	            	out.append("/");
    	            	out.append(PerkUtils.server().getMaxPlayers());
    	            	out.append("): ");
    	            out.append(ChatColor.WHITE);
    	        } else {
    	        	// display current users online and max users
    	        	out.append(ChatColor.GOLD + PerkUtils.server().getServerName() + ": " + ChatColor.GRAY + "Online (");
    	        	out.append(online.length - hidden.size());
    	            	out.append("/");
    	            	out.append(PerkUtils.server().getMaxPlayers());
    	            	out.append("): ");
    	            out.append(ChatColor.WHITE);
    	        }
    	        
	            for (Map.Entry<String, List<Player>> entry : groups.entrySet()) {
	    	        
	                out.append("\n");
	                out.append(getRankColor(entry.getKey())).append(entry.getKey());
	                out.append(ChatColor.WHITE + ": ");

	                // To keep track of commas
	                boolean first = true;

	                for (Player player : entry.getValue()) {
	                    if (!first) {
	                        out.append(", ");
	                    }

	                    if (PerkUtils.getPlayer(player).isAfk()) {
	                    	
	                    	out.append(ChatColor.GRAY + player.getName()).append(ChatColor.WHITE);
	                    } else if (PerkUtils.getPlayer(player).isHidden()) {
	                    	
	                    	out.append(ChatColor.GOLD + player.getName()).append(ChatColor.WHITE);
	                    } else {
	                    	
	                    	out.append(player.getDisplayName()).append(ChatColor.WHITE);
	                    }
	                    first = false;
	                }
	            }
	            
	            String[] lines = out.toString().split("\n");

	            for (String line : lines) {
	               sender.getPlayer().sendMessage(line);
	            }
	}
	
	private static ChatColor getRankColor(String name) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		PermissionGroup group = pex.getGroup(name);
		
		String prefix = group.getPrefix();
		String code = prefix.substring(prefix.indexOf("&") + 1, prefix.indexOf('&') + 2);
		
		if (code == null) 
			return ChatColor.WHITE;
	
		ChatColor colour = ChatColor.getByChar(code);
		
		if (colour == null) 
			return ChatColor.WHITE;
		
		return colour;
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("who") || commandLabel.equalsIgnoreCase("list") || commandLabel.equalsIgnoreCase("players")) {
			
			if (!player.hasPermission("perks.list", true))
				return true;
			
			showOnlineList(player);
			return true;
		}
		
		return false;
	}
	
}
