package me.wisbycraft.perks.donator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.TreeMap;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;
import me.wisbycraft.perks.utils.PerkWebChatPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkList {

	private static PermissionManager pex = PermissionsEx.getPermissionManager();
	
	public static void showOnlineList(PerkPlayer sender) {
		
		if (sender == null)
			return;
		
		Player[] online = PerkUtils.server().getOnlinePlayers();
		
		StringBuilder out = new StringBuilder();
		
		//out.append(onlineWebChat(PerkUtils.webChatPlayers));
		out.append(onlineServer(sender,online));
		
	    String[] lines = out.toString().split("\n");

	    for (String line : lines) {
	    	sender.getPlayer().sendMessage(line);
	    }
	}
	
	private static String getRank(PermissionGroup group) {
        
        String prefix = group.getPrefix();
        
        while(prefix.indexOf("&") != -1) {

			String code = prefix.substring(prefix.indexOf("&") + 1, prefix.indexOf("&") + 2);

			prefix = prefix.substring(0, prefix.indexOf("&")) +  ChatColor.getByChar(code) + prefix.substring(prefix.indexOf("&") + 2);
								
		}
        
        while(prefix.indexOf("[") != -1) {

			prefix = prefix.substring(0, prefix.indexOf("[")) + prefix.substring(prefix.indexOf("[") + 1);
								
		}
        
        while(prefix.indexOf("]") != -1) {

			prefix = prefix.substring(0, prefix.indexOf("]")) + prefix.substring(prefix.indexOf("]") + 1);
								
		}
        
        return prefix;
	}

	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("who") || commandLabel.equalsIgnoreCase("list") || commandLabel.equalsIgnoreCase("players")) {
			
			if (!player.hasPermission("perks.list", true))
				return true;
			
			showOnlineList(player);
			return true;
		}
		
		return false;
	}
	
	public static String onlineWebChat(ArrayList<PerkWebChatPlayer> webchatPlayers) {
		
		Map<PermissionGroup, List<PerkWebChatPlayer>> webchat = new HashMap<PermissionGroup, List<PerkWebChatPlayer>>();
        
        for (PerkWebChatPlayer player : webchatPlayers) {
        	PermissionGroup group = player.getGroup();
        	
        	if (webchat.containsKey(group)) {
        		webchat.get(group).add(player);
        	} else {
        		List<PerkWebChatPlayer> list = new ArrayList<PerkWebChatPlayer>();
                list.add(player);
                webchat.put(group, list);
        	}
        }
        
		StringBuilder out = new StringBuilder();
		
		// add the number of players on webchat
        out.append("WebChat:(");
        	out.append(webchatPlayers.size());
        	out.append("):");
        	
        int inGroup;
        
        for (Map.Entry<PermissionGroup, List<PerkWebChatPlayer>> entry : webchat.entrySet()) {
	        
        	inGroup = entry.getValue().size();
       	            	
        	if (inGroup == 0) {
        		continue;
        	}
        	
            out.append("\n");
            out.append(getRank(entry.getKey()));
            out.append(ChatColor.WHITE + ": ");

            // To keep track of commas
            boolean first = true;

            for (PerkWebChatPlayer player : entry.getValue()) {
            	if (!first) 
                   out.append(", ");
            	
               out.append(player.getName());
               
               first = false;
            }
        }
        
        return out.toString();
        
	}
	
	public static String onlineServer(PerkPlayer sender, Player[] online) {
		StringBuilder out = new StringBuilder();
		List<Player> hidden = new ArrayList<Player>();
        Map<PermissionGroup, List<Player>> groups = new HashMap<PermissionGroup, List<Player>>();

        for (Player player : online) {
            PermissionGroup[] playerGroups = pex.getUser(player.getName()).getGroups();
            PermissionGroup group = playerGroups.length > 0 ? playerGroups[0] : pex.getDefaultGroup();

            if (PerkUtils.getPlayer(player).isVanished()){
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
        
        if (online.length == PerkUtils.server().getMaxPlayers()) {
        	// display current users online and max users in gold
        	out.append(ChatColor.GOLD + PerkUtils.server().getServerName() + ": " + ChatColor.GRAY + "Online " + ChatColor.GOLD + "(" + ChatColor.GOLD);
        	out.append(online.length - hidden.size());
            	out.append("/");
            	out.append(PerkUtils.server().getMaxPlayers());
            	out.append(") ");
            out.append(ChatColor.WHITE);
        } else {
        	// display current users online and max users
        	out.append(ChatColor.GOLD + PerkUtils.server().getServerName() + ": " + ChatColor.GRAY + "Online (");
        	out.append(online.length - hidden.size());
            	out.append("/");
            	out.append(PerkUtils.server().getMaxPlayers());
            	out.append(") ");
            out.append(ChatColor.WHITE);
        }
            	           
        //FIXME get sorting working again, just compresses if more than one person online
        // Map<PermissionGroup, List<Player>> sortGroups = new TreeMap<PermissionGroup, List<Player>>(groups);
   
        int inGroup = 0;
        List<Player> hiddenGroup = new ArrayList<Player>();
        
        for (Map.Entry<PermissionGroup, List<Player>> entry : groups.entrySet()) {
	        
        	inGroup = entry.getValue().size();
        	
        	for (int i = 0; i < entry.getValue().size(); i++) {
        		if (PerkUtils.getPlayer(entry.getValue().get(i)).isVanished() && !sender.hasPermission("perks.list.showvanish", false)) {
        			hiddenGroup.add(entry.getValue().get(i));
        		}
        	}
        	
        	inGroup -= hiddenGroup.size();
        	
        	if (inGroup == 0) {
        		continue;
        	}
        	
            out.append("\n");
            out.append(getRank(entry.getKey()));
            out.append(ChatColor.WHITE + ": ");

            // To keep track of commas
            boolean first = true;

            for (Player player : entry.getValue()) {
                if (PerkUtils.getPlayer(player).isAfk()) {
                	if (!first) {
                        out.append(", ");
                    }
                	out.append(ChatColor.GRAY + player.getName()).append(ChatColor.WHITE);
                	
                } else if (PerkUtils.getPlayer(player).isVanished()) {
                	
                	if (sender.hasPermission("perks.list.showvanish", false)) {
                		
                		if (!first) {
	                        out.append(", ");
	                    }
                		out.append(ChatColor.GOLD + player.getName()).append(ChatColor.WHITE);
                		
                	} else {
                		
                		continue;
                	}
                } else {
                	
                	if (!first) {
                        out.append(", ");
                    }
                	
                	out.append(player.getDisplayName()).append(ChatColor.WHITE);
                }
                first = false;
            }
        }
        
        return out.toString();
	}
	
}
