package me.wman.perks.donator;

import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class PerkHunger {

    static public void onHungerLevelChange(FoodLevelChangeEvent event) {
        
    	// make sure we're dealing with a player
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        // get the PerkPlayer from the bukkit Player
        PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
        
        
        if (player == null) {
        	return;
        }
        
        // do they have the huger permission?
        if (!player.hasPermission("perks.hunger", false)) 
        	return;
        
        // if the player should ignore the hunger event cancel the event
        if (!player.shouldLowerHunger()) {
        	event.setFoodLevel(event.getFoodLevel() + 1);
        }   
    }
}
