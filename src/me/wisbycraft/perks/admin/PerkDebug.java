package me.wisbycraft.perks.admin;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;

public class PerkDebug {

	public static boolean onCommand(final PerkPlayer player, Command cmd, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("debug")) { 
			
			if (args[0].equalsIgnoreCase("clock")) {
				
				if (!player.hasPermission("perks.debug.clock", true))
					return true;
				
				 int expected = 5;

		         if (args.length == 2) {
		             expected = Math.min(30, Math.max(1, Integer.parseInt(args[1])));
		         }
		
		         player.getPlayer().sendMessage(ChatColor.DARK_RED
		                 + "Timing clock test for " + expected + " IN-GAME seconds...");
		         player.getPlayer().sendMessage(ChatColor.DARK_RED
		                 + "DO NOT CHANGE A WORLD'S TIME OR PERFORM A HEAVY OPERATION.");
		
		         final World world = PerkUtils.server().getWorlds().get(0);
		         final double expectedTime = expected * 1000;
		         final double expectedSecs = expected;
		         final int expectedTicks = 20 * (int)expectedSecs;
		         final long start = System.currentTimeMillis();
		         final long startTicks = world.getFullTime();
		
		         Runnable task = new Runnable() {
		             public void run() {
		                 long now = System.currentTimeMillis();
		                 long nowTicks = world.getFullTime();
		
		                 long elapsedTime = now - start;
		                 double elapsedSecs = elapsedTime / 1000.0;
		                 int elapsedTicks = (int) (nowTicks - startTicks);
		
		                 double error = (expectedTime - elapsedTime) / elapsedTime * 100;
		                 double clockRate = elapsedTicks / elapsedSecs;
		
		                 if (expectedTicks != elapsedTicks) {
		                     player.getPlayer().sendMessage(ChatColor.DARK_RED
		                             + "Warning: Bukkit scheduler inaccurate; expected "
		                             + expectedTicks + ", got " + elapsedTicks);
		                 }
		
		                 if (Math.round(clockRate) == 20) {
		                	 player.getPlayer().sendMessage(ChatColor.YELLOW + "Clock test result: "
		                             + ChatColor.GREEN + "EXCELLENT");
		                 } else {
		                     if (elapsedSecs > expectedSecs) {
		                         if (clockRate < 19) {
		                        	 player.getPlayer().sendMessage(ChatColor.YELLOW + "Clock test result: "
		                                     + ChatColor.DARK_RED + "CLOCK BEHIND");
		                        	 player.getPlayer().sendMessage(ChatColor.DARK_RED
		                                     + "WARNING: You have potential block respawn issues.");
		                         } else {
		                        	 player.getPlayer().sendMessage(ChatColor.YELLOW + "Clock test result: "
		                                     + ChatColor.DARK_RED + "CLOCK BEHIND");
		                         }
		                     } else {
		                    	 player.getPlayer().sendMessage(ChatColor.YELLOW + "Clock test result: "
		                                 + ChatColor.DARK_RED + "CLOCK AHEAD");
		                     }
		                 }
		
		                 player.getPlayer().sendMessage(ChatColor.GRAY + "Expected time elapsed: " + expectedTime + "ms");
		                 player.getPlayer().sendMessage(ChatColor.GRAY + "Time elapsed: " + elapsedTime + "ms");
		                 player.getPlayer().sendMessage(ChatColor.GRAY + "Error: " + error + "%");
		                 player.getPlayer().sendMessage(ChatColor.GRAY + "Actual clock rate: " + clockRate + " ticks/sec");
		                 player.getPlayer().sendMessage(ChatColor.GRAY + "Expected clock rate: 20 ticks/sec");
		             }
		         };
		
		         PerkUtils.server().getScheduler().scheduleSyncDelayedTask(PerkUtils.plugin, task, expectedTicks);
		         
		         return true;
			}
			
			if (args[0].equalsIgnoreCase("info")) {
				
				if (!player.hasPermission("perks.debug.info", true))
					return true;
				
				Runtime rt = Runtime.getRuntime();

	            player.getPlayer().sendMessage(ChatColor.YELLOW
	                    + String.format("System: %s %s (%s)",
	                    System.getProperty("os.name"),
	                    System.getProperty("os.version"),
	                    System.getProperty("os.arch")));
	            player.getPlayer().sendMessage(ChatColor.YELLOW
	                    + String.format("Java: %s %s (%s)",
	                    System.getProperty("java.vendor"),
	                    System.getProperty("java.version"),
	                    System.getProperty("java.vendor.url")));
	            player.getPlayer().sendMessage(ChatColor.YELLOW
	                    + String.format("JVM: %s %s %s",
	                    System.getProperty("java.vm.vendor"),
	                    System.getProperty("java.vm.name"),
	                    System.getProperty("java.vm.version")));

	            player.getPlayer().sendMessage(ChatColor.YELLOW + "Available processors: "
	                    + rt.availableProcessors());

	            player.getPlayer().sendMessage(ChatColor.YELLOW + "Available total memory: "
	                    + Math.floor(rt.maxMemory() / 1024.0 / 1024.0) + " MB");

	            player.getPlayer().sendMessage(ChatColor.YELLOW + "JVM allocated memory: "
	                    + Math.floor(rt.totalMemory() / 1024.0 / 1024.0) + " MB");

	            player.getPlayer().sendMessage(ChatColor.YELLOW + "Free allocated memory: "
	                    + Math.floor(rt.freeMemory() / 1024.0 / 1024.0) + " MB");
	            
	            return true;
	        }
			
			PerkUtils.OutputToPlayer(player, ChatColor.RED + "/debug <clock/info>");
			return true;
		}
		
		return false;
	}
}
