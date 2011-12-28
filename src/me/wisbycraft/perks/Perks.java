package me.wisbycraft.perks;

import org.bukkit.GameMode;
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
		
		PerkUtils.plugin = this;
		
		PluginManager pm = this.getServer().getPluginManager();

		// Player listeners
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Event.Priority.Normal, this);
		
        pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);

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
		
		// handle tp commands
		if (PerkTeleport.onCommand(player, cmd, commandLabel, args))
			return true;
		
		/*
		if (cmd.getName().equalsIgnoreCase("endcrystal")) {
			
			if (!player.hasPermission("perks.spawncrystal", true)) {
				return true;
			}
			
			Location eye = player.getPlayer().getTargetBlock(null, 100).getLocation().add(0.0f, 1.0f, 0.0f);
            if(eye.getBlock().isEmpty()) {
                eye.getWorld().spawn(eye, org.bukkit.entity.EnderCrystal.class);
                PerkUtils.OutputToPlayer(player, "Spawned an ender crystal");
            } else {
                PerkUtils.OutputToPlayer(player, "Something is blocking to block you are looking at");
            }
            return true;		
		}
		*/
		
		// needs moving
		if (cmd.getName().equalsIgnoreCase("gmtoggle")) {
			if (player.hasPermission("perks.gamemode.toggle", true)) {
				
				if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
					PerkUtils.OutputToPlayer(player, "Now in Survival Mode");
					player.getPlayer().setGameMode(GameMode.SURVIVAL);
				} else {
					PerkUtils.OutputToPlayer(player, "Now in Creative Mode");
					player.getPlayer().setGameMode(GameMode.CREATIVE);
				}
				
				return true;
			}
		}

		return false;
	}

}