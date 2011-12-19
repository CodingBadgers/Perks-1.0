package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerksPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and
									// the warning was annoying me

	private PerkPlayerArray perkPlayers = new PerkPlayerArray();

	public PerksPlayerListener(Perks plugin) {
		m_plugin = plugin;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		PerkPlayer player = new PerkPlayer(event.getPlayer());
		perkPlayers.add(player);
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		perkPlayers.removePlayer(event.getPlayer());
	}

	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		perkPlayers.removePlayer(event.getPlayer());
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {

		PerkPlayer player = perkPlayers.getPlayer(event.getPlayer());

		if (player == null)
			return;

		// handle flying...
		PerkFlying.fly(player, event);

	}

	public void onKeyPressedEvent(KeyPressedEvent event) {

		SpoutPlayer p = event.getPlayer();
		PerkPlayer player = perkPlayers.getPlayer(p);

		if (event.getKey() == p.getJumpKey()) {
			player.setJumping(true);
		}

		if (event.getKey() == p.getSneakKey()) {
			player.setSneaking(true);
		}

	}

	public void onKeyReleasedEvent(KeyReleasedEvent event) {

		SpoutPlayer p = event.getPlayer();
		PerkPlayer player = perkPlayers.getPlayer(p);

		if (event.getKey() == p.getJumpKey()) {
			player.setJumping(false);
		}

		if (event.getKey() == p.getSneakKey()) {
			player.setSneaking(false);
		}

	}

	public PerkPlayer findPlayer(Player player) {
		return perkPlayers.getPlayer(player);
	}

	public void onFoodLevelChange(FoodLevelChangeEvent event) {

		if (event.getFoodLevel() == 20) {
			event.setCancelled(true);
		}

		// i think the max is 20
		if (event.getFoodLevel() < 20) {
			event.setFoodLevel(20);
		}

		// once permissions are in this will come into play
		//
		// if (event.getFoodLevel() < 15) {
		// event.setFoodLevel(15);
		// }
		//
		// if (event.getFoodLevel() < 10) {
		// event.setFoodLevel(10);
		// }

	}

}