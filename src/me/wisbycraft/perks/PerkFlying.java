package me.wisbycraft.perks;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.getspout.spoutapi.player.SpoutPlayer;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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
			if (!player.isFlying()) {
				return;
			}

			Location to = event.getTo();
			if (player.getPlayer().isSneaking())
				to.setY(to.getY() - 1);

			player.getMagicCarpet().positionAndShow(event.getTo());
		}

	}

	public static boolean onCommand(PerkPlayer player, Command cmd,
			String commandLabel, String[] args) {
                PermissionManager permissions = PermissionsEx.getPermissionManager();
                Player p_player = (Player) player.getPlayer();
                
		if (cmd.getName().equalsIgnoreCase("fly")) {
                    if (permissions.has(p_player, "perks.fly")) {
			player.setFlying(true);
			return true;
                    }
		}

		if (cmd.getName().equalsIgnoreCase("land")) {
                    if (permissions.has(p_player, "perks.fly")) {
			player.setFlying(false);
			return true;
                    } else {
                        
                    }
		}

		if (cmd.getName().equalsIgnoreCase("flytoggle")) {
                    if (permissions.has(p_player, "perks.fly")) {
			if (player.isFlying()) {
				player.setFlying(false);
			} else {
				player.setFlying(true);
			}

			return true;
                    }
		}

		return false;
	}
}
