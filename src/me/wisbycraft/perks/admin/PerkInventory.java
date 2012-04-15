package me.wisbycraft.perks.admin;
import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkItem;
import me.wisbycraft.perks.utils.PerkItems;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

public class PerkInventory {      
	
	private static ItemStack matchItem(String name, int amount) {
		
		ItemStack items;
		PerkItem info;
		
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
			
			info = PerkItems.itemById(id, dv);
			
			if (info == null)
				return null;
			
			items = info.toStack();
			
			if (amount < 1) {
				items.setAmount(1);
			} else if (amount > items.getMaxStackSize()) {
				items.setAmount(items.getMaxStackSize());
			} else {
				items.setAmount(amount);
			}
			
		} else {
			
			info = PerkItems.itemByName(name);
			
			if (info == null)
				return null;
			
			items = info.toStack();
			
			if (amount < 1) {
				items.setAmount(1);
			} else if (amount > items.getMaxStackSize()) {
				items.setAmount(items.getMaxStackSize());
			} else {
				items.setAmount(amount);
			}
			
		}
		return items;
	}
	
	private static String getName(String item) {		
		return PerkItems.itemByName(item).getName();
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args){
		
		if (cmd.getName().equalsIgnoreCase("item") || cmd.getName().equalsIgnoreCase("i")) {
			
			if (!player.hasPermission("perks.item", true))
				return true;
			
			if (args.size() == 1) {
				
				ItemStack item = matchItem(args.getString(0), 1);
				String name = getName(args.getString(0));
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "Could not find item");
					return true;
				}
				
				player.getPlayer().getInventory().addItem(item);
				
				PerkUtils.OutputToPlayer(player, "You have been given "  + 1 + " " + name );
				
			} else if (args.size() == 2) {
				int ammount;
				try {
					ammount = args.getInt(1);
				} catch(NumberFormatException ex) {
					PerkUtils.OutputToPlayer(player, "The ammount you entered was not a number, using 1 as the ammount");
					ammount = 1;
				}
				ItemStack item = matchItem(args.getString(0),ammount);
				String name = getName(args.getString(0));
				
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
