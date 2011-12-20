package me.wisbycraft.perks;

import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;

// spout only

public class PerksInputListener extends InputListener {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	private PerksPlayerListener m_playerListener = null;
	
	public PerksInputListener(Perks plugin, PerksPlayerListener playerListener) {
		m_plugin = plugin;
		m_playerListener = playerListener;
	}
	
	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {
		m_playerListener.onKeyPressedEvent(event);	
	}
	
	@Override
	public void onKeyReleasedEvent(KeyReleasedEvent event) {
		m_playerListener.onKeyReleasedEvent(event);	
	}
	
}
