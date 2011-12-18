package me.wisbycraft.perks;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {
	
	private final PerksPlayerListener playerListener = new PerksPlayerListener(this);
	private final PerksInputListener inputListener = new PerksInputListener(this);

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		// Player listeners
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		
		// Input Listener
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inputListener, Event.Priority.Normal, this);
		
	}

}