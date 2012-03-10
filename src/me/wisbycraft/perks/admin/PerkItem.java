package me.wisbycraft.perks.admin;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import net.milkbowl.vault.item.Items;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

public class PerkItem {      
        
	private static ItemStack matchItem(String name, String amountStr) {
		
		int amount;
		
		try {
			amount = Integer.parseInt(amountStr);
		} catch(NumberFormatException ex) {
			return null;
		}
		
		return matchItem(name, amount);
	}
	
	private static ItemStack matchItem(String name, int amount) {
		
		ItemStack items;
		
		if (PerkUtils.isNumeric(name)) {	
			
			int id;
			short dv = 0;
			
			if (name.indexOf(':') != -1) {
				
				String data = name.substring(name.indexOf(':') + 1);
				name = name.substring(0, name.indexOf(':'));
				try {
					dv = Short.parseShort(data);
				} catch (NumberFormatException ex) {
					return null;
				}
			}
			try {
				id = Integer.parseInt(name);
			} catch(NumberFormatException ex) {
				return null;
			}
			
			if (PerkUtils.vaultEnabled) {
				items = Items.itemById(id, dv).toStack();
			} else {
				items = new ItemStack(Material.getMaterial(id));
			}
			
			if (amount < 1 || amount > 64) {
				items.setAmount(1);
			} else {
				items.setAmount(amount);
			}
			
		} else {
			
			items = Items.itemByName(name).toStack();
			
			if (amount < 1 || amount > 64) {
				items.setAmount(1);
			} else {
				items.setAmount(amount);
			}
			
		}
		return items;
	}
	
	private static String getName(String item) {		
		return Items.itemByName(item).getName();
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args){
		
		if (cmd.getName().equalsIgnoreCase("item") || cmd.getName().equalsIgnoreCase("i")) {
			
			if (!player.hasPermission("perks.item", true))
				return true;
			
			if (args.length == 1) {
				
				ItemStack item = matchItem(args[0], 1);
				String name = getName(args[0]);
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item");
					return true;
				}
				
				player.getPlayer().getInventory().addItem(item);
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + "1"   + " " + name );
				
			} else if (args.length == 2) {
				
				ItemStack item = matchItem(args[0], args[1]);
				String name = getName(args[0]);
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item");
					return true;
				}
				
				player.getPlayer().getInventory().addItem(item);
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + item.getAmount()   + " " + name );
			}
			
			return true;
		}
		
		return  false;
	}
	
}
