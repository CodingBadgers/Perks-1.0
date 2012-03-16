package me.wisbycraft.perks.admin;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.util.Vector;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkFun {

	private static final Random random = new Random();
	
	public static void slap(PerkPlayer player) {
		
		player.getPlayer().setVelocity(new Vector(
                random.nextDouble() * 10.0 - 5,
                random.nextDouble() * 2,
                random.nextDouble() * 10.0 - 5));
	}
	
	public static void rocket(PerkPlayer player) {
		
		player.getPlayer().setVelocity(new Vector(0,50,0));
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("slap")) {
			 
			if (!player.hasPermission("perks.fun.slap", true)) 
				return true;
			
			if (args.length != 1) {
				PerkUtils.OutputToPlayer(player, "use /slap <target>");
				return true;
			}
			
			PerkPlayer target = PerkUtils.getPlayer(args[0]);
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "That player is not online");
				return true;
			}
			
			slap(target);
			PerkUtils.OutputToAll(player.getPlayer().getName() + " has slapped " + target.getPlayer().getName());
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("rocket")) {
			 
			if (!player.hasPermission("perks.fun.rocket", true)) 
				return true;
			
			if (args.length != 1) {
				PerkUtils.OutputToPlayer(player, "use /rocket <target>");
				return true;
			}
			
			PerkPlayer target = PerkUtils.getPlayer(args[0]);
			
			if (target == null) {
				PerkUtils.OutputToPlayer(player, "That player is not online");
				return true;
			}
			
			slap(target);
			PerkUtils.OutputToAll(player.getPlayer().getName() + " has rocketed " + target.getPlayer().getName());
			return true;
		}
		
		return false;
	}
}
