package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PerkMagicCarpet {
	
	ArrayList<Location> m_blocks = new ArrayList<Location>();
	Location m_location = null;

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
		
		loc.setY(loc.getY()-1);
		
		// we havn't moved so don't update...
		if (m_location.equals(loc))
			return;
		
		m_location = loc;
		
		remove();

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