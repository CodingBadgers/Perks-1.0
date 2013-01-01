package uk.codingbadgers.perks.donator;

import org.bukkit.command.Command;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkDynmap {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (PerkUtils.dynmapapi == null)
			return false;
		
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
