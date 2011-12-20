package me.wisbycraft.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 *
 * @author James
 */
public class PerkHunger {

    static public void HungerLevel1(FoodLevelChangeEvent event) {
        
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
        
        if (!(player.hasPermission("Perk.hunger.level1"))) {
            return;
        }
        
        if (event.getFoodLevel() < 10) {
            event.setFoodLevel(10);
        }
        
    }
    
    static public void HungerLevel2(FoodLevelChangeEvent event) {
        
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
        
        if (!(player.hasPermission("Perk.hunger.level2"))) {
            return;
        }
        
        if (event.getFoodLevel() < 15) {
            event.setFoodLevel(15);
        }
        
    }
    
    static public void HungerLevel3(FoodLevelChangeEvent event) {
        
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        PerkPlayer player = PerkUtils.getPlayer((Player)event.getEntity());
        
        if (!(player.hasPermission("Perk.hunger.level3"))) {
            return;
        }
        
        if (event.getFoodLevel() < 20) {
            event.setFoodLevel(20);
        }
        
    }
}
