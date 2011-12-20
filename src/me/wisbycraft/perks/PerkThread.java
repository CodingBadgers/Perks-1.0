package me.wisbycraft.perks;

import java.util.Calendar;
import java.util.Date;

public class PerkThread extends Thread {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null;
	private boolean m_running = false;
	
	private final int FRIDAY = 6;
	
	public PerkThread(Perks plugin) {
		m_plugin = plugin;
		m_running = true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {	
		while (m_running) {

			Date date = new Date();	
			Calendar calendar = Calendar.getInstance();
		    calendar.set(Calendar.YEAR, date.getYear());
		    calendar.set(Calendar.MONTH, date.getMonth());
		    calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
			
		    int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		    if (weekday == FRIDAY) {
		    	// if its midday on Friday pay bitches
		    	
		    	if (date.getMinutes() == 0 && date.getHours() == 12) {
		    		// pay them

		    	}
		    }
		    
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
			
		}	
	}
}
