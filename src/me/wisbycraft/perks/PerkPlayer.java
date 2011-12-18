package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkPlayer {
	
	private Player m_player = null;
	private SpoutPlayer m_spoutPlayer = null;
	
	private boolean m_flying = true;
	private boolean m_jumping = true;
	private boolean m_sneaking = true;
		
	public PerkPlayer(Player player) {
		m_player = player;
		m_spoutPlayer = (SpoutPlayer)player;
	}
	
	public Player getPlayer() {
		return m_player;
	}
	
	public SpoutPlayer getSpoutPlayer() {
		return m_spoutPlayer;
	}
	
	public void setFlying(boolean flying) {
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

}
