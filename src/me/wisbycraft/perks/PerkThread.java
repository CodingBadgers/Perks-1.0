package me.wisbycraft.perks;

public class PerkThread extends Thread {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	private boolean m_running = false;
	
	
	public PerkThread(Perks plugin) {
		m_plugin = plugin;
		m_running = true;
	}

	@Override
	public void run() {	
		while (m_running) {
					
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
			
		}	
	}
}
