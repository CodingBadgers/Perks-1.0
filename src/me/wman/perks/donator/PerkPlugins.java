package me.wman.perks.donator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

public class PerkPlugins{

	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		PerkPlayer player = PerkUtils.getPlayer((Player)sender);
		
		if (player == null)
			return false;
		
		if (commandLabel.equalsIgnoreCase("plugins") || commandLabel.equalsIgnoreCase("pl")) {
			
			if (!player.hasPermission("perks.plugins", true))
				return true;
			
			displayPluginList(player);
			return true;
		}
		
		return false;
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
		
		PerkUtils.OutputToPlayer(player, "Plugins (" + plugins.length + "): ");
		
		String[] lines = out.toString().split("\n");
		for (String line : lines) {
			player.getPlayer().sendMessage(line);
		}
	}
	
	private static boolean isBlacklisted(String plugin) {
		return PerkUtils.pluginBlacklist.contains(plugin);
	}
}
