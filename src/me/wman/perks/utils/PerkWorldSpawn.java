package me.wman.perks.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class PerkWorldSpawn {

	private World m_world;
	private Location m_spawn;
	
	public PerkWorldSpawn(World world, Location spawn){
		m_world = world;
		m_spawn = spawn;
	}
	
	public World getWorld() {
		return m_world;
	}
	
	public Location getSpawn() {
		return m_spawn;
	}
	
	public void setSpawn(Location spawn) {
		m_spawn = spawn;
	}
}
