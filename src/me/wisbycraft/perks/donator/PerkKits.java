package me.wisbycraft.perks.donator;

import java.util.ArrayList;

import me.wisbycraft.perks.utils.Kit;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;


public class PerkKits {

	public static ArrayList<Kit> kits = new ArrayList<Kit>();
	
	public static void addKit(String name, int timeout,ArrayList<ItemStack> items) {
		Kit kit = new Kit(name, timeout, items);
		kits.add(kit);
	}
	
	public static Kit getKit(String name) {
		Kit kit = null;
		
		for (int i = 0; i<kits.size(); i++) {
			
			if (kits.get(i).getName() == name) {
				
				kit = kits.get(i);
			}
		}
		
		return kit;
	}
	
	public static boolean kitExists(String name) {
		
		for (int i = 0; i<kits.size(); i++) {
			
			if (kits.get(i).getName() == name) {
				
				return true;
			
			}
		
		}
		
		return false;
	}
	
	public static void listKits(PerkPlayer player) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		
		for (int i = 0; i<kits.size(); i++) {
			
			out.append(kits.get(i).getName());
			
			if (!first) {
				out.append(',');
			}
			
			first = false;
		}
		
		String[] lines = out.toString().split("\n");

        for (String line : lines) {
           PerkUtils.OutputToPlayer(player, line);
        }
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("kit")) {
			
			if (args.length == 0) {
				
				listKits(player);
				return true;
				
			} else if (args.length == 1) {
			
			
				String name = args[0];
				
				if (!kitExists(name)) {
					PerkUtils.OutputToPlayer(player, "That kit does not exist.");
					return true;
				}
					
				if (!player.hasPermission("perks.kit." + name, true))
					return true;
				
				Kit kit = getKit(name);
				
				for (int i = 0; i<kit.getItems().size(); i++) {
					
					player.getPlayer().getInventory().addItem(kit.getItems().get(i));
				}
				
				PerkUtils.OutputToPlayer(player, "You have been given kit " + name);
				
			} else {
				
				PerkUtils.OutputToPlayer(player, "use '/kit <name>' or '/kit' to list the kits");
				return true;
			}
		}
		return false;
	}
}
