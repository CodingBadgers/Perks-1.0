package me.wisbycraft.perks;

public class PerkThread extends Thread {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	private boolean m_running = false;
	private int m_period = 10;
	
	public PerkThread(Perks plugin) {
		m_plugin = plugin;
		m_running = true;
	}

	@Override
	public void run() {	
		
		int sleepTime = (1000 * 60) * m_period;
		
		while (m_running) {
							
			PerkUtils.OutputToAll("Don't forget to vote for us daily on..");
			PerkUtils.OutputToAll("\t - minestatus.net");
			PerkUtils.OutputToAll("\t - mcserverstatus.com");
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}	
	}
}
