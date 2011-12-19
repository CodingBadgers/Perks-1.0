package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkPlayer {

	private Player m_player = null;
	private SpoutPlayer m_spoutPlayer = null;

	private boolean m_flying = false;
	private boolean m_jumping = false;
	private boolean m_sneaking = false;
	private PerkMagicCarpet m_magicCarpet = null;

	public PerkPlayer(Player player) {
		m_player = player;
		m_spoutPlayer = (SpoutPlayer) player;

		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			m_magicCarpet = new PerkMagicCarpet();
		}
	}

	// called when a player is kicked or leaves...
	// all cleanups should be done in here
	public void remove() {
		if (m_magicCarpet != null) {
			m_magicCarpet.destroy();
		}
	}

	public Player getPlayer() {
		return m_player;
	}

	public SpoutPlayer getSpoutPlayer() {
		return m_spoutPlayer;
	}

	public void setFlying(boolean flying) {
		if (flying)
			PerkUtils.OutputToPlayer(this, "Fly mode is now enabled");
		else
			PerkUtils.OutputToPlayer(this, "Fly mode is now disabled");

		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			if (flying)
				m_magicCarpet.create(m_player);
			else
				m_magicCarpet.destroy();
		}

		m_flying = flying;
	}

	public boolean isFlying() {
		return m_flying;
	}

	public boolean isJumping() {
		return m_jumping;
	}

	public void setJumping(boolean jumping) {
		m_jumping = jumping;
	}

	public boolean isSneaking() {
		return m_sneaking;
	}

	public void setSneaking(boolean sneaking) {
		m_sneaking = sneaking;
	}

	public PerkMagicCarpet getMagicCarpet() {
		return m_magicCarpet;
	}

}
