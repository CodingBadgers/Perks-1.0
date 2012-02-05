package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Perks extends JavaPlugin {

	private final PerksPlayerListener playerListener = new PerksPlayerListener(this);
    private final PerksEntityListener entityListener = new PerksEntityListener(this);
    
	private final PerkThread m_thread = new PerkThread(this);

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		
		PerkUtils.plugin = this;
		
		PluginManager pm = this.getServer().getPluginManager();
		
		PerkUtils.spoutEnabled = pm.getPlugin("Spout") != null;

		pm.registerEvents(playerListener, this);
		pm.registerEvents(entityListener, this);
		
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
			player.getPlayer().sendMessage("1 - /fly, /mc - If you're using spout you will fly else a magic carpet will be created.");
			player.getPlayer().sendMessage("2 - /tpr <name> - Sends a teleport request to a player.");
			player.getPlayer().sendMessage("3 - Unlimited air under water when wearing a gold helmet.");
			player.getPlayer().sendMessage("4 - You're hunger decreases at a much slower rate.");
			
			return true;
		}
		
		// handle fly commands
		if (PerkFlying.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handle tp commands
		if (PerkTeleport.onCommand(player, cmd, commandLabel, args))
			return true;
		
		// handles gamemode cmds
		if (PerkGameMode.onCommand(player, cmd, commandLabel, args)) {
                    return true;
                }

		return false;
	}

}