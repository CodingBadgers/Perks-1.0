package me.wisbycraft.perks;

import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkFlying {
		
	public static void fly(PerkPlayer player) {
		
		if (!player.isFlying())
			return;
		
		SpoutPlayer spoutPlayer = player.getSpoutPlayer();
				
		// currently flying is only enabled if you're using spoutcraft
    	// will have to implement a magic carpet
    	if (spoutPlayer.isSpoutCraftEnabled()) {
    	
	    	// turn on flying to stop them getting kicked
    		spoutPlayer.setCanFly(true);
	    		    	
	    	// effect gravity based upon keyboard input
	    	
	    	// need to hook into spoutcraft keyboard
	    	
	    	if (spoutPlayer.isSneaking())
	    		spoutPlayer.setGravityMultiplier(0.2f);
	    	else
	    		spoutPlayer.setGravityMultiplier(0);
	    	
	    	// speed up through the air
	    	spoutPlayer.setAirSpeedMultiplier(2);
    	}
		
	}

}
