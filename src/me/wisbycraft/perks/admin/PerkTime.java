package me.wisbycraft.perks.admin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.wisbycraft.perks.utils.PerkArgSet;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.World;
import org.bukkit.command.Command;

public class PerkTime {

	protected static final Pattern TWELVE_HOUR_TIME = Pattern.compile("^([0-9]+(?::[0-9]+)?)([apmAPM\\.]+)$");
	
	public static void setTime(long time) {
		List<World> worlds = PerkUtils.server().getWorlds();
		
		for (int i = 0; i<worlds.size(); i++) {
			
			worlds.get(i).setTime(time);
		}
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
        	return (7 - 8 + 24) * 1000;
        } else if (timeStr.equalsIgnoreCase("sexytime")){
        	// to be handled later, first I need to finish flags/args
        }
        
		return -1;

    }
	
	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, PerkArgSet args) {
		
		if (commandLabel.equalsIgnoreCase("time")) {
			
			String timeStr;
			
			if (args.size() == 0) {
                timeStr = "current";
                // If no world was specified, get the world from the sender, but
                // fail if the sender isn't player
            } else {
                timeStr = args.getString(0);
            }
			
			boolean silent = false;
			if (args.hasFlag("s"))
				silent = true;
			
			// Let the player get the time
            if (timeStr.equalsIgnoreCase("current")
                    || timeStr.equalsIgnoreCase("cur")
                    || timeStr.equalsIgnoreCase("now")) {

                    if (!player.hasPermission("perks.time.check", true)) 
                    	return true; 
                    
                    PerkUtils.OutputToPlayer(player, "Time: " + getTimeString(player.getPlayer().getLocation().getWorld().getTime()));
                    return true;
                }
            
            if (!player.hasPermission("perks.time.change", true))
				return true;
            
            int time = matchTime(timeStr);
            
            if (time == -1) {
            	PerkUtils.OutputToPlayer(player, "Time value is invaid.");
            	return true;
            }
            
           	setTime(time);

           	if (!silent) 
            PerkUtils.OutputToAll(player.getPlayer().getName( )+ " set the time of all worlds to " + getTimeString(matchTime(timeStr)));
            return true;
		}
		
		return false;
	}
	
}
