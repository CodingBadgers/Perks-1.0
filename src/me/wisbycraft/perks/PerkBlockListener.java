package me.wisbycraft.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PerkBlockListener implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak (BlockBreakEvent event) {
		// PerkVanish.vanishBlockBreak(PerkUtils.getPlayer(event.getPlayer()), event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace (BlockPlaceEvent event) {
		// PerkVanish.vanishBlockPlace(PerkUtils.getPlayer(event.getPlayer()), event);
	}
	
}
