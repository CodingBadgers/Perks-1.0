package me.wisbycraft.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkUnlimitedAir {
	
	static public void drown(EntityDamageEvent event) {
		PermissionManager permissions = PermissionsEx.getPermissionManager();
		if (!(event.getEntity() instanceof Player))
			return;

		PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());	
		
		if (event.getCause() == DamageCause.DROWNING) {
                    if (permissions.has(player, "perks.water")) {
			if (player.getPlayer().getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
				player.getPlayer().setRemainingAir(player.getPlayer().getMaximumAir());
				event.setCancelled(true);
                        }
                    }
		}
	}

}
