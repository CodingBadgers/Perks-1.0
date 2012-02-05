package me.wisbycraft.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

class PerksEntityListener implements Listener {

	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and
								   // the warning was annoying me

	public PerksEntityListener(Perks plugin) {
		m_plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {

		PerkUnlimitedAir.drown(event);
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        PerkHunger.onHungerLevelChange(event);
    }
	
}
