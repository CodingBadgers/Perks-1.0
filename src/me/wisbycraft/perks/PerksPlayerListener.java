package me.wisbycraft.perks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerksPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and
									// the warning was annoying me

	public PerksPlayerListener(Perks plugin) {
		m_plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		PerkPlayer player = new PerkPlayer(event.getPlayer());
		PerkUtils.perkPlayers.add(player);
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
	}

	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		// handle flying...
		PerkFlying.fly(player, event);
	}

	@Override
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

	}

	// spout only
	public void onKeyPressedEvent(KeyPressedEvent event) {

		SpoutPlayer p = event.getPlayer();
		PerkPlayer player = PerkUtils.getPlayer(p);

		if (event.getKey() == p.getJumpKey()) {
			player.setJumping(true);
		}

		if (event.getKey() == p.getSneakKey()) {
			player.setSneaking(true);
		}
	}

	// spout only
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		SpoutPlayer p = event.getPlayer();
		PerkPlayer player = PerkUtils.getPlayer(p);

		if (event.getKey() == p.getJumpKey()) {
			player.setJumping(false);
		}

		if (event.getKey() == p.getSneakKey()) {
			player.setSneaking(false);
		}
	}

	// returns a PerkPlayer from a given Bukkit Player
	public PerkPlayer findPlayer(Player player) {
		return PerkUtils.getPlayer(player);
	}

}