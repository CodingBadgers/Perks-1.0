package uk.codingbadgers.perks.utils;

import uk.codingbadgers.perks.Perks;
import uk.codingbadgers.perks.config.PerkConfig;


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
}
