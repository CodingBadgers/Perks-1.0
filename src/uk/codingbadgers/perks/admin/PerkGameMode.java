package uk.codingbadgers.perks.admin;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

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
			}
			return true;
		}

		// checks the players gamemode
		if (cmd.getName().equalsIgnoreCase("gm")) {
			if (args.size() == 0) {
				if (player.hasPermission("perks.gamemode.check", true)) {
					// outputs the players gamemode
					PerkUtils.OutputToPlayer(player, "You are in " + player.getPlayer().getGameMode().toString().toLowerCase());
				}
			} else if (args.size() == 1) {
				if (args.getString(0).equalsIgnoreCase("creative") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.CREATIVE.getValue()))) {
					if (player.hasPermission("perks.gamemode.creative", true)) {
		                if (!(player.getPlayer().getGameMode() == GameMode.CREATIVE)) {
		                	player.getPlayer().setGameMode(GameMode.CREATIVE);
		                	PerkUtils.OutputToPlayer(player, "Now in creative mode");
		                } else {
		                	PerkUtils.OutputToPlayer(player, "You are already in creative mode");
		                }
					}
				} else if (args.getString(0).equalsIgnoreCase("survival") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.SURVIVAL.getValue()))) {
					if (player.hasPermission("perks.gamemode.survival", true)) {
						if (!(player.getPlayer().getGameMode() == GameMode.SURVIVAL)) {
							player.getPlayer().setGameMode(GameMode.SURVIVAL);
							PerkUtils.OutputToPlayer(player, "Now in survial mode");
		                } else {
		                    PerkUtils.OutputToPlayer(player, "You are already in survival mode");
		                }
					}
				} else if (args.getString(0).equalsIgnoreCase("adventure") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.ADVENTURE.getValue()))) {
					if (player.hasPermission("perks.gamemode.adventure", true)) {
						if (!(player.getPlayer().getGameMode() == GameMode.ADVENTURE)) {
							player.getPlayer().setGameMode(GameMode.ADVENTURE);
							PerkUtils.OutputToPlayer(player, "Now in adventure mode");
				        } else {
				        	PerkUtils.OutputToPlayer(player, "You are already in adventure mode");
				        }
					}
				} else {
				
					if (player.hasPermission("perks.gamemode.check.other", true)) {
						// this will check another players gamemode.
						Player otherPlayer = PerkUtils.getPlayer(args.getString(0)).getPlayer();
						if (otherPlayer == null) {
							PerkUtils.OutputToPlayer(player, "Could not find a player with the name " + args.getString(0));
						}				
						PerkUtils.OutputToPlayer(player, otherPlayer.getName()+ " is in " + otherPlayer.getGameMode().toString().toLowerCase());
					}
				}
			} else if (args.size() == 2) {
				Player target = PerkUtils.server().getPlayer(args.getString(1));
				
				if (target == null) {
					PerkUtils.OutputToPlayer(player, "That player is not online");
					return true;
				}
					 
				if (args.getString(0).equalsIgnoreCase("creative") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.CREATIVE.getValue()))) {
					if (player.hasPermission("perks.gamemode.creative", true)) {
		                if (!(player.getPlayer().getGameMode() == GameMode.CREATIVE)) {
		                	target.setGameMode(GameMode.CREATIVE);
		                	PerkUtils.OutputToPlayer(player, target.getName() + " is now in creative mode");
		                	PerkUtils.OutputToPlayer(target, "You are now in adventure mode");
		                } else {
		                	PerkUtils.OutputToPlayer(player, target.getName() + " is already in creative mode");
		                }
					}
				} else if (args.getString(0).equalsIgnoreCase("survival") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.SURVIVAL.getValue()))) {
					if (player.hasPermission("perks.gamemode.survival", true)) {
						if (!(player.getPlayer().getGameMode() == GameMode.SURVIVAL)) {
							target.setGameMode(GameMode.SURVIVAL);
							PerkUtils.OutputToPlayer(player, target.getName() + " is now in survial mode");
							PerkUtils.OutputToPlayer(target, "You are now in adventure mode");
		                } else {
		                    PerkUtils.OutputToPlayer(player, target.getName() + " is already in survival mode");
		                }
					}
				} else if (args.getString(0).equalsIgnoreCase("adventure") || args.getString(0).equalsIgnoreCase(String.valueOf(GameMode.ADVENTURE.getValue()))) {
					if (player.hasPermission("perks.gamemode.adventure", true)) {
						if (!(player.getPlayer().getGameMode() == GameMode.ADVENTURE)) {
							target.setGameMode(GameMode.ADVENTURE);
							PerkUtils.OutputToPlayer(player, target.getName() + " is now in adventure mode");
							PerkUtils.OutputToPlayer(target, "You are now in adventure mode");
				        } else {
				        	PerkUtils.OutputToPlayer(player, target.getName() + " is already in adventure mode");
				        }
					}
				}
			} else {
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
			}
			
			return true;			
		}
		
		// change another players gamemode

		// change to creative mode
		if (cmd.getName().equalsIgnoreCase("creative")) {
			if (player.hasPermission("perks.gamemode.creative", true)) {
                if (!(player.getPlayer().getGameMode() == GameMode.CREATIVE)) {
                	player.getPlayer().setGameMode(GameMode.CREATIVE);
                	PerkUtils.OutputToPlayer(player, "Now in creative mode");
                } else {
                	PerkUtils.OutputToPlayer(player, "You are already in creative mode");
                }
			}
			return true;
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
			}
			return true;
		}
		
		// change to adventure mode
		if (cmd.getName().equalsIgnoreCase("adventure")) {
			if (player.hasPermission("perks.gamemode.adventure", true)) {
				if (!(player.getPlayer().getGameMode() == GameMode.ADVENTURE)) {
					player.getPlayer().setGameMode(GameMode.ADVENTURE);
					PerkUtils.OutputToPlayer(player, "Now in adventure mode");
		        } else {
		        	PerkUtils.OutputToPlayer(player, "You are already in adventure mode");
		        }
			}
			return true;
		}

		return false;
	}
}
