package me.wman.perks.admin;

import me.wman.perks.utils.PerkArgSet;
import me.wman.perks.utils.PerkItem;
import me.wman.perks.utils.PerkItems;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

public class PerkInventory {      
	
	private static ItemStack[] matchItem(String name, int amount) {
		
		ItemStack itemsTemp;
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
			
			itemsTemp = info.toStack();
			
			
		} else {
			
			info = PerkItems.itemByName(name);
			
			if (info == null)
				return null;
			
			itemsTemp = info.toStack();
			
		}
		
		ItemStack[] items = null;
		
		if (amount < 1) {
			itemsTemp.setAmount(1);
			items = new ItemStack[] {itemsTemp};
		} else if (amount > itemsTemp.getMaxStackSize()) {
			// TODO needs to split it up into stacks of the max stack size
			itemsTemp.setAmount(itemsTemp.getMaxStackSize());
			items = new ItemStack[] {itemsTemp};
		} else {
			itemsTemp.setAmount(amount);
			items = new ItemStack[] {itemsTemp};
		}
		
		return items;
	}
	
	private static String getName(String item) {		
		return PerkItems.itemByName(item).getName();
	}
	
	private static String getName(ItemStack item) {		
		return PerkItems.itemByName(item.toString().toLowerCase()).getName();
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args){
		
		if (cmd.getName().equalsIgnoreCase("item") || cmd.getName().equalsIgnoreCase("i")) {
			
			if (!player.hasPermission("perks.item", true))
				return true;
			
			if (args.size() >= 1 && args.size() <= 3) {
				
				PerkPlayer target;
				if (args.hasFlag('o')) {
					if (!player.hasPermission("perks.item.other", true))
						return true;
					
					if (PerkUtils.isNumeric(args.getString(1))) {
						target = PerkUtils.getPlayer(args.getString(2));
					} else {
						target = PerkUtils.getPlayer(args.getString(1));
					}
				} else {
					target = player;
				}
				
				String itemString = args.getString(0);
				int amount = 0;
				if (args.size() >= 2) {
					if (PerkUtils.isNumeric(args.getString(1))) {
						amount = args.getInt(1);
					}
				}
				
				ItemStack[] item = matchItem(itemString, amount);
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "That item doesn't exist");
					return true;
				}
				
				if (item.length > 1) 
					amount = item.length * item[0].getMaxStackSize();
				else 
					amount = item[0].getAmount();

				if (args.hasFlag('d')) {
					for (ItemStack stack : item) {
						Location loc = target.getPlayer().getLocation();
						loc.getWorld().dropItemNaturally(loc, stack);
					}
				} else {
					target.getPlayer().getInventory().addItem(item);
				}
				
				if (player == target) {
					PerkUtils.OutputToPlayer(player, "You have been given " + (amount == 0 ? '1' : amount) + " " + getName(itemString));
				} else {
					PerkUtils.OutputToPlayer(target, player.getPlayer().getName() + " gave you " + (amount == 0 ? '1' : amount) + " " + getName(itemString));
					PerkUtils.OutputToPlayer(player, "You gave " + target.getPlayer().getName() + " " + (amount == 0 ? '1' : amount) + " " + getName(itemString));
				}
			} else {
				
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
			}
			
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("giveall")) {
			
			if (!player.hasPermission("perks.item.all", true))
				return true;
			
			if (args.size() >= 1 && args.size() <= 3) {
				
				String itemString = args.getString(0);
				int amount = 0;
				if (args.size() >= 2) {
					if (PerkUtils.isNumeric(args.getString(1))) {
						amount = args.getInt(1);
					}
				}
				
				ItemStack[] item = matchItem(itemString, amount);
				
				if (item == null) {
					PerkUtils.OutputToPlayer(player, "That item doesn't exist");
					return true;
				}
				
				if (item.length > 1) 
					amount = item.length * item[0].getMaxStackSize();
				else 
					amount = item[0].getAmount();
				
				for (PerkPlayer target : PerkUtils.perkPlayers) {
					if (args.hasFlag('d')) {
						for (ItemStack stack : item) {
							Location loc = target.getPlayer().getLocation();
							loc.getWorld().dropItemNaturally(loc, stack);
						}
					} else {
						target.getPlayer().getInventory().addItem(item);
					}
				}
				
				PerkUtils.OutputToPlayer(player, "You have even everyone on the server " + amount + " of " + getName(item[0]));
				PerkUtils.OutputToAll(player.getPlayer().getName() + " has given everyone "  + amount + " of " + getName(item[0]));
			} else {
				
				PerkUtils.OutputToPlayer(player, PerkUtils.getUsage(cmd));
			}
			
			return true;
		}
		
		return  false;
	}
	
}
