package me.wisbycraft.perks;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

class PerksEntityListener extends EntityListener {

	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and
								   // the warning was annoying me

	public PerksEntityListener(Perks plugin) {
		m_plugin = plugin;
	}

	public void onEntityDamage(EntityDamageEvent event) {
		
		PerkUnlimitedAir.drown( event);
		
	}
}
