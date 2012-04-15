package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;

public class PerksMobAreanaListener implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinArena(ArenaPlayerJoinEvent event) {
		
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player.isFlying()) {
			
			player.setFlying(false);
		
			PerkUtils.OutputToPlayer(player,"Your fly mode has been disabled while you are in the arena");
		}
		
		if (player.isHidden()) {
			
			player.showPlayer(true);
			
			PerkUtils.OutputToPlayer(player, "Your vanish mode has been disabled while you are in the arena");
		}
		
		if (player.isAfk()) {
			
			player.setAfk(false);
			
			PerkUtils.OutputToPlayer(player, "You are back while you are in the arena while you are in the arena");
		}
	}
	
}
