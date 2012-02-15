package me.wisbycraft.perks;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PerkVault {
	
	public static Permission perms;
	public static Economy eco;
	
	public static boolean setupPerms() {
		RegisteredServiceProvider<Permission> rsp = PerkUtils.server().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}
	
	public static boolean setupEco() {
		RegisteredServiceProvider<Economy> rsp = PerkUtils.server().getServicesManager().getRegistration(Economy.class);
		eco = rsp.getProvider();
		return eco != null;
	}
	
	
}
