package me.wisbycraft.perks.admin;

import org.bukkit.command.Command;
import org.bukkit.plugin.PluginManager;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkAdmin {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("perksadmin") || commandLabel.equalsIgnoreCase("padmin")) {
			
			if (args[0].equalsIgnoreCase("reload")) {
				PluginManager pm = PerkUtils.server().getPluginManager();
				pm.disablePlugin(PerkUtils.plugin);
				pm.enablePlugin(PerkUtils.plugin);
				
				PerkUtils.OutputToPlayer(player, "Plugin reloaded");
				return true;
			}
			return true;
		}
		return false;
	}
	
}
