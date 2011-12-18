package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkPlayer {
	
	private Player m_player = null;
	private SpoutPlayer m_spoutPlayer = null;
	
	private boolean m_flying = false;
	private boolean m_jumping = false;
	private boolean m_sneaking = false;
	private MagicCarpet m_magicCarpet = null;
		
	public PerkPlayer(Player player) {
		m_player = player;
		m_spoutPlayer = (SpoutPlayer)player;
		
		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			m_magicCarpet = new MagicCarpet();
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
			m_player.sendMessage("[Perks] Fly mode is now enabled.");
		else
			m_player.sendMessage("[Perks] Fly mode is now disabled.");
		
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
	
	public MagicCarpet getMagicCarpet() {
		return m_magicCarpet;
	}
	
	public class MagicCarpet {
		
		ArrayList<Location> m_blocks = new ArrayList<Location>();
		
		public MagicCarpet() {
				
		}
		
		public void destroy() {
			Iterator<Location> itr = m_blocks.iterator();
			while(itr.hasNext()) {
				Location l = itr.next();
				l.getBlock().setType(Material.AIR);
			}
		}
				
		public void positionAndShow(Location loc) {
			
			destroy();
			
			loc = loc.add(0, -1, 0);
			
			for (int z = -1; z < 2; z++) {
				for (int x = -1; x < 2; x++) {
					Location l = new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z);
					
					if (l.getBlock().isEmpty()) {
						l.getBlock().setType(Material.GLASS);
						m_blocks.add(l);
					}
				}
			}
			
		}
		
	}

}
