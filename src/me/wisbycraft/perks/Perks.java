package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener(this);
	private final PerksInputListener inputListener = new PerksInputListener(this, playerListener);
    private final PerksEntityListener entityListener = new PerksEntityListener(this);
    
    @SuppressWarnings("unused")
	private final PerkThread m_thread = new PerkThread(this);

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
        pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);

		// Input Listener
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inputListener, Event.Priority.Normal, this);
		
		// Set our thread going
		//m_thread.start();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		PerkPlayer player = playerListener.findPlayer((Player) sender);
		if (player == null)
			return false;

		// handle fly commands
		if (PerkFlying.onCommand(player, cmd, commandLabel, args))
			return true;

		return false;
	}

}