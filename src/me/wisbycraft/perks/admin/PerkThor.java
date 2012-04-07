package me.wisbycraft.perks.admin;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkThor {

	private static final Random random = new Random();
	
	public static void shock(PerkPlayer player) {
		shock(player.getPlayer().getLocation());
	}
	
	public static void shock(Location location) {
		location.getWorld().strikeLightning(location);
	}
	
	public static void shockEffect(PerkPlayer player) {
		shockEffect(player.getPlayer().getLocation());
	}
	
	public static void shockEffect(Location location) {
		location.getWorld().strikeLightningEffect(location);
	}
	
	public static void onPlayerInteract (PerkPlayer player, PlayerInteractEvent event) {
		
		if (player.isThorEnabled()) {

			ItemStack hand = event.getItem();
			
			if (player.getThorHammer() == null)
				return;
			
			if (hand.getType() != player.getThorHammer())
				return;
			
			if (event.getAction() == Action.LEFT_CLICK_AIR) {
				if (player.getThorAmmount() != 0) {
					
					for (int i = 0; i < player.getThorAmmount(); i++) {
						Block block = player.getPlayer().getTargetBlock(null, 300);
		                if (block == null) 
		                	return;
		                Location loc = block.getLocation();
	                    loc.setY(block.getLocation().getY() + 1);
		                
                        loc.setX(loc.getX() + random.nextDouble() * 20 - 10);
                        loc.setZ(loc.getZ() + random.nextDouble() * 20 - 10);
                        
                        player.getPlayer().getLocation().getWorld().strikeLightning(loc);
					}
					return;
					
				}
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            	if (player.getThorAmmount() != 0) {
					
					for (int i = 0; i < player.getThorAmmount(); i++) {
						Block block = event.getClickedBlock();
		                Location loc = block.getLocation();
	                    loc.setY(block.getLocation().getY() + 1);
		                
                        loc.setX(loc.getX() + random.nextDouble() * 20 - 10);
                        loc.setZ(loc.getZ() + random.nextDouble() * 20 - 10);
                        
                        player.getPlayer().getLocation().getWorld().strikeLightning(loc);
					}
					return;
					
				}
            }
		}
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args, List<String> flags) {
		
		if (commandLabel.equalsIgnoreCase("thor")) {
			
			if (!player.hasPermission("perks.thor.hammer", true))
				return true;
			
			if (player.isBlacklisted(true))
				return true;
	
			int ammount = 1; 
			if (args.length >= 1) {
				try {
					ammount = Integer.parseInt(args[1]);
				} catch (NumberFormatException ex) {
					ammount = 1;
				}
			}
				
				if (player.isThorEnabled()) {
					
					player.setThor(false);
					PerkUtils.OutputToPlayer(player, "The power of thors hammer has been removed");
					shockEffect(player.getPlayer().getLocation());
					player.setThorHammer(Material.AIR);
					player.setThorAmmount(0);
					return true;
				} else {
					
					player.setThor(true);
					PerkUtils.OutputToPlayer(player, "You have been given thors hammer, use it wisely");
					shockEffect(player.getPlayer().getLocation());
					player.setThorHammer(player.getPlayer().getItemInHand().getType());
					player.setThorAmmount(ammount);
					return true;
				}

		}
		
		if (commandLabel.equalsIgnoreCase("shock")) {
			
			if (!player.hasPermission("perks.thor.shock", true))
				return true;
			
			if (player.isBlacklisted(true))
				return true;
			
			boolean kill = false;
			boolean silent = false;
			
			if (flags.contains("k"))
				kill = true;
			
			if (flags.contains("s"))
				silent = true;
			
			PerkPlayer target = PerkUtils.getPlayer(args[0]);
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "No player with that name is online.");
				return true;
			}
			
			if (!silent)
				PerkUtils.OutputToAll(player.getPlayer().getName() + " has shocked " + target.getPlayer().getName());
			
			PerkUtils.OutputToPlayer(player, "You have shocked " + target.getPlayer().getName());
			PerkUtils.OutputToPlayer(target, player.getPlayer().getName() + " has shocked you");
				
			if (kill)
				target.getPlayer().setHealth(0);
			
			shock (target);
			
			return true;
		}
		
		return false;
	}
}
