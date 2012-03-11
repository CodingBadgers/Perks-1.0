package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.admin.PerkSpectate;
import me.wisbycraft.perks.admin.PerkVanish;
import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkColors;
import me.wisbycraft.perks.donator.PerkFlying;
import me.wisbycraft.perks.donator.PerkList;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
		
		PerkVanish.vanishJoin(player, event);
		
		PerkColors.addColor(player.getPlayer());
		
		if (event.getPlayer() instanceof Player) {
			PerkList.showOnlineList(player);
		}

	}	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		Player bPlayer = event.getPlayer();
		if (bPlayer == null)
			return;
		
		PerkPlayer player = PerkUtils.getPlayer(bPlayer);
		if (player == null)
			return;
		
		player.showPlayer(false);
		PerkUtils.perkPlayers.removePlayer(bPlayer);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		PerkUtils.getPlayer(event.getPlayer()).showPlayer(false);
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		// handle flying...
		PerkFlying.fly(player, event);
		
		/* Handle afk moving */
		if (player.isAfk()) {
			Location from = event.getFrom();
			Location to = from;
			event.setTo(to);
		}
		
		/* handle spectate */		
		if (PerkSpectate.isBeingFolowed(player)) {
			PerkPlayer stalker = player.getSpecatingPlayer();
			stalker.teleport(player.getPlayer().getLocation());
		}

		// stop the stalker moving
		if (player.isSpectating()) {
			event.setTo(event.getFrom());
		}
		
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
	
	// returns a PerkPlayer from a given Bukkit Player
	public PerkPlayer findPlayer(Player player) {
		return PerkUtils.getPlayer(player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player.isAfk()) {
			
			event.setCancelled(true);
		}
		
		if (player.isHidden()) {
			
			event.setCancelled(true);
		}
		
	}
}