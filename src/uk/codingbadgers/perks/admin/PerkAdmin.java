package uk.codingbadgers.perks.admin;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.plugin.PluginManager;

import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkAdmin {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("perksadmin") || commandLabel.equalsIgnoreCase("padmin")) {
			
			if (args.getString(0).equalsIgnoreCase("reload")) {
				
				if (!player.hasPermission("perks.admin.reload", true))
					return true;
				
				PluginManager pm = PerkUtils.server().getPluginManager();
				pm.disablePlugin(PerkUtils.plugin);
				pm.enablePlugin(PerkUtils.plugin);
				
				PerkUtils.OutputToPlayer(player, "Plugin reloaded");
				return true;
			} else if (args.getString(0).equalsIgnoreCase("disable")){
				
				if (!player.hasPermission("perks.admin.disable", true))
					return true;
				
				PluginManager pm = PerkUtils.server().getPluginManager();
				
				// just making sure it will be displayed
				PerkUtils.OutputToPlayer(player, "Plugin Disabled");
				pm.disablePlugin(PerkUtils.plugin);
				
				return true;
			} else {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
			}
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("perks")) {
			
			PerkUtils.OutputToPlayer(player, "Perks consists of the following perks..");
			ArrayList<String> text = PerkConfig.perksInfo;
			
			for (int i = 0; i < text.size(); i++) {
				player.getPlayer().sendMessage(text.get(i));
			}
			
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("ping")) {
			PerkUtils.OutputToPlayer(player, "pong");
			return true;
		}
				
		return false;
	}
	
}
