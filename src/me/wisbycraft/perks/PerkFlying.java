package me.wisbycraft.perks;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.event.player.PlayerMoveEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkFlying {

	private static double m_defaultGravity = 0.0f;

	public static void fly(PerkPlayer player, PlayerMoveEvent event) {

		SpoutPlayer spoutPlayer = player.getSpoutPlayer();

		// currently flying is only enabled if you're using spoutcraft
		// will have to implement a magic carpet
		if (spoutPlayer.isSpoutCraftEnabled()) {

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

			// effect gravity based upon keyboard input
			if (player.isJumping())
				spoutPlayer.setGravityMultiplier(-0.2f);
			else if (player.isSneaking())
				spoutPlayer.setGravityMultiplier(0.2f);
			else
				spoutPlayer.setGravityMultiplier(0);

			// speed up through the air
			spoutPlayer.setAirSpeedMultiplier(2);
		} else {
			
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
		
		// all the following commands require this permission
		if (!player.hasPermission("perks.fly"))
			return false;
		
		// turns fly on
		if (cmd.getName().equalsIgnoreCase("fly")) {
			player.setFlying(true);
			return true;
		}

		// turns fly off
		if (cmd.getName().equalsIgnoreCase("land")) {
			player.setFlying(false);
			return true;
		}

		// toggles flying
		if (cmd.getName().equalsIgnoreCase("flytoggle")) {
			if (player.isFlying()) {
				player.setFlying(false);
			} else {
				player.setFlying(true);
			}
			return true;
		}

		return false;
	}
}
