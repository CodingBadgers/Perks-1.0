package me.wisbycraft.perks;

import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PerksInputListener extends InputListener {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	
	public PerksInputListener(Perks plugin) {
		m_plugin = plugin;
	}

	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {

		SpoutPlayer p = event.getPlayer();
		
	    if (event.getKey() == p.getJumpKey()) {
	        p.sendMessage("Jumping!");
	    }
	    
	}
	
}
