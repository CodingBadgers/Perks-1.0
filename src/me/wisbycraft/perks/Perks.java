package me.wisbycraft.perks;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {
	
	private final PerksPlayerListener playerListener = new PerksPlayerListener(this);
	private final PerksInputListener inputListener = new PerksInputListener(this, playerListener);
        
        // loging stuff
        private static final Logger log = Logger.getLogger("minecraft");
        public String logprefix = "[Perks]";
        
        // enable/disable variables
        public String name;
        public String version;
        public Server server;

	@Override
	public void onDisable() {
                // Disable Variables
		server = getServer();
                PluginDescriptionFile pdf = this.getDescription();
                
                name = pdf.getName();
                version = pdf.getVersion();
                
                // Disable Text
                log.log(Level.INFO, logprefix + " " + name + " v: " + version + " has been disabled successfuly.");
	}

	@Override
	public void onEnable() {
                // enable variables
                server = getServer();
		PluginManager pm = server.getPluginManager();
                PluginDescriptionFile pdf = this.getDescription();
                
                name = pdf.getName();
                version = pdf.getVersion();
		
		// Player listeners
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
                pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, playerListener, Event.Priority.Normal, this);
		
		// Input Listener
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inputListener, Event.Priority.Normal, this);
                
                // Enable Text
                log.log(Level.INFO, logprefix + " " + name + " v: " + version + " has been enabled sucessfuly.");
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		PerkPlayer player = playerListener.findPlayer((Player)sender);
		if (player == null)
			return false;
		
		if(PerkFlying.onCommand(player, cmd, commandLabel, args))
			return true;
    	return false; 
    }



}