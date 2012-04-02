package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.donator.PerkDeathTP;
import me.wisbycraft.perks.donator.PerkHunger;
import me.wisbycraft.perks.donator.PerkUnlimitedAir;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class PerksEntityListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		PerkUnlimitedAir.drown(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamamage(EntityDamageByEntityEvent event) {
	
		if (event.getCause() != DamageCause.ENTITY_ATTACK)
			return;
			
		PerkPlayer attacker = PerkUtils.getPlayer((Player)evnt.getDamager());
		
		// don't allow attacking whilst flying or in vanish
		if (attacker != null (attacker.isFlying() || attacker.isHidden())) {
			event.setCanceled(true);
			PerkUtils.OutputToPlayer(attacker, attacker.isFlying() ? "You cannot attack whist flying!" : "You cannot attack whist vanished!");
		}
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
