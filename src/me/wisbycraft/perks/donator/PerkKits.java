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
		for (int i = 0; i < kits.size(); i++) {
			if (kits.get(i).getName().equalsIgnoreCase(name)) {
				return kits.get(i);
			}
		}
		return null;
	}
	
	public static boolean kitExists(String name) {
		
		return getKit(name) != null;
	}
	
	public static void listKits(PerkPlayer player) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		
		for (int i = 0; i<kits.size(); i++) {

			if (!first) {
				out.append(',');
			}
			
			out.append(kits.get(i).getName());
			
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
				if (kit == null) {
					PerkUtils.OutputToPlayer(player, "An unknown error has occured.");
					PerkUtils.OutputToPlayer(player, "Please inform a developer.");
					return true;
				}
				
				if (!player.canUseKit(kit)) {
					return true;
				}
				
				for (int i = 0; i< kit.getItems().size(); i++) {
					player.getPlayer().getInventory().addItem(kit.getItems().get(i));
				}
				
				PerkUtils.OutputToPlayer(player, "You have been given kit " + name);
				
				player.usedKit(kit);
				
				return true;
				
			} else {
				PerkUtils.OutputToPlayer(player, "use '/kit <name>' or '/kit' to list the kits");
				return true;
			}
		}
		
		return false;
	}
}
