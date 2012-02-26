package me.wisbycraft.perks.admin;

import java.util.HashMap;
import java.util.Map;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.inventory.ItemStack;

public class PerkItem {
	
	public static Map<String, Integer> itemNames = new HashMap<String, Integer>();
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
				
				String name = args[0].toLowerCase();
				ItemInfo iteminfo = Items.itemByName(name);
				
				if (iteminfo == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item.");
				}
				
				ItemStack item = iteminfo.toStack();
				item.setAmount(1);
				
				player.getPlayer().getInventory().addItem(item);
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + "1"   + " " + item.toString() );
				
			} else if (args.length == 2) {
				
				int ammount = 1;
				
				try {
					
					ammount = Integer.parseInt(args[1]);
				} catch(NumberFormatException ex) {
					
					PerkUtils.OutputToPlayer(player, "Could not parse ammount.");
					return true;
				}
				
				String name = args[0].toLowerCase();
				ItemInfo iteminfo = Items.itemByName(name);
				
				if (iteminfo == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item.");
				}
				
				ItemStack item = iteminfo.toStack();
				item.setAmount(ammount);
				
				player.getPlayer().getInventory().addItem(item);
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + ammount + " "+ item.toString() );
			}
			
			return true;
		}
		
		return  false;
	}
	
}
