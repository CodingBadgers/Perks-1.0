package me.wisbycraft.perks.donator;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class PerkUnlimitedAir {

	static public void drown(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;

		Player bPlayer = (Player) event.getEntity();
		
		if (bPlayer == null)
			return;
		
		PerkPlayer player = PerkUtils.getPlayer(bPlayer);
		
		if (player == null)
			return;

		if (!player.hasPermission("perks.water", false))
			return;

		if (event.getCause() == DamageCause.DROWNING) {
			{
				if (!player.hasPermission("perks.water.plus", false))
				{
					if (bPlayer.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
						bPlayer.setRemainingAir(bPlayer.getMaximumAir());
						event.setCancelled(true);
					}
				}
				else
				{
					bPlayer.setRemainingAir(bPlayer.getMaximumAir());
					event.setCancelled(true);
				}
			}
		}
	}

}
