package uk.codingbadgers.perks.donator;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkUnlimitedAir {

	@SuppressWarnings("deprecation")
	static public void drown(EntityDamageEvent event) {

		if (event.getCause() == DamageCause.DROWNING) {
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
						if (bPlayer.getInventory().getHelmet() != null && bPlayer.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
							bPlayer.setRemainingAir(bPlayer.getMaximumAir());
							
							if (!player.hasPermission("perks.water.damage", false)) {
								bPlayer.getInventory().getHelmet().setDurability((short) (bPlayer.getInventory().getHelmet().getDurability() + PerkConfig.waterDamageAmount));
								bPlayer.updateInventory();
							}
							
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

}
