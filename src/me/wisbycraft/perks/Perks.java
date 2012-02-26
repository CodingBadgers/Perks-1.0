package me.wisbycraft.perks;

import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.config.PerkConfig;
import me.wisbycraft.perks.donator.PerkAFK;
import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkDeathTP;
import me.wisbycraft.perks.donator.PerkFlying;
import me.wisbycraft.perks.donator.PerkHomeAndBuild;
import me.wisbycraft.perks.donator.PerkKits;
import me.wisbycraft.perks.donator.PerkList;
import me.wisbycraft.perks.donator.PerkTeleport;
import me.wisbycraft.perks.listeners.PerksEntityListener;
import me.wisbycraft.perks.listeners.PerksMobAreanaListener;
import me.wisbycraft.perks.listeners.PerksPlayerListener;
import me.wisbycraft.perks.staff.PerkClear;
import me.wisbycraft.perks.staff.PerkDemote;
import me.wisbycraft.perks.staff.PerkGameMode;
import me.wisbycraft.perks.staff.PerkItem;
import me.wisbycraft.perks.staff.PerkPromote;
import me.wisbycraft.perks.staff.PerkVanish;
import me.wisbycraft.perks.utils.PerkMobArena;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


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
		pm.registerEvents(maListener, this);
		
		/* 
		// setup vault
		if (PerkVault.setupPerms()) {
			PerkUtils.ErrorConsole("Could not find Vault, disabling plugin");
			pm.disablePlugin(this);
		}
		
		PerkVault.setupEco();
		*/
		
		// set up mob arena
		if (pm.getPlugin("MobArena") != null)
			PerkMobArena.setupMobArenaHandler();
		
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
			String commandLabel, String[] args) {

		PerkPlayer player = playerListener.findPlayer((Player) sender);
		
		if (player == null)
			return false;

		if (cmd.getName().equalsIgnoreCase("perks")) {
			
			PerkUtils.OutputToPlayer(player, "Perks consists of the following perks...");
			player.getPlayer().sendMessage("1 - /fly, /mc - If you're using spout you will fly else a magiccarpet.");
			player.getPlayer().sendMessage("2 - /tpr <name> - Sends a teleport request to a player.");
			player.getPlayer().sendMessage("3 - Unlimited air under water when wearing a gold helmet.");
			player.getPlayer().sendMessage("4 - You're hunger decreases at a much slower rate.");
			player.getPlayer().sendMessage("5 - /death - to teleport to your last death location.");
			player.getPlayer().sendMessage("6 - Capes for spoutcraft users donator and above");
			player.getPlayer().sendMessage("7 - /tphr <name> - Sends a teleport here request to a player");
			
			return true;
		}
		
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

		// handles promote cmds
		if (PerkDemote.onCommand(player, cmd, commandLabel, args))
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
		
		return false;
	}

}