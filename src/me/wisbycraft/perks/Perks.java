package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener();
    private final PerksEntityListener entityListener = new PerksEntityListener();
    private final PerkBlockListener blockListener = new PerkBlockListener();
    
	private final PerkThread m_thread = new PerkThread(this);

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
		pm.registerEvents(blockListener, this);
		
		/* 
		// setup vault
		if (PerkVault.setupPerms()) {
			PerkUtils.ErrorConsole("Could not find Vault, disabling plugin");
			pm.disablePlugin(this);
		}
		
		PerkVault.setupEco();
		*/
		
		// set up mob arena
		PerkMobArena.setupMobArenaHandler();
		
		// load the homes from the database
		DatabaseManager.LoadHomes();
		
		// load the config
		if (!PerkConfig.loadConfig()) {
			PerkUtils.ErrorConsole("Could not load config");
		}
		
		// Set our thread going
		m_thread.start();
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

		return false;
	}

}