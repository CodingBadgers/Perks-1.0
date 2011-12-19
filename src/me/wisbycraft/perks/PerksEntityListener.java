package me.wisbycraft.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author James
 */
class PerksEntityListener extends EntityListener{
    
    	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and
									// the warning was annoying me

	private PerkPlayerArray perkPlayers = new PerkPlayerArray();

	public PerksEntityListener(Perks plugin) {
		m_plugin = plugin;
	}
        
        public void onEntityDamage(EntityDamageEvent event) {
            
            if (!(event.getEntity() instanceof Player)) {
                return;
            }
            
            Player player = (Player) event.getEntity();
            
            if (event.getCause() == DamageCause.DROWNING) {
                
                if(player.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
                    
                    event.setCancelled(true);  
                    
                }
                
            }
            
        }
    
}
