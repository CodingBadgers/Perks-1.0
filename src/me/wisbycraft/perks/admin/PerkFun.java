package me.wisbycraft.perks.admin;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.util.Vector;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

public class PerkFun {

	private static final Random random = new Random();
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("slap")) {
			 
			if (!player.hasPermission("perks.fun.slap", true)) 
				return true;
			
			if (player.isBlacklisted(true))
				return true;
			
			PerkPlayer target;
			if (args.size() != 1) {
				target = player;
			} else {
				target = PerkUtils.getPlayer(args.getString(0));
				if (target == null) {
					PerkUtils.OutputToPlayer(player, args.getString(0) + " is not online");
					return true;
				}
			}
			
			boolean silent = false;
			if (args.hasFlag('s')) {
				silent = true;
			}
			
			if (args.hasFlag('v')) {
                target.getPlayer().setVelocity(new Vector(
                        random.nextDouble() * 10.0 - 5,
                        random.nextDouble() * 10,
                        random.nextDouble() * 10.0 - 5));
            } else if (args.hasFlag('h')) {
                target.getPlayer().setVelocity(new Vector(
                        random.nextDouble() * 5.0 - 2.5,
                        random.nextDouble() * 5,
                        random.nextDouble() * 5.0 - 2.5));
            } else {
                target.getPlayer().setVelocity(new Vector(
                        random.nextDouble() * 2.0 - 1,
                        random.nextDouble() * 2.5,
                        random.nextDouble() * 2.0 - 1));
            }
			
			if (!silent)
				PerkUtils.OutputToAll(player.getPlayer().getName() + " has slapped " + target.getPlayer().getName());
			
			if (target != player)
				PerkUtils.OutputToPlayer(target, "You have been slapped by " + player.getPlayer().getName());
			PerkUtils.OutputToPlayer(player, "You have slapped " + (target != player ? target.getPlayer().getName() : "Yourself"));
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("rocket")) {
			 
			if (!player.hasPermission("perks.fun.rocket", true)) 
				return true;
			
			if (player.isBlacklisted(true))
				return true;
			
			PerkPlayer target;
			if (args.size() != 1) {
				target = player;
			} else {
				target = PerkUtils.getPlayer(args.getString(0));
				if (target == null) {
					PerkUtils.OutputToPlayer(player, args.getString(0) + " is not online");
					return true;
				}
			}
			
			boolean silent = false;
			if (args.hasFlag('s')) {
				silent = true;
			}
			
			if (args.hasFlag('h')) {
                target.getPlayer().setVelocity(new Vector(0, 50, 0));
            } else {
                target.getPlayer().setVelocity(new Vector(0, 20, 0));
            }
			
			if (!silent)
				PerkUtils.OutputToAll(player.getPlayer().getName() + " has rocketed " + target.getPlayer().getName());
			
			if (target != player)
				PerkUtils.OutputToPlayer(target, "You have been rocketed by " + player.getPlayer().getName());
			PerkUtils.OutputToPlayer(player, "You have rocketed " + (target != player ? target.getPlayer().getName() : "Yourself"));return true;
		}
		
		return false;
	}
}
