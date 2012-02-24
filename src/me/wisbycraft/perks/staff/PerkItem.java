package me.wisbycraft.perks.staff;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.inventory.ItemStack;


public class PerkItem {
	
	public static Material getItem(String name) throws CommandException{
		
		Material item = null;
		
        if (PerkUtils.isNumeric(name)) {
        	
        	int id = Integer.parseInt(name);
        	item = Material.getMaterial(id);
        	
        	if (item == null) 
        		throw new CommandException("Could not find item from id");
        } else {
        	
        	item = Material.getMaterial(name);
        	
        	if (item == null)
        		throw new CommandException("Could not find item from name");
        }
        
        return item;
	}        
        
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) 
			throws CommandException{
		
		if (cmd.getName().equalsIgnoreCase("item") || cmd.getName().equalsIgnoreCase("i")) {
			
			if (!player.hasPermission("perks.item", true))
				return true;
			
			if (args.length == 1) {
				
				Material item = getItem(args[0].toString());
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item.");
				}
				
				player.getPlayer().getInventory().addItem(new ItemStack(item));
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + "1"   + " " + item.toString() );
				
			} else if (args.length == 2) {
				
				int ammount = 0;
				
				try {
					
					ammount = Integer.parseInt(args[1]);
				} catch(NumberFormatException ex) {
					
					PerkUtils.OutputToPlayer(player, "Could not parse ammount.");
					return true;
				}
				
				Material item = getItem(args[0].toString());
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item.");
				}
				
				player.getPlayer().getInventory().addItem(new ItemStack((item), ammount));
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + ammount + " "+ item.toString() );
			}
			
			return true;
		}
		
		return  false;
	}
	
}
