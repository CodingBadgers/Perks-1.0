package uk.codingbadgers.perks.listeners;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


import com.garbagemule.MobArena.events.ArenaEndEvent;
import com.garbagemule.MobArena.events.ArenaPlayerDeathEvent;
import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import com.garbagemule.MobArena.events.ArenaPlayerLeaveEvent;
import com.garbagemule.MobArena.events.ArenaStartEvent;
import com.garbagemule.MobArena.framework.Arena;

public class PerksMobAreanaListener implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinArena(ArenaPlayerJoinEvent event) {
		
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaStartEvent(ArenaStartEvent event) {
		Arena arnea = event.getArena();
		for (Player player : arnea.getAllPlayers()) {
				player.setGameMode(GameMode.ADVENTURE);
		}
	}	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaEndEvent(ArenaEndEvent event) {
		Arena arnea = event.getArena();
		for (Player player : arnea.getAllPlayers()) {
			player.setGameMode(GameMode.SURVIVAL);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaDeathEvent(ArenaPlayerDeathEvent event) {
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaPlayerLeave(ArenaPlayerLeaveEvent event) {
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
}
