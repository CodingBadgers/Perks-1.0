package me.wisbycraft.perks;

import org.bukkit.entity.Player;

/**
 * 
 * @author James
 */
public class PerkUnlimitedAir {

	public void unlimitedAir(Player player) {

		if (player.getRemainingAir() <= player.getMaximumAir()) {
			player.setRemainingAir(player.getMaximumAir());
		}

	}

}
