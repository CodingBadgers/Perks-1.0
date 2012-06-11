package me.wman.perks.admin;

import me.wman.perks.config.PerkConfig;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkStop extends Thread{

	private static PerkStop m_thread;
	
	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("stop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player)sender).hasPermission("perks.serveradmin.stop", true))
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
			
			if (sender instanceof Player)
				PerkUtils.OutputToPlayer((Player)sender, "Shutting server down in " + time + " seconds");
			else
				PerkUtils.DebugConsole("Shutting server down in " + time + " seconds");
			
			m_thread = new PerkStop(time);
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
				PerkUtils.OutputToPlayer((Player)sender, "Server shutdown aborted");
			else
				PerkUtils.DebugConsole("Server shutdown aborted");
			
			return true;
			
		}
		
		return false;
	}

	private int m_time;
	private boolean m_cancled;

	public PerkStop (int time) {
		m_time = time;
		m_cancled = false;
	}

	public void run() {
		
		PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds...");
		
		while (m_time > 0) {
			try {
				Thread.sleep(1000);
				m_time--;
				
				if (m_cancled) {
					PerkUtils.OutputToAll("Shutdown Terminated");
					return;
				}
				
				if (m_time % 10 == 0 && m_time != 0) {
					PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds..");
				} else if (m_time <= 5 && m_time != 0) {
					PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds..");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (m_cancled) {
			PerkUtils.OutputToAll("Shutdown Terminated");
			return;
		}
		
		PerkUtils.shutdownServer();
	}
	
	public void cancel() {
		m_cancled = true;
	}
}