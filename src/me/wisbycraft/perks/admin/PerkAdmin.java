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
		
		if (commandLabel.equalsIgnoreCase("perks")) {
			
			PerkUtils.OutputToPlayer(player, "Perks consists of the following perks...");
			player.getPlayer().sendMessage("1 - /fly, /mc - If you're using spout you will fly else a magiccarpet.");
			player.getPlayer().sendMessage("2 - /tpr <name> - Sends a teleport request to a player.");
			player.getPlayer().sendMessage("3 - Unlimited air under water when wearing a gold helmet.");
			player.getPlayer().sendMessage("4 - You're hunger decreases at a much slower rate.");
			player.getPlayer().sendMessage("5 - /death - to teleport to your last death location.");
			player.getPlayer().sendMessage("6 - Capes for spoutcraft users donator and above");
			player.getPlayer().sendMessage("7 - /tphr <name> - Sends a teleport here request to a player");
			
			return true;
		}
		return false;
	}
	
}
