package uk.codingbadgers.perks.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import uk.codingbadgers.perks.utils.PerkArgSet;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;

public class PerkTime {

	protected static final Pattern TWELVE_HOUR_TIME = Pattern.compile("^([0-9]+(?::[0-9]+)?)([apmAPM\\.]+)$");
	
	private static Map<String, BukkitTask> lockedWorlds = new HashMap<String, BukkitTask>();
	
	public static void setTime(World world, long time) {
		if (lockedWorlds.containsKey(world.getName())) {
			// cancel current lock
			lockedWorlds.get(world.getName()).cancel();
			lockedWorlds.remove(world.getName());
		}
		
		world.setTime(time);
	}
	
	public static void lockTime(final World world) {
		final long time = world.getTime();
		
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(PerkUtils.plugin, new Runnable() {

			@Override
			public void run() {
				world.setTime(time);
			}
			
		}, 1, 1);
		
		lockedWorlds.put(world.getName(), task);
	}

	private static void setTime(Player player, long time, boolean lock) {
		player.setPlayerTime(time, !lock);
	}
	
	public static String getTimeString(long time) {
        int hours = (int) ((time / 1000 + 8) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d (%d:%02d %s)",
                hours, minutes, (hours % 12) == 0 ? 12 : hours % 12, minutes,
                hours < 12 ? "am" : "pm");
    }
	
