package me.wisbycraft.perks;

import org.bukkit.command.Command;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerkFlying {
	
	private static double m_defaultGravity = 0.0f;
		
	public static void fly(PerkPlayer player) {
		
		SpoutPlayer spoutPlayer = player.getSpoutPlayer();
						
		// currently flying is only enabled if you're using spoutcraft
    	// will have to implement a magic carpet
    	if (spoutPlayer.isSpoutCraftEnabled()) {
    		
    		if (m_defaultGravity == 0.0f)
    			m_defaultGravity = spoutPlayer.getGravityMultiplier();
    		
    		// see if the player is flying, if not set all back to default...
    		if (!player.isFlying()) {
    			spoutPlayer.setGravityMultiplier(m_defaultGravity);
    			spoutPlayer.setAirSpeedMultiplier(1);
    			spoutPlayer.setCanFly(false);
    			return;
    		}
    		    	
	    	// turn on flying to stop them getting kicked
    		spoutPlayer.setCanFly(true);
    		
    		// set the fall distance to 0 to stop players dying when they land
    		spoutPlayer.setFallDistance(0.0f);
	    		    	
	    	// effect gravity based upon keyboard input
	    	if (player.isJumping())
	    		spoutPlayer.setGravityMultiplier(-0.2f);
	    	else if (player.isSneaking())
	    		spoutPlayer.setGravityMultiplier(0.2f);
	    	else
	    		spoutPlayer.setGravityMultiplier(0);
	    	
	    	// speed up through the air
	    	spoutPlayer.setAirSpeedMultiplier(2);
    	}
		
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("fly")) {
			player.setFlying(true);			
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("land")) {
    		player.setFlying(false);		
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("flytoggle")) {
    		
    		if (player.isFlying()) {
    			player.setFlying(false);		
    		} else {
    			player.setFlying(true);		
    		}
    			
    		return true;
    	}
    	
		return false;
	}

}
