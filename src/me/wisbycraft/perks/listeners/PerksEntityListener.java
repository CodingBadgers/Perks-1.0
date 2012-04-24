package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.donator.PerkDeathTP;
import me.wisbycraft.perks.donator.PerkHunger;
import me.wisbycraft.perks.donator.PerkUnlimitedAir;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class PerksEntityListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		PerkUnlimitedAir.drown(event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamamage(EntityDamageByEntityEvent event) {
	
		if (!(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE || event.getCause() == DamageCause.MAGIC))
			return;
			
		PerkPlayer attacker = null;
				
		if (event.getDamager() instanceof Player)
			attacker = PerkUtils.getPlayer((Player)event.getDamager());
		else if (event.getDamager() instanceof Arrow) {
			LivingEntity shooter = ((Arrow)event.getDamager()).getShooter();
			if (shooter instanceof Player)
				attacker = PerkUtils.getPlayer((Player)shooter);
		} else if (event.getDamager() instanceof ThrownPotion)
			attacker = PerkUtils.getPlayer((Player)(((ThrownPotion)event.getDamager()).getShooter()));
		
		// don't allow attacking whilst flying or in vanish
		if (attacker != null &&  (attacker.isFlying() || attacker.isVanished())) {
			event.setCancelled(true);
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
