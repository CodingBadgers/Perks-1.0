package uk.codingbadgers.perks.listeners;

import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.events.PAEndEvent;
import net.slipcor.pvparena.events.PAJoinEvent;
import net.slipcor.pvparena.events.PALeaveEvent;
import net.slipcor.pvparena.events.PAStartEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerksPvpArenaListener implements Listener {

	private void validatePlayer(Player p) {
		
		PerkPlayer player = PerkUtils.getPlayer(p);
		
		if (player.isFlying()) {
			player.setFlying(false);
			PerkUtils.OutputToPlayer(player, "Your fly mode has been disabled while you are in the arena");
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PAJoinEvent event) {
		validatePlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaStartEvent(PAStartEvent event) {
		for (ArenaPlayer player : event.getArena().getEveryone()) {
			validatePlayer(player.get());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaEndEvent(PAEndEvent event) {
		for (ArenaPlayer player : event.getArena().getEveryone()) {
			validatePlayer(player.get());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onArenaPlayerLeave(PALeaveEvent event) {

	}
}
