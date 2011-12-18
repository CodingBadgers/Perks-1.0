package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkPlayer {
	
	private Player m_player = null;
	private SpoutPlayer m_spoutPlayer = null;
	private boolean m_flying = true;
	
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

}
