package me.wisbycraft.perks.donator;

import org.bukkit.command.Command;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkDynmap {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("show")) {
			
			if (player.isDynmapHidden()){
				PerkUtils.OutputToPlayer(player, "You are already visible on livemap");
			}
			
			player.dynmapShow();
			PerkUtils.OutputToPlayer(player, "You are now visible on livemap");
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("hide")) {
			
			if (!player.isDynmapHidden()){
				PerkUtils.OutputToPlayer(player, "You are already hidden on livemap");
			}
			
			player.dynmapHide();
			PerkUtils.OutputToPlayer(player, "You are now hidden on livemap");
			return true;
		}
		
		return false;
	}
	
}
