package me.wisbycraft.perks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PerksPlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		PerkPlayer player = new PerkPlayer(event.getPlayer());
		PerkUtils.perkPlayers.add(player);
		
		if (player.hasPermission("perks.capes", false)) {
			PerkCapes.setCape(player.getPlayer());
		}
		
		PerkColors.addColor(player.getPlayer());
		
		// PerkVanish.vanishJoin(player, event);
	}	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
		// PerkVanish.vanishPlayerQuit(PerkUtils.getPlayer(event.getPlayer()), event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
		// PerkVanish.vanishPlayerKick(PerkUtils.getPlayer(event.getPlayer()), event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		// handle flying...
		PerkFlying.fly(player, event);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		Location from = event.getFrom();
		Location to = event.getTo();

		// stop people changing worlds without permission
		if (from.getWorld() != to.getWorld()) {
			String permission = "perks.changeworld." + to.getWorld().getName();
			if (!player.hasPermission(permission, false)) {
				PerkUtils.OutputToPlayer(player, "You don't have permission to enter that world");
				event.setCancelled(true);
				return;
			}

			Player bukkitPlayer = player.getPlayer();

			// if in creative and DOES NOT have permissions to keep inventory,
			// wipe inventory
			if (bukkitPlayer.getGameMode() == GameMode.CREATIVE) {
				if (!player.hasPermission("perks.keepinventonworldchange", false)) {
					bukkitPlayer.getInventory().clear();
					bukkitPlayer.setGameMode(GameMode.SURVIVAL);
				}
			}
		}
		
		if (player.hasPermission("perks.capes", false)) {
			PerkCapes.setCape(player.getPlayer());
		}
		
		PerkColors.addColor(player.getPlayer());

	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		// PerkVanish.vanishPlayerItemPickup(PerkUtils.getPlayer(event.getPlayer()), event);
	}
	
	// returns a PerkPlayer from a given Bukkit Player
	public PerkPlayer findPlayer(Player player) {
		return PerkUtils.getPlayer(player);
	}
}