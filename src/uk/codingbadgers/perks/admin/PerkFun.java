package uk.codingbadgers.perks.admin;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.util.Vector;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkFun {

	private static final Random random = new Random();
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("slap")) {
			 
			if (!player.hasPermission("perks.fun.slap", true)) 
				return true;
			
			PerkPlayer target;
			if (args.size() == 0) {
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
			
			double power = 1.0;
			String powerString = ""; 
			if (args.hasFlag('v')) {
				power = 5.0;
				powerString = " very hard";
            } else if (args.hasFlag('h')) {
            	power = 2.5;
            	powerString = " hard";
            }
			
			target.getPlayer().setVelocity(new Vector(
	            (random.nextDouble() + 0.01) * power - (power * 0.5),
	            (random.nextDouble() + 0.01) * power,
	            (random.nextDouble() + 0.01) * power - (power * 0.5))
			);
			
			if (!silent) {
				PerkUtils.OutputToAllExcluding(player.getPlayer().getName() + " has slapped " + target.getPlayer().getName() + powerString, player.getPlayer());
			}
			
			if (target != player) {
				PerkUtils.OutputToPlayer(target, "You have been slapped" + powerString + " by " + player.getPlayer().getName());
			}
			
			PerkUtils.OutputToPlayer(player, "You have slapped " + (target != player ? target.getPlayer().getName() : "Yourself") + powerString);
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("rocket")) {
			 
			if (!player.hasPermission("perks.fun.rocket", true)) 
				return true;
			
			PerkPlayer target;
			if (args.size() == 0) {
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
		
			double power = 10.0;		
			target.getPlayer().setVelocity(new Vector(0, power, 0));
			
			if (!silent) {
				PerkUtils.OutputToAllExcluding(player.getPlayer().getName() + " has rocketed " + target.getPlayer().getName(), player.getPlayer());
			}
			
			if (target != player) {
				PerkUtils.OutputToPlayer(target, "You have been rocketed by " + player.getPlayer().getName());
			}
			
			PerkUtils.OutputToPlayer(player, "You have rocketed " + (target != player ? target.getPlayer().getName() : "Yourself"));
			return true;
		}
		
		return false;
	}
}
