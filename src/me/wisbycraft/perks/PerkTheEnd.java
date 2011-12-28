package me.wisbycraft.perks;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PerkTheEnd {

	public static void dragonHurt(EntityDamageEvent event) {
		
		// not a dragon been hurt?
		if (!(event.getEntity() instanceof EnderDragon))
			return;

		// dragon not been hurt by another entity?
		if(!(event instanceof EntityDamageByEntityEvent))
			return;
	
		EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent)event;
		
		// dragon not being hurt by a human?
		if (!(entityEvent.getDamager() instanceof Player))
			return;
		
		// The player that hit the dragon
		PerkPlayer player = PerkUtils.getPlayer((Player)entityEvent.getDamager());
		
		// Something went tits up
		if (player == null)
			return;
		
		player.causedDamageToDragon(event.getDamage());
		
	}

}
