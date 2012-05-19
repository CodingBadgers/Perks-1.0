package me.wman.perks.admin;

import me.wman.perks.utils.PerkUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkStop extends Thread{

	public static boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("stop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player)sender).hasPermission("perks.serveradmin.stop", true))
					return true;
			}
			
			
			int time = 20;
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
			
			PerkStop thread = new PerkStop(time);
			thread.start();

			return true;
		}
		
		if (commandLabel.equalsIgnoreCase("forcestop")) {
			
			if (sender instanceof Player) {
				if (!PerkUtils.getPlayer((Player) sender).hasPermission("perks.serveradmin.forcestop", true));
					return true;
			}
			
			PerkUtils.shutdownServer();
			return true;
		}
		
		return false;
	}

	private int m_time;

	public PerkStop (int time) {
		m_time = time;
	}

	public void run() {
		
		PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds...");
		
		while (m_time > 0) {
			try {
				Thread.sleep(1000);
				m_time--;
				
				if (m_time % 10 == 0 && m_time != 0) {
					PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds...");
				} else if (m_time <= 5 && m_time != 0) {
					PerkUtils.OutputToAll("The Server will shutdown in " + m_time + " seconds...");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		PerkUtils.shutdownServer();
	}
}