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

		PerkPlayer player = PerkUtils.getPlayer((Player) event.getEntity());
		
		if (player == null) {
			return; // sanity check
		}

		if (!player.hasPermission("perks.water", false))
			return;

		if (event.getCause() == DamageCause.DROWNING) {
			{
				if (player.getPlayer().getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
					player.getPlayer().setRemainingAir(player.getPlayer().getMaximumAir());
					event.setCancelled(true);
				}
			}
		}
	}

}
