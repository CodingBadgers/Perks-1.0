package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.utils.PerkPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerMoveEvent;


public class PerkFlying {

	/* Replaced With Bukkit system */
	public static void fly(PerkPlayer player, PlayerMoveEvent event) {

		// this is the magic carpet version... laggier and poo.
			
		// make sure the person is using mc
		if (!player.isForceCarpet())
			return;
		
		// make sure the person is flying
		if (!player.isFlying())
			return;

		// get where they are going to. if there sneaking go down one.
		Location to = event.getTo();
		if (player.getPlayer().isSneaking())
			to.setY(to.getY() - 1);

		// update the magic carpet
		player.getMagicCarpet().positionAndShow(event.getTo());			

	}

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
				
		// toggles fly mode
		if (cmd.getName().equalsIgnoreCase("fly") || cmd.getName().equalsIgnoreCase("mc")) {
			
			// all the following commands require this permission
			if (!player.hasPermission("perks.fly", true))
				return true;
			
			if (player.isFlying()) {
				player.setFlying(false, cmd.getName().equalsIgnoreCase("mc"));
			} else {
				player.setFlying(true, cmd.getName().equalsIgnoreCase("mc"));
			}
			
			return true;
		}
		
		// turns fly off, for jail mainly
		if (cmd.getName().equalsIgnoreCase("land")) {
			
			if (!player.hasPermission("perks.fly", true))
				return true;
			
			player.setFlying(false, false);
			
			return true;
		}
		return false;
	}
}
