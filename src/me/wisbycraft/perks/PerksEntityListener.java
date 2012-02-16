package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

class PerksEntityListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		PerkUnlimitedAir.drown(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        PerkHunger.onHungerLevelChange(event);
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(EntityDeathEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;
		
		PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
		if (player == null)
			return;
		
		PerkDeathTP.OnDeath(player, event);
		
	}
	
}
