package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


/**
 * 
 * @author James
 */
public class PerkGameMode {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("gmtoggle")) {
			// permission perks.gamemode.toggle.<world_name> eg perks.gamemode.toggle.WisbyWorld
			if (player.hasPermission("perks.gamemode.toggle." + player.getPlayer().getWorld().getName(), true)) {

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

		// checks the players gamemode
		if (cmd.getName().equalsIgnoreCase("gm") || cmd.getName().equalsIgnoreCase("gamemode")) {
			if (args.length == 0) {
				if (player.hasPermission("perks.gamemode.check", true)) {
					// outputs the players gamemode
					PerkUtils.OutputToPlayer(player, "You are in " + player.getPlayer().getGameMode());
				}
			} else if (args.length == 1) {
				if (player.hasPermission("perks.gamemode.check.other", true)) {
					// this will check another players gamemode.
					Player otherPlayer = PerkUtils.getPlayer(args[0]).getPlayer();
					if (otherPlayer == null) {
						PerkUtils.OutputToPlayer(player, "Could not find a player with the name " + args[0]);
					}
					
					PerkUtils.OutputToPlayer(player, otherPlayer.getName()+ " is in " + otherPlayer.getGameMode());
				}
			}

			return true;			
		}

		// change to creative mode
		if (cmd.getName().equalsIgnoreCase("creative")) {
			// permission perks.gamemode.creative.<world_name> eg perks.gamemode.creative.WisbyWorld
			if (player.hasPermission("perks.gamemode.creative." + player.getPlayer().getWorld().getName(), true)) {
                if (!(player.getPlayer().getGameMode() == GameMode.CREATIVE)) {
                	player.getPlayer().setGameMode(GameMode.CREATIVE);
                	PerkUtils.OutputToPlayer(player, "Now in creative mode");
                } else {
                	PerkUtils.OutputToPlayer(player, "You are already in creative mode");
                }
				return true;
			}
		}

		// change to survival mode
		if (cmd.getName().equalsIgnoreCase("survival")) {
			// permission perks.gamemode.survival.<world_name> eg perks.gamemode.survival.WisbyWorld
			if (player.hasPermission("perks.gamemode.survival." + player.getPlayer().getWorld().getName(), true)) {
				if (!(player.getPlayer().getGameMode() == GameMode.SURVIVAL)) {
					player.getPlayer().setGameMode(GameMode.SURVIVAL);
					PerkUtils.OutputToPlayer(player, "Now in survial mode");
                } else {
                    PerkUtils.OutputToPlayer(player, "You are already in survival mode");
                }
				return true;
			}
		}

		return false;
	}
}
