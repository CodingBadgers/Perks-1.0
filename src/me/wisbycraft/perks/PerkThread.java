package me.wisbycraft.perks;

import org.bukkit.entity.Player;

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
			
			Player[] player = PerkUtils.plugin.getServer().getOnlinePlayers();
			
			for(int i = 0; i<player.length; i++) {
				
				if (PerkUtils.getPlayer(player[i]).hasPermission("Perks.capes", false)) {
					
					PerkCapes.setCape(player[i]);
				}
				
				PerkColors.addColor(player[i]);
			}
							
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}	
	}
}
