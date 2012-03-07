package me.wisbycraft.perks.utils;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class PerkVault {

	public static Permission perms;
	
	public static boolean setupPerms() {
		RegisteredServiceProvider<Permission> rsp = PerkUtils.server().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

}
