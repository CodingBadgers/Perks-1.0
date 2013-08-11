package uk.codingbadgers.perks.donator;

import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class AFKThread extends Thread {
	
	private boolean m_running = false;
	
	public AFKThread() {
		m_running = true;
	}
	
	public void kill() {
		m_running = false;
	}
	
	public void run() {
		
		while (m_running) {
			try {
				final int checkRate = 60;
				Thread.sleep(checkRate * 1000);
			} catch(Exception ex) {}
			
			if (!m_running) {
				return;
			}
			
			for (PerkPlayer player : PerkUtils.perkPlayers) {
				player.checkAFK();
			}
		}
		
    }
}
