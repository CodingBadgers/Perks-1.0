package me.wisbycraft.perks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.MobArenaHandler;

public class PerkMobArena {

	public static MobArenaHandler maHandler;
	
	public static void setupMobArenaHandler()
	{
	    Plugin maPlugin = (MobArena) Bukkit.getServer().getPluginManager().getPlugin("MobArena");
	    
	    if (maPlugin == null)
	        return;

	    maHandler = new MobArenaHandler();
	}
}
