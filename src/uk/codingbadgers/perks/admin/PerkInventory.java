package uk.codingbadgers.perks.admin;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkItem;
import uk.codingbadgers.perks.utils.PerkItems;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkInventory {      
	
	private static final Pattern itemIdPattern = Pattern.compile("[0-9]{1,3}(:[0-9]{1,3}){0,1}");
	private static final Pattern itemNamePattern = Pattern.compile("[a-zA-Z]+(:[0-9]{1,3}){0,1}");
	
	private static ItemStack[] matchItem(String name, int amount) {
		
		ItemStack itemsTemp;
		PerkItem info;
		
		if (itemIdPattern.matcher(name).matches()) {	
			
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
			
			Material material = Material.getMaterial(id);
			
			itemsTemp = new ItemStack(material, 1, dv);			
			
		} else {
			
			Matcher matcher = itemNamePattern.matcher(name);
			
			if (!matcher.matches()) {
				String itemName = matcher.group(1);
				
				short dv = 0;
				
				if (matcher.group(2) != null) {
					dv = Short.parseShort(matcher.group(2).substring(1));
				}
				
				info = PerkItems.itemByName(itemName);
				
				if (info == null)
					return null;
				
				itemsTemp = info.toStack(dv);
			} else {
				return null;
			}
		}
		
		ItemStack[] items = null;
		
		if (amount < 1) {
			itemsTemp.setAmount(1);
			items = new ItemStack[] {itemsTemp};
		} else if (amount > itemsTemp.getMaxStackSize()) {

			// FIXME currently is still abit buggy
			int amountToGive = amount;
			int stackSize = itemsTemp.getMaxStackSize();
			int current = 0;
			int stacks = (int) Math.ceil((double)amountToGive / (double)stackSize);
			
			if (stacks > 36) {
				amountToGive = stackSize*36;
			}
			
			items = new ItemStack[stacks];
			
			while(amountToGive >= stackSize) {
				items[current] = new ItemStack(itemsTemp.getType(), stackSize);
				amountToGive -= stackSize;
				current++;
			}
			
			if (amountToGive > 0) {
				items[current] = new ItemStack(itemsTemp.getType(), amountToGive);
			}
			
		} else {
			itemsTemp.setAmount(amount);
			items = new ItemStack[] {itemsTemp};
		}
		
		return items;
	}
	
	private static String getName(String name) {		
		PerkItem item = PerkItems.itemByName(name);
		
		String itemName;
		
		if (item == null) {
			Material mat = Material.getMaterial(name);
			
			if (mat == null) {
				itemName = name;
			} else {
				itemName = mat.toString().toLowerCase().replace('_', ' ');
			}
		} else {
			itemName = item.getName();
		}
		
		return itemName;
	}
	
	private static String getName(ItemStack stack) {		
		PerkItem item = PerkItems.itemById(stack.getTypeId());
		
		String itemName;
		
		if (item == null) {
			itemName = stack.getType().toString().toLowerCase().replace('_', ' ');
		} else {
			itemName = item.getName();
		}
		
		return itemName;
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
				
				amount = getAmount(item);

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
	
	public static int getAmount(ItemStack[] items) {
		int amount = 0;
		for (ItemStack item : items) {
			amount += item.getAmount();
		}
		return amount;
	}
}
