package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;
import net.slipcor.pvparena.events.PAJoinEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PerksPvpArenaListener implements Listener{

	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoin(PAJoinEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player.isFlying()) {
			
			player.setFlying(false);
		
			PerkUtils.OutputToPlayer(player,"Your fly mode has been disabled while you are in the arena");
		}
		
		if (player.isHidden()) {
			
			player.showPlayer(true);
			
			PerkUtils.OutputToPlayer(player, "Your vanish mode has been disabled while you are in the arena");
		}
	}
}
