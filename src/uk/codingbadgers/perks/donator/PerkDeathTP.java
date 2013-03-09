package uk.codingbadgers.perks.donator;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkMobArena;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;


public class PerkDeathTP {
	
	public static void OnDeath(PerkPlayer player, EntityDeathEvent event) {
	
		if (!player.hasPermission("perks.deathtp", false))
			return;
		
		if (PerkMobArena.maHandler != null && (PerkMobArena.maHandler.isPlayerInArena(player.getPlayer()) || PerkMobArena.maHandler.inRegion(player.getPlayer().getLocation())))
			return;
		
		if (PerkConfig.isPvpServer() && !canDeathTpTo(player.getPlayer().getLastDamageCause())) {
			PerkUtils.OutputToPlayer(player, "You cannot return to that death location with death teleport");
			return;
		}
		
		Location deathLocation = player.getPlayer().getLocation();
		
		player.addDeathLocation(deathLocation);
		
	}
	
	private static boolean canDeathTpTo(EntityDamageEvent lastDamageCause) {
		switch(lastDamageCause.getCause()) {
			case CONTACT:
			case SUFFOCATION:
			case FALL:
			case FIRE:
			case FIRE_TICK:
			case LAVA:
			case DROWNING:
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
			case LIGHTNING:
			case SUICIDE:
			case STARVATION:
			case WITHER:
				return true;
			default:
				break;
		}
		
		if (lastDamageCause.getCause() == DamageCause.ENTITY_ATTACK || lastDamageCause.getCause() == DamageCause.PROJECTILE) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)lastDamageCause;
			
			Entity attacker = event.getDamager();
			
			if (attacker instanceof Player)
				return false;

			if (attacker instanceof Arrow) {
				if (((Arrow) attacker).getShooter() instanceof Skeleton)
					return true;
			}
			
			if (attacker instanceof ThrownPotion) {
				if (((ThrownPotion)attacker).getShooter() instanceof Witch) 
					return true;
			}	
		}
		
		return false;
	}

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (cmd.getName().equalsIgnoreCase("death")) {

			if (!player.hasPermission("perks.deathtp", true))
				return true;
			
			if (player.canDeathTP()) {
				Location deathloc = player.getDeathLocation();
				
				if (PerkMobArena.maHandler != null && PerkMobArena.maHandler.inRegion(deathloc)) {
					PerkUtils.OutputToPlayer(player, "Sorry you can't deathtp into the arena");
					return true;
				}
				
				if (player.getPlayer().getLocation().getWorld() == deathloc.getWorld())
				{
					player.getPlayer().teleport(deathloc);
					player.resetDeath();
					PerkUtils.OutputToPlayer(player, "You have been teleported to your last death point");
				}
				else
				{
					PerkUtils.OutputToPlayer(player, "You have to be in the same world as where you died");
				}
			}			
			return true;
		}
				
		return false;
		
	}

}