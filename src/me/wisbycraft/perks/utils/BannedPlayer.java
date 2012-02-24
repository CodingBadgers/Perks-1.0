package me.wisbycraft.perks.utils;

import org.bukkit.entity.Player;

public class BannedPlayer {

	private String m_player = null;
	private String m_reason = null;
	private Player m_staff = null;
	
	public BannedPlayer(String player, String reason, Player staff) {
		m_player = player;
		m_reason = reason;
		m_staff = staff;
	}
	
	public String getName() {
		return m_player;
	}
	
	public String getReason() {
		return m_reason;
	}
	
	public Player getStaff() {
		return m_staff;
	}
}