	public static int matchTime(String timeStr) {
        Matcher matcher;

        try {
            int time = Integer.parseInt(timeStr);

            // People tend to enter just a number of the hour
            if (time <= 24) {
                return ((time - 8) % 24) * 1000;
            }

            return time;
        } catch (NumberFormatException e) {
            // Not an integer!
        }

        // Tick time
        if (timeStr.matches("^*[0-9]+$")) {
            return Integer.parseInt(timeStr.substring(1));

            // Allow 24-hour time
        } else if (timeStr.matches("^[0-9]+:[0-9]+$")) {
            String[] parts = timeStr.split(":");
            int hours = Integer.parseInt(parts[0]);
            int mins = Integer.parseInt(parts[1]);
            return (int) (((hours - 8) % 24) * 1000
                    + Math.round((mins % 60) / 60.0 * 1000));

            // Or perhaps 12-hour time
        } else if ((matcher = TWELVE_HOUR_TIME.matcher(timeStr)).matches()) {
            String time = matcher.group(1);
            String period = matcher.group(2);
            int shift;

            if (period.equalsIgnoreCase("am")
                    || period.equalsIgnoreCase("a.m.")) {
                shift = 0;
            } else if (period.equalsIgnoreCase("pm")
                    || period.equalsIgnoreCase("p.m.")) {
                shift = 12;
            } else {
                return -1;
            }

            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int mins = parts.length >= 2 ? Integer.parseInt(parts[1]) : 0;
            return (int) ((((hours % 12) + shift - 8) % 24) * 1000
                    + (mins % 60) / 60.0 * 1000);

            // Or some shortcuts
        } else if (timeStr.equalsIgnoreCase("dawn")) {
            return (6 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("sunrise")) {
            return (7 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("morning")) {
            return (24) * 1000;
        } else if (timeStr.equalsIgnoreCase("day")) {
            return (24) * 1000;
        } else if (timeStr.equalsIgnoreCase("midday")
                || timeStr.equalsIgnoreCase("noon")) {
            return (12 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("afternoon")) {
            return (14 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("evening")) {
            return (16 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("sunset")) {
            return (21 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("dusk")) {
            return (21 - 8 + 24) * 1000 + (int) (30 / 60.0 * 1000);
        } else if (timeStr.equalsIgnoreCase("night")) {
            return (22 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("midnight")) {
            return (0 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("s1m") || timeStr.equalsIgnoreCase("sim")) {
        	return 22618;
        }
        
		return -1;

    }
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("time")) {
			
			String timeStr;
           	World world = player.getPlayer().getWorld();

			if (args.size() >= 2) {
				world = Bukkit.getWorld(args.getString(1));
				
				if (world == null) {
					PerkUtils.OutputToPlayer(player, "The world " + args.getString(1) + " is not recognised on this server");
					return true;
				}
			}
			
			if (args.size() == 0) {
                timeStr = "current";
                // If no world was specified, get the world from the sender, but
                // fail if the sender isn't player
            } else {
                timeStr = args.getString(0);
            }
			
			boolean silent = args.hasFlag("s");
			boolean lock = args.hasFlag("l");
			boolean allWorlds = args.hasFlag("a");
			
			// Let the player get the time
            if (timeStr.equalsIgnoreCase("current")
                    || timeStr.equalsIgnoreCase("cur")
                    || timeStr.equalsIgnoreCase("now")) {

                    if (!player.hasPermission("perks.time.check", true)) 
                    	return true; 
                    
                    PerkUtils.OutputToPlayer(player, "Time: " + getTimeString(world.getTime()));
                    return true;
                }
            
            if (!player.hasPermission("perks.time.change", true))
				return true;
            
            int time = matchTime(timeStr);
            
            if (time == -1) {
            	PerkUtils.OutputToPlayer(player, "Time value is invaid.");
            	return true;
            }
            
            if (!allWorlds) {
            	setTime(world, time);
            }
            else {
            	for (World currentWorld : Bukkit.getWorlds()) {
            		setTime(currentWorld, time);
            	}
            }

           	if (lock) 
           		lockTime(world);

			if (!silent) 
           		PerkUtils.OutputToAll(player.getPlayer().getName( ) + " " + (lock ? "locked" : "set") + " the time of " + world.getName() + " to " + getTimeString(matchTime(timeStr)));
            return true;
		} else if (commandLabel.equalsIgnoreCase("playerTime")) {

			String timeStr;
           	
			Player target = player.getPlayer();
			
			if (args.size() >= 2) {
				target = Bukkit.getPlayer(args.getString(1));

				if (target == null) {
					PerkUtils.OutputToPlayer(player, "The player " + args.getString(1) + " is not recognised at this point in time on this server");
					return true;
				}
			}
			
			if (args.size() == 0) {
                timeStr = "current";
                // If no world was specified, get the world from the sender, but
                // fail if the sender isn't player
            } else {
                timeStr = args.getString(0);
            }
			
			boolean lock = false;
			
			if (args.hasFlag("l"))
				lock = true;
			
			// Let the player get the time
            if (timeStr.equalsIgnoreCase("current")
                    || timeStr.equalsIgnoreCase("cur")
                    || timeStr.equalsIgnoreCase("now")) {

            	if (!player.hasPermission("perks.player.time.check", true)) 
            		return true; 
                    
                PerkUtils.OutputToPlayer(player, "Time: " + getTimeString(target.getPlayerTime()));
                return true;
                
             } else if (timeStr.equalsIgnoreCase("reset")) {
            	 target.resetPlayerTime();
            	 PerkUtils.OutputToPlayer(player, "You have reset " + target.getName() + "'s player time to world time");
            	 PerkUtils.OutputToPlayer(target, "Your player time has been reset by " + player.getPlayer().getName());
               	return true;
             }
            
            if (!player.hasPermission("perks.player.time.change", true))
				return true;
            
            long time = matchTime(timeStr) - target.getWorld().getTime();
            
            if (time == -1) {
            	PerkUtils.OutputToPlayer(player, "Time value is invaid.");
            	return true;
            }
            
           	setTime(target, time, lock);

           	PerkUtils.OutputToPlayer(player, "You have " + (lock ? "locked" : "set") + " " + target.getName() + "'s player time to " + getTimeString(target.getPlayerTime()));
           	PerkUtils.OutputToPlayer(target, "Your player time has been " + (lock ? "locked" : "set") + " to " + getTimeString(target.getPlayerTime()) + " by " + player.getPlayer().getName());
           	return true;
		}
		
		return false;
	}
	
}
