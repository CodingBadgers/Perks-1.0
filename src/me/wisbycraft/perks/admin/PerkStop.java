package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.Bukkit;
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
			
			
			int time = 10;
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
				PerkUtils.ErrorConsole("Shutting server down in " + time + " seconds");
			
			//PerkStop thread = new PerkStop(time);
			//thread.start();

			return true;
		}
		
		return false;
	}

	protected int time;

	public PerkStop (int time) {
		this.time = time;
	}

	public void run() {
		
		PerkUtils.OutputToAll("The Server will shutdown in " + time + " seconds...");
		
		while (time > 0) {
			try {
				Thread.sleep(1000);
				time--;
				if (time <= 5 && time != 0) {
					PerkUtils.OutputToAll("The Server will shutdown in " + time + " seconds...");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Bukkit.shutdown();		
	}
}