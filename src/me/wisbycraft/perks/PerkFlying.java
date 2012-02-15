package me.wisbycraft.perks;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerMoveEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkFlying {

	private static double m_defaultGravity = 0.0f;

	public static void fly(PerkPlayer player, PlayerMoveEvent event) {

		if (PerkUtils.spoutEnabled) {
			
			SpoutPlayer spoutPlayer = player.getSpoutPlayer();
	
			// currently flying is only enabled if you're using spoutcraft
			// will have to implement a magic carpet
			if (spoutPlayer.isSpoutCraftEnabled() && !player.getForceCarpet()) {
	
				if (m_defaultGravity == 0.0f)
					m_defaultGravity = spoutPlayer.getGravityMultiplier();
	
				// see if the player is flying, if not set all back to default...
				if (!player.isFlying()) {
					spoutPlayer.setGravityMultiplier(m_defaultGravity);
					spoutPlayer.setAirSpeedMultiplier(1);
					spoutPlayer.setCanFly(false);
					return;
				}
	
				// turn on flying to stop them getting kicked
				spoutPlayer.setCanFly(true);
	
				// set the fall distance to 0 to stop players dying when they land
				spoutPlayer.setFallDistance(0.0f);
	
				float pitch = spoutPlayer.getLocation().getPitch();
				if (pitch < 10.0f && pitch > -10.0f)
					pitch = 0.0f;
				
				spoutPlayer.setGravityMultiplier(pitch * 0.005);
	
				// speed up through the air
				spoutPlayer.setAirSpeedMultiplier(2); // increase speed
			} else {
				
				// this is the magic carpet version...
				
				// make sure the person is flying...
				if (!player.isFlying()) {
					return;
				}
	
				// get where they are going to. if there sneaking go down one.
				Location to = event.getTo();
				Location from = event.getFrom();
				
				if (player.getPlayer().isSneaking())
					to.setY(to.getY() - 1);
				else if(from.getY() > to.getY())
					to.setY(from.getY());
				
				// update the magic carpet
				player.getMagicCarpet().positionAndShow(event.getTo());
			}
		}
		else
		{
			// this is the magic carpet version... laggier and poo.
			
			// make sure the person is flying...
			if (!player.isFlying()) {
				return;
			}

			// get where they are going to. if there sneaking go down one.
			Location to = event.getTo();
			if (player.getPlayer().isSneaking())
				to.setY(to.getY() - 1);

			// update the magic carpet
			player.getMagicCarpet().positionAndShow(event.getTo());			
		}

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
