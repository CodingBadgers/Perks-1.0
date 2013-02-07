package uk.codingbadgers.perks;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.dynmap.DynmapAPI;

import uk.codingbadgers.perks.admin.*;
import uk.codingbadgers.perks.config.*;
import uk.codingbadgers.perks.donator.*;
import uk.codingbadgers.perks.listeners.*;
import uk.codingbadgers.perks.utils.*;

import com.onarandombox.MultiverseCore.api.MVWorldManager;


public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener();
    private final PerksEntityListener entityListener = new PerksEntityListener();
    private final PerksMobAreanaListener maListener = new PerksMobAreanaListener();
    private final PerksPvpArenaListener paListener = new PerksPvpArenaListener();
    private final PerksHeroChatListener hcListener = new PerksHeroChatListener();
    //private final PerksWebChatListener wcListener = new PerksWebChatListener();

	// private final PerkThread m_thread = new PerkThread(this);

	public void onDisable() {
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
		
		loadDependencies(pm);
		loadConfigs();
		
		// Set our thread going
		// m_thread.start();
		
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
		if (pm.getPlugin("Vault") != null) {
			PerkUtils.DebugConsole("Vault found, setting up vault dependency");
			PerkUtils.vaultEnabled = true;
			PerkVault.setupPerms();
		} else {
			PerkUtils.DebugConsole("Vault not found disabling vault stuff");
		}
		
		// setup dynmap
		if (pm.getPlugin("dynmap") != null) {
			PerkUtils.DebugConsole("Dynmap found, setting up Dynmap dependency");
			PerkUtils.dynmapapi = (DynmapAPI)pm.getPlugin("dynmap");
		}	
				
		// setup mob arena
		if (pm.getPlugin("MobArena") != null) {
			PerkUtils.DebugConsole("MobArena found, setting up MobArena dependency");
			pm.registerEvents(maListener, this);
			PerkMobArena.setupMobArenaHandler();
		}
				
		// setup pvparena
		if (pm.getPlugin("pvparena") != null) {
			PerkUtils.DebugConsole("PvpArena found, setting up PvpArena dependency");
			pm.registerEvents(paListener, this);
		}
				
		// setup multiverse
		if (pm.getPlugin("Multiverse") != null) {
			PerkUtils.DebugConsole("Multiverse found, setting up multiverse dependency");
			PerkUtils.worldManager = (MVWorldManager) pm.getPlugin("Multiverse-Core");
		}
				
		// setup herochat
		if (pm.getPlugin("Herochat") != null) {
			PerkUtils.DebugConsole("HeroChat found, setting up HeroChat dependency");
			pm.registerEvents(hcListener, this);
		}
		
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] input) {

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

		// handles promote commands
		if (PerkPromote.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles demote cmds
		if (PerkDemote.onCommand(player, cmd, commandLabel, args))
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
		
		// handles kit cmds
		if (PerkKits.onCommand(player, cmd, commandLabel, args))
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
		
		// handles spectate cmds
		if (PerkSpectate.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles fun cmds
		if (PerkFun.onCommand(player, cmd, commandLabel, args))
			return true; 
		
		// handles thor cmds
		if (PerkThor.onCommnad(player, cmd, commandLabel, args))
			return true;

		// handles dynmap cmds
		if (PerkDynmap.onCommand(player, cmd, commandLabel, args))
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