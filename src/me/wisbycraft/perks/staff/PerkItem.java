package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.inventory.ItemStack;


public class PerkItem {
	
	public static Material getItem(String name) throws CommandException{
		
        if (PerkUtils.isNumeric(name)) {
        	int id = Integer.parseInt(name);
        	return Material.getMaterial(id);
        } else {
        	return Material.getMaterial(name);
        }
        
	}        
        
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) 
			throws CommandException{
		
		if (cmd.getName().equalsIgnoreCase("item") || cmd.getName().equalsIgnoreCase("i")) {
			
			if (!player.hasPermission("perks.item", true))
				return true;
						
			int amount = 1;
			
			if (args.length == 2) {
								
				try {
					amount = Integer.parseInt(args[1]);
				} catch(NumberFormatException ex) {
				}
			}
			
			Material item = getItem(args[0].toString());
			
			if (item == null) {
				PerkUtils.OutputToPlayer(player, "Could not find item.");
				return true;
			}
			
			player.getPlayer().getInventory().addItem(new ItemStack(item, amount));
			
			PerkUtils.OutputToPlayer(player, "You have been given " + amount + " " + item.toString() );
			
			return true;
		}
		
		return  false;
	}
	
}
