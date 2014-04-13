package uk.codingbadgers.perks.listeners;


import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
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
import org.bukkit.projectiles.ProjectileSource;
import uk.codingbadgers.perks.donator.PerkDeathTP;
import uk.codingbadgers.perks.donator.PerkHunger;
import uk.codingbadgers.perks.donator.PerkUnlimitedAir;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerksEntityListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		PerkUnlimitedAir.drown(event);
		
		PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
		player.cancelTeleports("You got attacked");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamamage(EntityDamageByEntityEvent event) {
	
		if (!(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE || event.getCause() == DamageCause.MAGIC))
			return;
			
		PerkPlayer attacker = null;
				
		if (event.getDamager() instanceof Player)
			attacker = PerkUtils.getPlayer((Player)event.getDamager());
		else if (event.getDamager() instanceof Arrow) {
			ProjectileSource shooter = ((Arrow)event.getDamager()).getShooter();
			if (shooter instanceof Player)
				attacker = PerkUtils.getPlayer((Player)shooter);
		} else if (event.getDamager() instanceof ThrownPotion) { 
			ProjectileSource potionThrower = ((ThrownPotion)event.getDamager()).getShooter();
			if (potionThrower instanceof Player)
				attacker = PerkUtils.getPlayer((Player)(potionThrower));
		} else if (event.getEntityType() == EntityType.FISHING_HOOK) {
			ProjectileSource fisherMan = ((Fish)event.getDamager()).getShooter();
			if (fisherMan instanceof Player)
				attacker = PerkUtils.getPlayer((Player)fisherMan);
		}
		
		// don't allow attacking whilst flying or in vanish
		if (attacker != null &&  (attacker.isVanished())) {
			event.setCancelled(true);
			PerkUtils.OutputToPlayer(attacker, "You cannot attack whist vanished!");
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
