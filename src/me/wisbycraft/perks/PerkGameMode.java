package me.wisbycraft.perks;

import org.bukkit.GameMode;
import org.bukkit.command.Command;

/**
 *
 * @author James
 */
public class PerkGameMode {
    
    public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
        
        // toggles the plays gamemode
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
