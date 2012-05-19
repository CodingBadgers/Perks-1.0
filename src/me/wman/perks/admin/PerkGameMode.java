package me.wman.perks.admin;

import me.wman.perks.utils.PerkArgSet;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PerkGameMode {

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
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

		// checks the players gamemode
		if (cmd.getName().equalsIgnoreCase("gm") || cmd.getName().equalsIgnoreCase("gamemode")) {
			if (args.size() == 0) {
				if (player.hasPermission("perks.gamemode.check", true)) {
					// outputs the players gamemode
					PerkUtils.OutputToPlayer(player, "You are in " + player.getPlayer().getGameMode());
				}
			} else if (args.size() == 1) {
				if (player.hasPermission("perks.gamemode.check.other", true)) {
					// this will check another players gamemode.
					Player otherPlayer = PerkUtils.getPlayer(args.getString(0)).getPlayer();
					if (otherPlayer == null) {
						PerkUtils.OutputToPlayer(player, "Could not find a player with the name " + args.getString(0));
					}
					
					PerkUtils.OutputToPlayer(player, otherPlayer.getName()+ " is in " + otherPlayer.getGameMode());
				}
			}

			return true;			
		}

		// change to creative mode
		if (cmd.getName().equalsIgnoreCase("creative")) {
			if (player.hasPermission("perks.gamemode.creative", true)) {
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
			if (player.hasPermission("perks.gamemode.survival", true)) {
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