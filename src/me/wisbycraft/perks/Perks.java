package me.wisbycraft.perks;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {
	
	private final PerksPlayerListener playerListener = new PerksPlayerListener(this);

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		
	}

}