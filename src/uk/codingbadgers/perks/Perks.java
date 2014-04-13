package uk.codingbadgers.perks;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.perks.admin.PerkAdmin;
import uk.codingbadgers.perks.admin.PerkClear;
import uk.codingbadgers.perks.admin.PerkDebug;
import uk.codingbadgers.perks.admin.PerkFun;
import uk.codingbadgers.perks.admin.PerkGameMode;
import uk.codingbadgers.perks.admin.PerkGroupSet;
import uk.codingbadgers.perks.admin.PerkInventory;
import uk.codingbadgers.perks.admin.PerkLookup;
import uk.codingbadgers.perks.admin.PerkStop;
import uk.codingbadgers.perks.admin.PerkThor;
import uk.codingbadgers.perks.admin.PerkTime;
import uk.codingbadgers.perks.admin.PerkTroll;
import uk.codingbadgers.perks.admin.PerkVanish;
import uk.codingbadgers.perks.admin.PerkWeather;
import uk.codingbadgers.perks.config.DatabaseManager;
import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.donator.AFKThread;
import uk.codingbadgers.perks.donator.PerkAFK;
import uk.codingbadgers.perks.donator.PerkDeathTP;
import uk.codingbadgers.perks.donator.PerkFlying;
import uk.codingbadgers.perks.donator.PerkHomeAndBuild;
import uk.codingbadgers.perks.donator.PerkSpawn;
import uk.codingbadgers.perks.donator.PerkTeleport;
import uk.codingbadgers.perks.listeners.PerksEntityListener;
import uk.codingbadgers.perks.listeners.PerksHeroChatListener;
import uk.codingbadgers.perks.listeners.PerksPlayerListener;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;
import uk.codingbadgers.perks.utils.PerkVault;

public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener();
    private final PerksEntityListener entityListener = new PerksEntityListener();
    private final PerksHeroChatListener hcListener = new PerksHeroChatListener();

    private AFKThread m_afkThread = null;
    
	public void onDisable() {
		m_afkThread.kill();
		DatabaseManager.Stop();
		PerkUtils.DebugConsole("Perks version '" + getDescription().getVersion() + "' has been disabled");
	}
	
	public void onEnable() {
		
		PerkUtils.plugin = this;
		
		// get the plugin manager
		PluginManager pm = getServer().getPluginManager();

		// register the 2 bukkit event listeners
		pm.registerEvents(playerListener, this);
		pm.registerEvents(entityListener, this);
		
		loadConfigs();
		loadDependencies(pm);
		
		m_afkThread = new AFKThread();
		m_afkThread.start();
		
		PerkUtils.DebugConsole("Perks version '" + getDescription().getVersion() + "' has been enabled");
		
	}
	
	private void loadConfigs() {
		// load the config
		// load before database incase we use sql
		if (!PerkConfig.loadConfig()) {
			PerkUtils.ErrorConsole("Could not load config");
		}
				
		// load the databases
		DatabaseManager.loadDatabases();
		
		
	}

	private void loadDependencies(PluginManager pm) {
		// setup vault
		if (pm.getPlugin("Vault") != null && PerkConfig.isDependencyEnabled("vault")) {
			PerkUtils.DebugConsole("Vault found, setting up vault dependency");
			PerkUtils.vaultEnabled = true;
			PerkVault.setupPerms();
		} else {
			PerkUtils.DebugConsole("Vault not found disabling vault stuff");
		}
				
		// setup herochat
		if (pm.getPlugin("Herochat") != null && PerkConfig.isDependencyEnabled("herochat")) {
			PerkUtils.DebugConsole("HeroChat found, setting up HeroChat dependency");
			pm.registerEvents(hcListener, this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] input) {

		if (PerkStop.onCommand(sender, cmd, commandLabel, input))
			return true;
		
		if (!(sender instanceof Player))
			return false;
		
		PerkPlayer player = playerListener.findPlayer((Player) sender);
		
		if (player == null)
			return false;
		
		List<String> flags = new ArrayList<String>();
		String[] parsedArgs = new String[input.length];

		// get the flags provided
		int k = 0;
		for (int i = 0; i < input.length; i ++) {	
			if (input[i].startsWith("-") && !input[i].matches("^[\\-0-9\\.]+,[\\-0-9\\.]+,[\\-0-9\\.]+(?:.+)?$")) {
				for (int x = 0; x < input[i].length(); x++) {
					if (input[i].charAt(x) == '+') 
						continue;
					flags.add(String.valueOf(input[i].charAt(x)));
				}
			} else if (input[i].length() > 0 && input[i] != null){
				parsedArgs[k] = input[i];
				k++;
			}
		}
		
		PerkArgSet args = new PerkArgSet(parsedArgs, flags);
		
		// handle fly commands
		if (PerkFlying.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handle teleport commands
		if (PerkTeleport.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles gamemode commands
		if (PerkGameMode.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles death commands
		if (PerkDeathTP.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles home and build commands
		if (PerkHomeAndBuild.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles vanish cmds
		if (PerkVanish.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles inv clear cmds
		if (PerkClear.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles item cmds
		if (PerkInventory.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles afk cmds
		if (PerkAFK.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles time cmds
		if (PerkTime.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles weather cmds
		if (PerkWeather.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles admin cmds
		if (PerkAdmin.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles debug cmds
		if (PerkDebug.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles spawn cmds
		if (PerkSpawn.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles lookup cmds
		if (PerkLookup.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles fun cmds
		if (PerkFun.onCommand(player, cmd, commandLabel, args))
			return true; 
		
		// handles thor cmds
		if (PerkThor.onCommnad(player, cmd, commandLabel, args))
			return true;
		
		// handles trolling cmds
		if (PerkTroll.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles group set cmd
		if (PerkGroupSet.onCommand(player, cmd, commandLabel, args))
			return true;
		
		return false;
	}

}