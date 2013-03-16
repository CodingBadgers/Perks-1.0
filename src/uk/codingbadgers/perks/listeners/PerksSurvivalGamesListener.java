package uk.codingbadgers.perks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;
import uk.thecodingbadgers.survivalgames.Events.Custom.SGPlayerJoinArenaEvent;

public class PerksSurvivalGamesListener implements Listener {

	@EventHandler
	public void onPlayerJoinArena(SGPlayerJoinArenaEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player.isFlying()) {
			
			player.setFlying(false);
		
			PerkUtils.OutputToPlayer(player,"Your fly mode has been disabled while you are in the arena");
		}
		
		if (player.isVanished()) {
			
			player.showPlayer(true);
			
			PerkUtils.OutputToPlayer(player, "Your vanish mode has been disabled while you are in the arena");
		}
		
		if (player.isAfk()) {
			
			player.setAfk(false);
			
			PerkUtils.OutputToPlayer(player, "You are back while you are in the arena while you are in the arena");
		}
		
		if (PerkUtils.disguiseCraftApi != null) {
			
			if (PerkUtils.disguiseCraftApi.isDisguised(player.getPlayer())) {
				
				PerkUtils.disguiseCraftApi.undisguisePlayer(player.getPlayer());
				
				PerkUtils.OutputToPlayer(player, "You have been undisguised whilst you are in the arena");
			}
		}
	}
}
