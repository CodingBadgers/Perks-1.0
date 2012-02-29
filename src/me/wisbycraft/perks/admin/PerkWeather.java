package me.wisbycraft.perks.admin;

import java.util.List;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.World;
import org.bukkit.command.Command;

public class PerkWeather {

	public static void setStorm(boolean storm, int duration) {
		List<World> worlds = PerkUtils.server().getWorlds();
		
		for (int i = 0; i<worlds.size(); i++) {
			
			//if (worlds.get(i).getName().equalsIgnoreCase(/* pvp world */)) {
			//	return;
			//}
			
			worlds.get(i).setStorm(storm);
			
			if (duration>0) {

				worlds.get(i).setWeatherDuration(duration);
			}
		}
	}
	
	public static boolean onCommand(PerkPlayer player, Command cmds, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("weather")) {
			
			if (!player.hasPermission("perks.weather", true))
				return true;
			
	        String weatherStr = args[0];
	        int duration = -1;
	
	        if (args.length == 2) {
	            duration = Integer.parseInt(args[1]);
	        }
	
	        if (weatherStr.equalsIgnoreCase("stormy")
	                || weatherStr.equalsIgnoreCase("rainy")
	                || weatherStr.equalsIgnoreCase("snowy")
	                || weatherStr.equalsIgnoreCase("rain")
	                || weatherStr.equalsIgnoreCase("snow")
	                || weatherStr.equalsIgnoreCase("on")) {
	
	        	setStorm(true, duration * 20);
	            
	            PerkUtils.OutputToAll(player.getPlayer().getName() + " has started a storm");
	            return true;
	
	        } else if (weatherStr.equalsIgnoreCase("clear")
	                || weatherStr.equalsIgnoreCase("sunny")
	                || weatherStr.equalsIgnoreCase("snowy")
	                || weatherStr.equalsIgnoreCase("rain")
	                || weatherStr.equalsIgnoreCase("snow")
	                || weatherStr.equalsIgnoreCase("off")) {
	
	            setStorm(false, duration * 20);
	            
	            PerkUtils.OutputToAll(player.getPlayer().getName() + " has stopped a storm");
	            return true;
	
	        } else {
	            PerkUtils.OutputToPlayer(player, "Unknown weather state! Acceptable states: sunny or stormy");
	            return true;
	        }
		}
		
		return false;
	}
}
