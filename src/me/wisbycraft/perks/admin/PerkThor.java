package me.wisbycraft.perks.admin;

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

	public static void shock(PerkPlayer player) {
		shock(player.getPlayer().getLocation());
	}
	
	public static void shock(Location location) {
		location.getWorld().strikeLightning(location);
	}
	
	public static void shockEffect(PerkPlayer player) {
		shock(player.getPlayer().getLocation());
	}
	
	public static void shockEffect(Location location) {
		location.getWorld().strikeLightningEffect(location);
	}
	
	public static void onPlayerInteract (PerkPlayer player, PlayerInteractEvent event) {
		
		if (player.isThorEnabled()) {

			ItemStack hand = event.getItem();
			
			if (hand.getType() == Material.WOOD_PICKAXE 
					|| hand.getType() == Material.IRON_PICKAXE 
					|| hand.getType() == Material.STONE_PICKAXE 
					|| hand.getType() == Material.DIAMOND_PICKAXE)
				return;
			
			if (event.getAction() == Action.LEFT_CLICK_AIR) {
                Block block = player.getPlayer().getTargetBlock(null, 300);
                if (block != null) {
                	PerkThor.shock(block.getLocation());
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                PerkThor.shock(block.getLocation());
            }
		}
	}
	
	public static boolean onCommnad(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("thor")) {
			
			if (!player.hasPermission("perks.thor.hammer", true))
				return true;
			
			if (args.length == 0) {
				
				if (player.isThorEnabled()) {
					
					player.setThor(false);
					PerkUtils.OutputToPlayer(player, "The power of thors hammer has been removed");
					shockEffect(player.getPlayer().getLocation());
					return true;
				} else {
					
					player.setThor(true);
					PerkUtils.OutputToPlayer(player, "You have been given thors hammer, use it wisely");
					shockEffect(player.getPlayer().getLocation());
					return true;
				}
			} 
			
			if (args.length == 1) {
				PerkPlayer target = PerkUtils.getPlayer(args[0]);
				
				if (target == null) {
					PerkUtils.OutputToPlayer(player, "No player with that name is online.");
					return true;
				}
				
				if (target.isThorEnabled()) {
					
					target.setThor(false);
					PerkUtils.OutputToPlayer(target, "The power of thors hammer has been removed by " + player.getPlayer().getName());
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has had thors hammer removed");
					shockEffect(player.getPlayer().getLocation());
					return true;
				} else {
					
					player.setThor(true);
					PerkUtils.OutputToPlayer(target, "You have been given thors hammer, use it wisely");
					PerkUtils.OutputToPlayer(player, target.getPlayer().getName() + " has had thors hammer given to them");
					shockEffect(player.getPlayer().getLocation());
					return true;
				}
			}
		}
		
		if (commandLabel.equalsIgnoreCase("shock")) {
			
			if (!player.hasPermission("perks.thor.shock", true))
				return true;
			
			PerkPlayer target = PerkUtils.getPlayer(args[0]);
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "No player with that name is online.");
				return true;
			}
			
			shock (target);
			PerkUtils.OutputToAll(player.getPlayer().getName() + " has shocked " + target.getPlayer().getName());
			PerkUtils.OutputToPlayer(player, "You have shocked " + target.getPlayer().getName());
			return true;
		}
		
		return false;
	}
}
