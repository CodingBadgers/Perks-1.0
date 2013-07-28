package uk.codingbadgers.perks.admin;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.perks.config.PerkConfig;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkStop extends Thread{

	private static PerkStop m_thread;
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("restartserver")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player)sender).hasPermission("perks.serveradmin.restart", true))
					return true;
			}
						
			int time = PerkConfig.shutdownTimeout;
			if (args.length == 1) {
				try {
					time = Integer.parseInt(args[0]);
				} catch(Exception ex) {
					if (sender instanceof Player)
						PerkUtils.OutputToPlayer((Player)sender, "Invalid timeout '" + args[0] + "'");
					else
						PerkUtils.ErrorConsole("Invalid timeout '" + args[0] + "'");
					return true;
				}
			}
			
			if (!(sender instanceof Player)) {
				PerkUtils.DebugConsole("Shutting server down in " + time + " seconds");
			}
			
			int shutdownTaskID = PerkUtils.server().getScheduler().scheduleSyncDelayedTask(PerkUtils.plugin, new Runnable() {

				   public void run() {
				       PerkUtils.shutdownServer();
				   }
				   
			}, (time + 1L) * 20L); // times 20 as we 'should' run at 20 ticks per second
			
			m_thread = new PerkStop(time, shutdownTaskID);
			m_thread.start();

			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("forcestop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player) sender).hasPermission("perks.serveradmin.forcestop", true))
					return true;
			}
			
			PerkUtils.plugin.getServer().shutdown(); // really force a stop, fuck thread safty
			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("stopstop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player) sender).hasPermission("perks.servveradmin.stopstop", true))
					return true;
			}
			
			m_thread.cancel();
			
			if (sender instanceof Player)
				PerkUtils.OutputToPlayer((Player)sender, "Server restart aborted");
			else
				PerkUtils.DebugConsole("Server restart aborted");
			
			return true;
			
		}
		
		return false;
	}

	private int m_time;
	private boolean m_cancled;
	private final int m_shutdownTaskID;

	public PerkStop (int time, int shutdownTaskID) {
		m_time = time;
		m_cancled = false;
		m_shutdownTaskID = shutdownTaskID;
	}

	public void run() {
		
		PerkUtils.OutputToAll("The Server will restart in " + PerkUtils.parseTime(m_time) + "...");
		
		while (m_time > 0) {
			try {
				Thread.sleep(1000);
				m_time--;
				
				if (m_cancled) {
					break;
				}
				
				boolean showMessage = false;
				
				// every 5 minutes
				if (m_time >= 300 && m_time % 300 == 0) 
				{ 
					showMessage = true;
				} 
				// every minute
				else if (m_time < 300 && m_time >= 60 && m_time % 60 == 0) 
				{ 
					showMessage = true;
				} 
				// every 10 seconds
				else if (m_time < 60 && m_time > 5 && m_time % 10 == 0 && m_time != 0) 
				{ 
					showMessage = true;
				}
				// every second
				else if (m_time <= 5 && m_time != 0) 
				{ 
					showMessage = true;
				}
				
				if (showMessage == true) {
					PerkUtils.OutputToAll("The Server will shutdown in " + PerkUtils.parseTime(m_time) + "...");
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (m_cancled) {
			PerkUtils.OutputToAll("Shutdown Terminated");
			PerkUtils.server().getScheduler().cancelTask(m_shutdownTaskID);
			return;
		}
	}
	
	public void cancel() {
		m_cancled = true;
	}
}