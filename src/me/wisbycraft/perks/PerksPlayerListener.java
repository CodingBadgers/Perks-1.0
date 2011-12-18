package me.wisbycraft.perks;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.getspout.spoutapi.player.*;

public class PerksPlayerListener extends PlayerListener {
	
	//private Perks m_plugin = null;
	
	public PerksPlayerListener(Perks plugin) {
		//m_plugin = plugin;
	}
	
    public void onPlayerMove(PlayerMoveEvent event){
    	
    	SpoutPlayer player = (SpoutPlayer)event.getPlayer();
    	
    	boolean isJumping = (event.getFrom().getY() < event.getTo().getY());
    	
    	// currently flying is only enabled if you're using spoutcraft
    	// will have to implement a magic carpet
    	if (player.isSpoutCraftEnabled()) {
    	
	    	// turn on flying to stop them getting kicked
	    	player.setCanFly(true);
	    		    	
	    	// effect gravity based upon keyboard input
	    	if (isJumping) {
	    		player.setGravityMultiplier(-0.2f);
	    	} if (player.isSneaking())
	    		player.setGravityMultiplier(0.2f);
	    	else
	    		player.setGravityMultiplier(0);
	    	
	    	// speed up through the air
	    	player.setAirSpeedMultiplier(2);
    	
    	}
    	
    }

}
