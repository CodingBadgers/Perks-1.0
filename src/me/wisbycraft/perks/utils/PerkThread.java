package me.wisbycraft.perks.utils;

import me.wisbycraft.perks.Perks;
import me.wisbycraft.perks.config.PerkConfig;


public class PerkThread extends Thread {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	private boolean m_running = false;
	private int m_period = PerkConfig.capeRefresh;
	
	public PerkThread(Perks plugin) {
		m_plugin = plugin;
		m_running = true;
	}

	@Override
	public void run() {	
		
		int sleepTime = (1000 * 60) * m_period;
		
		while (m_running) {
			
							
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}	
	}
	
	public void destroy(){
		m_running = false;
	}
}
