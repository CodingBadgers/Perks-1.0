package me.wisbycraft.perks;

import java.util.ArrayList;
import java.util.List;

import me.wisbycraft.perks.admin.PerkAdmin;
import me.wisbycraft.perks.admin.PerkClear;
import me.wisbycraft.perks.admin.PerkDebug;
import me.wisbycraft.perks.admin.PerkDemote;
import me.wisbycraft.perks.admin.PerkFun;
import me.wisbycraft.perks.admin.PerkGameMode;
import me.wisbycraft.perks.admin.PerkItem;
import me.wisbycraft.perks.admin.PerkLookup;
import me.wisbycraft.perks.admin.PerkPromote;
import me.wisbycraft.perks.admin.PerkSpectate;
import me.wisbycraft.perks.admin.PerkThor;
import me.wisbycraft.perks.admin.PerkTime;
import me.wisbycraft.perks.admin.PerkVanish;
import me.wisbycraft.perks.admin.PerkWeather;
import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.config.PerkConfig;
import me.wisbycraft.perks.donator.PerkAFK;
import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkDeathTP;
import me.wisbycraft.perks.donator.PerkFlying;
import me.wisbycraft.perks.donator.PerkHomeAndBuild;
import me.wisbycraft.perks.donator.PerkKits;
import me.wisbycraft.perks.donator.PerkList;
import me.wisbycraft.perks.donator.PerkSpawn;
import me.wisbycraft.perks.donator.PerkTeleport;
import me.wisbycraft.perks.listeners.PerksEntityListener;
import me.wisbycraft.perks.listeners.PerksMobAreanaListener;
import me.wisbycraft.perks.listeners.PerksPlayerListener;
import me.wisbycraft.perks.utils.PerkMobArena;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;
import me.wisbycraft.perks.utils.PerkVault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.onarandombox.MultiverseCore.api.MVWorldManager;


public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener();
    private final PerksEntityListener entityListener = new PerksEntityListener();
    private final PerksMobAreanaListener maListener = new PerksMobAreanaListener();
    
	// private final PerkThread m_thread = new PerkThread(this);

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		
		PerkUtils.plugin = this;
		
		// get the plugin manager
		PluginManager pm = this.getServer().getPluginManager();
		
		// decide wether spout is enabled or not
		PerkUtils.spoutEnabled = pm.getPlugin("Spout") != null;

		// register the 3 event listeners
		pm.registerEvents(playerListener, this);
		pm.registerEvents(entityListener, this);
		
		// setup vault
		if (pm.getPlugin("Vault") != null) {
			PerkUtils.vaultEnabled = true;
			PerkVault.setupPerms();
		} else {
			PerkUtils.DebugConsole("Vault not found disabling vault stuff");
		}
		
        PerkUtils.dynmapapi = (DynmapAPI)pm.getPlugin("dynmap");
		
		// set up mob arena
		if (pm.getPlugin("MobArena") != null) {
			pm.registerEvents(maListener, this);
			PerkMobArena.setupMobArenaHandler();
		}
		
		// check for multiverse
		if (pm.getPlugin("Multiverse") != null) {
			PerkUtils.worldManager = (MVWorldManager) pm.getPlugin("Multiverse-Core");
		}
		
		// load the homes from the database
		DatabaseManager.loadDatabases();
		
		// load the config
		if (!PerkConfig.loadConfig()) {
			PerkUtils.ErrorConsole("Could not load config");
		}
		
		// Set our thread going
		// m_thread.start();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] input) {

		if (!(sender instanceof Player))
			return false;
		
		PerkPlayer player = playerListener.findPlayer((Player) sender);
		
		if (player == null)
			return false;
		
		List<String> flags = new ArrayList<String>();
		/* for some reason this is fucking up, will look into it later
		 * It ends up defaulting to always having one argument on the command
		 * and i think it is causing the error of picking a random player
		String arguments = "";
		
		boolean first = true;
		// get the flags provided
		for (int i = 0; i < input.length; i ++) {
			
			if (input[i].startsWith("-")) {
				for (int x = 0; x < input[i].length(); x++) {
					if (input[i].charAt(x) == '-') 
						continue;
					flags.add(String.valueOf(input[i].charAt(x)));
				}
			} else if (input[i].length() > 0 && input[i] != " "){
				if (!first) {
					arguments += ", ";
				}
				arguments += input[i];
				first = false;
			}
		}
		
		arguments = arguments.trim();
		
		String[] args = arguments.split(",");

		// Debug //
		for (String string : args) {
			PerkUtils.DebugConsole(string + ", ");
		}
		PerkUtils.DebugConsole(String.valueOf(args.length));
		PerkUtils.DebugConsole(arguments);
		PerkUtils.DebugConsole(String.valueOf(arguments.length()));
		*/
		
		String[] args = input;
		// handle fly commands
		
		if (PerkFlying.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handle tp commands
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
		
		// handles cape and color commands
		if (PerkCapes.onCommand(player, cmd, commandLabel, args))
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
		
		// handles list cmds
		if (PerkList.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles inv clear cmds
		if (PerkClear.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles item cmds
		if (PerkItem.onCommand(player, cmd, commandLabel, args))
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
		
		return false;
	}

}