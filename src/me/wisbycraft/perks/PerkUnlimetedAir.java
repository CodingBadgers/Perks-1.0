package me.wisbycraft.perks;

import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class PerkUnlimetedAir {

    public void unlimetedAir(Player player) { 
        
        if (player.getRemainingAir() <= player.getMaximumAir()) {
            player.setRemainingAir(player.getMaximumAir());
        }
        
    }
    
}
