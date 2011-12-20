package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PerkMagicCarpet {
	
	class CarpetBlock {
		public Location loc;
		public boolean placed = false;
		
		public CarpetBlock(Location loc, boolean placed) {
			this.loc = loc;
			this.placed = placed;
		}
	}
	
	private ArrayList<CarpetBlock> m_blocks = new ArrayList<CarpetBlock>();
	private Location m_location = null;

	public void create(Player player) {

		// get the players location to allocate a carpet around them
		Location loc = new Location(player.getWorld(), player.getLocation()
				.getX(), player.getLocation().getY() - 1, player
				.getLocation().getZ());
		
		m_location = loc;

		// allocate a 3 x 3 region
		for (int z = -1; z < 2; z++) {
			for (int x = -1; x < 2; x++) {
				Location l = new Location(loc.getWorld(), 
						loc.getX() + x,
						loc.getY(), 
						loc.getZ() + z);
				
				CarpetBlock cb = new CarpetBlock(l, false);
				
				m_blocks.add(cb);
			}
		}
	}

	public void destroy() {

		// set back to air and remove from our block of memory
		Iterator<CarpetBlock> itr = m_blocks.iterator();
		while (itr.hasNext()) {
			CarpetBlock cb = itr.next();
			if (cb.placed) {
				cb.loc.getBlock().setType(Material.AIR);
			}
		}

		m_blocks.clear();
	}

	public void remove() {

		// Set all blocks back to air
		Iterator<CarpetBlock> itr = m_blocks.iterator();
		while (itr.hasNext()) {
			CarpetBlock cb = itr.next();
			if (cb.placed) {
				cb.loc.getBlock().setType(Material.AIR);
				cb.placed = false;
			}
		}
	}

	public void positionAndShow(Location loc) {
		
		loc.setY(loc.getY()-1);
		
		// we havn't moved so don't update...
		if (m_location.equals(loc))
			return;
		
		m_location = loc;
		
		remove();

		Iterator<CarpetBlock> itr = m_blocks.iterator();

		for (int z = -1; z < 2; z++) {
			for (int x = -1; x < 2; x++) {
				CarpetBlock cb = itr.next();
				cb.loc.setX(loc.getX() + x);
				cb.loc.setY(loc.getY());
				cb.loc.setZ(loc.getZ() + z);

				if (cb.loc.getBlock().getTypeId() == 0) {
					cb.loc.getBlock().setType(Material.GLASS);
					cb.placed = true;
				}
			}
		}
	}
}