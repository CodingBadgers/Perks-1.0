package me.wman.perks.donator;

import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

public class PerkPlugins{

	// works now but is abit of a hack
	public static void onCommand(String command, PerkPlayer player) {
		
		/*PerkPlayer player = PerkUtils.getPlayer((Player)sender);
		
		if (player == null)
			return false;*/
		
		if (command.equalsIgnoreCase("plugins") || command.equalsIgnoreCase("pl")) {
			
			if (!player.hasPermission("perks.plugins", true))
				return;
			
			displayPluginList(player);
			return;
		}
	
	}
	
	public static void displayPluginList(PerkPlayer player){
		
		Plugin[] plugins = PerkUtils.server().getPluginManager().getPlugins();
		StringBuilder out = new StringBuilder();
		boolean first = true;
		
		for (Plugin plugin : plugins) {
			
			if (isBlacklisted(plugin.getName()))
				continue;
			
			out.append((!first ? ", " : "" ) + (plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + plugin.getName());
			first = false;
		}
		
		player.getPlayer().sendMessage(ChatColor.AQUA + "[Perks] " + "Plugins (" + plugins.length + "): ");
		
		String[] lines = out.toString().split("\n");
		for (String line : lines) {
			player.getPlayer().sendMessage(line);
		}
	}
	
	private static boolean isBlacklisted(String plugin) {
		return PerkUtils.pluginBlacklist.contains(plugin);
	}
}
