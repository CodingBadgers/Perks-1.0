package uk.codingbadgers.perks.admin;

import java.util.Random;


import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.codingbadgers.perks.config.DatabaseManager;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkVanish {
	
	public static void vanishJoin (PerkPlayer player, PlayerJoinEvent event) {

		if (DatabaseManager.isVanished(player)) {
			player.hidePlayer(false);			
		}
		
		if (player.hasPermission("perks.vanish.view", false))
			return;
		
		Player[] players = PerkUtils.server().getOnlinePlayers();
		for (int i = 0; i < players.length; ++i)
		{
			PerkPlayer p = PerkUtils.getPlayer(players[i]);
			
			if (p == player)
				continue;
			
			if (p.isVanished()) {
				player.getPlayer().hidePlayer(p.getPlayer());
			}			
		}
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (cmd.getName().equalsIgnoreCase("vanish")) {
								 
			if (!player.hasPermission("perks.vanish", true))
				return true;
			
			Random rand = new Random();
				
			if (args.hasFlag('s')) {
				player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(),
																		Effect.SMOKE,
																		rand.nextInt(18));
			} 
			
			if (args.hasFlag('l')) {
				player.getPlayer().getLocation().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
			} 
			
			if (args.hasFlag('f')) {
				player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(),
																		Effect.MOBSPAWNER_FLAMES,
																		rand.nextInt(18));
			}
			
			if (player.isVanished()) {
				
				player.showPlayer(true);
				removePlayer(player);
			} else {

				player.hidePlayer(true);
				addPlayer(player);
			}
			
			return true;
		}
		
		return false;
		
	}
	
	public static void addPlayer(PerkPlayer player) {
		DatabaseManager.addVanishPlayer(player);
	}
	
	public static void removePlayer(PerkPlayer player) {
		DatabaseManager.removeVanishPlayer(player);
	}

}
