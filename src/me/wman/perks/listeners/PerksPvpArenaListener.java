package me.wman.perks.listeners;

import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;
import net.slipcor.pvparena.events.PAJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PerksPvpArenaListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PAJoinEvent event) {
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
	}
}