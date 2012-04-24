package me.wisbycraft.perks.admin;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkThor {

	private static final Random random = new Random();
	
	public static void onPlayerInteract (PerkPlayer player, PlayerInteractEvent event) {
		
		if (player.isThorEnabled()) {

			ItemStack hand = event.getItem();
			
			if (player.getThorHammer() == null)
				return;
			
			if (hand.getType() != player.getThorHammer())
				return;
			
			if (event.getAction() == Action.LEFT_CLICK_AIR) {
				if (player.getThorAmmount() != 1) {
					
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
					
				} else {
					Block block = player.getPlayer().getTargetBlock(null, 300);
	                if (block != null) {
	                	Location loc = block.getLocation();
	                    loc.setY(block.getLocation().getY() + 1);
	                    loc.getWorld().strikeLightning(loc);
	                    return;
	                }
				}
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            	if (player.getThorAmmount() != 1) {
					
					for (int i = 0; i < player.getThorAmmount(); i++) {
						Block block = event.getClickedBlock();
		                Location loc = block.getLocation();
	                    loc.setY(block.getLocation().getY());
		                
                        loc.setX(loc.getX() + random.nextDouble() * 20 - 10);
                        loc.setZ(loc.getZ() + random.nextDouble() * 20 - 10);
                        
                        player.getPlayer().getLocation().getWorld().strikeLightning(loc);
					}
					return;
					
				} else {
            	
	            	Block block = event.getClickedBlock();
	                Location loc = block.getLocation();
	                loc.setY(block.getLocation().getY() + 1);
	                loc.getWorld().strikeLightning(loc);
	                return;
				}
            }
		}
	}
	
	public static boolean onCommnad(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("thor")) {
			
			if (!player.hasPermission("perks.thor.hammer", true))
				return true;
			
			if (args.size() == 0) {
				
				if (player.isThorEnabled()) {
					
					player.setThor(false);
					PerkUtils.OutputToPlayer(player, "The power of thors hammer has been removed");
					player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(), 
																			Effect.MOBSPAWNER_FLAMES,
																			new Random().nextInt(9));
					player.setThorHammer(Material.AIR);
					player.setThorAmmount(0);
					return true;
				} else {
					
					player.setThor(true);
					PerkUtils.OutputToPlayer(player, "You have been given thors hammer, use it wisely");
					player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(), 
																			Effect.MOBSPAWNER_FLAMES,
																			new Random().nextInt(9));
					player.setThorHammer(player.getPlayer().getItemInHand().getType());
					player.setThorAmmount(1);
					return true;
				}
			} 
			
			if (args.size() == 1) {
				
				if (player.isThorEnabled()) {
					
					player.setThor(false);
					PerkUtils.OutputToPlayer(player, "The power of thors hammer has been removed");
					player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(), 
																			Effect.MOBSPAWNER_FLAMES,
																			new Random().nextInt(9));
					player.setThorHammer(Material.AIR);
					player.setThorAmmount(0);
					return true;
				} else {
					
					int ammount;
					try {
						ammount = (args.getInt(0) > 50) ? 50 : args.getInt(0);
					} catch(NumberFormatException ex) {
						ammount = 1;
					}
					player.setThorAmmount(ammount);
					player.setThor(true);
					PerkUtils.OutputToPlayer(player, "You have been given thors hammer, use it wisely");
					player.getPlayer().getLocation().getWorld().playEffect(player.getPlayer().getLocation(), 
																			Effect.MOBSPAWNER_FLAMES,
																			new Random().nextInt(9));
					player.setThorHammer(player.getPlayer().getItemInHand().getType());
					return true;
				}
			}
		}
		
		if (commandLabel.equalsIgnoreCase("shock")) {
			
			if (!player.hasPermission("perks.thor.shock", true))
				return true;
			
			boolean silent = false;
			if (args.hasFlag("s"))
				silent = true;
			
			PerkPlayer target;
			if (args.size() != 1) {
				target = player;
			} else {
				target = PerkUtils.getPlayer(args.getString(0));
				if (target == null) {
					PerkUtils.OutputToPlayer(player, args.getString(0) + " is not online");
					return true;
				}
			}
			
			if (!silent)
				PerkUtils.OutputToAll(player.getPlayer().getName() + " has shocked " + target.getPlayer().getName());
			
			if (target != player)
				PerkUtils.OutputToPlayer(target, "You have been shocked by " + player.getPlayer().getName());
			PerkUtils.OutputToPlayer(player, "You have shocked " + (target != player ? target.getPlayer().getName() : "Yourself"));
			
			target.getPlayer().getLocation().getWorld().strikeLightning(target.getPlayer().getLocation());
			
			return true;
		}
		
		return false;
	}
}
