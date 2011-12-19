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
		m_spoutPlayer = (SpoutPlayer) player;

		if (!m_spoutPlayer.isSpoutCraftEnabled()) {
			m_magicCarpet = new MagicCarpet();
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

	public MagicCarpet getMagicCarpet() {
		return m_magicCarpet;
	}

	public class MagicCarpet {

		ArrayList<Location> m_blocks = new ArrayList<Location>();

		public MagicCarpet() {

		}

		public void create(Player player) {

			// get the players location to allocate a carpet around them
			Location loc = new Location(player.getWorld(), player.getLocation()
					.getX(), player.getLocation().getY() - 1, player
					.getLocation().getZ());

			// allocate a 3 x 3 region
			for (int z = -1; z < 2; z++) {
				for (int x = -1; x < 2; x++) {
					Location l = new Location(loc.getWorld(), loc.getX() + x,
							loc.getY(), loc.getZ() + z);
					m_blocks.add(l);
				}
			}
		}

		public void destroy() {

			// set back to air and remove from our block of memory
			Iterator<Location> itr = m_blocks.iterator();
			while (itr.hasNext()) {
				Location l = itr.next();
				l.getBlock().setType(Material.AIR);
			}

			m_blocks.clear();
		}

		public void remove() {

			// Set all blocks back to air
			Iterator<Location> itr = m_blocks.iterator();
			while (itr.hasNext()) {
				Location l = itr.next();
				if (l.getBlock().getType() == Material.GLASS) {
					l.getBlock().setType(Material.AIR);
				}
			}
		}

		public void positionAndShow(Location loc) {

			remove();

			loc = loc.add(0, -1, 0);

			Iterator<Location> itr = m_blocks.iterator();

			for (int z = -1; z < 2; z++) {
				for (int x = -1; x < 2; x++) {
					Location l = itr.next();
					l.setX(loc.getX() + x);
					l.setY(loc.getY());
					l.setZ(loc.getZ() + z);

					if (l.getBlock().getTypeId() == 0) {
						l.getBlock().setType(Material.GLASS);
					}
				}
			}

		}

	}

}
