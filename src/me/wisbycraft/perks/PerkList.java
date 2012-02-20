package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkList {

	public static void showOnlineList(Player sender) {
		
		PermissionManager pex = PermissionsEx.getPermissionManager();
		Player[] online = PerkUtils.server().getOnlinePlayers();
		
		 	StringBuilder out = new StringBuilder();
		
		 		List<Player> hidden = new ArrayList<Player>();
	            Map<String, List<Player>> groups = new HashMap<String, List<Player>>();

	            for (Player player : online) {
	                PermissionGroup[] playerGroups = pex.getUser(player.getName()).getGroups();
	                String group = playerGroups.length > 0 ? playerGroups[0].getName() : "Default";

	                if (PerkUtils.getPlayer(player).isHidden()){
	                	hidden.add(player);
	                	continue;
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
    	            sender.sendMessage("0 players are online.");
    	            return;
    	        }

    	        // display current users online and max users
    	        out.append(ChatColor.GOLD + PerkUtils.server().getName() + ": " + ChatColor.GRAY + "Online (");
    	        out.append(online.length - hidden.size());
    	            out.append("/");
    	            out.append(PerkUtils.server().getMaxPlayers());
    	        out.append("): ");
    	        out.append(ChatColor.WHITE);
    	        
	            for (Map.Entry<String, List<Player>> entry : groups.entrySet()) {
	    	        
	                out.append("\n");
	                out.append(ChatColor.WHITE).append(entry.getKey());
	                out.append(": ");

	                // To keep track of commas
	                boolean first = true;

	                for (Player player : entry.getValue()) {
	                    if (!first) {
	                        out.append(", ");
	                    }

	                    out.append(player.getDisplayName()).append(ChatColor.WHITE);

	                    first = false;
	                }
	            }
	            
	            String[] lines = out.toString().split("\n");

	            for (String line : lines) {
	               sender.sendMessage(line);
	            }
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("who") || commandLabel.equalsIgnoreCase("list") || commandLabel.equalsIgnoreCase("players")) {
			if (!player.hasPermission("perks.list", true))
				return true;
			
			showOnlineList(player.getPlayer());
			return true;
		}
		
		return false;
	}
	
}
