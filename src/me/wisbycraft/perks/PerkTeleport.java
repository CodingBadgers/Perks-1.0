package me.wisbycraft.perks;

import org.bukkit.command.Command;

public class PerkTeleport {
	
	private static boolean m_threadLock = false; 	//!< Still wont be strictly thread safe, but will stop the majority of potential memory access write violations
	
	public static void ThreadCall() {
		
		if (m_threadLock)
			return;
		
		
		
		
		
		
	}

	public static boolean onCommand(PerkPlayer player, Command cmd, String commandLabel, String[] args) {
				
		m_threadLock = true;
		
		PerkPlayer toPlayer = null;
		
		if (args.length == 1) {
			String playerName = args[0];
			toPlayer = PerkUtils.getPlayer(PerkUtils.server().getPlayer(playerName));
			
			if (toPlayer == null) {
				PerkUtils.OutputToPlayer(player, playerName + " isn't online.");
				m_threadLock = false;
				return false;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("tpr")) {
			
			if (args.length != 1) {
				PerkUtils.OutputToPlayer(player, "In correct usage of command");
				m_threadLock = false;
				return true;
			}
			
			if (!player.hasPermission("perks.teleport.tpr", true)) {
				m_threadLock = false;
				return true;
			}
			
			toPlayer.sendTpRequest(player);
			
			m_threadLock = false;
			return true;
		}
		
		// accepting and declining shouldn't have a permission as everyone needs to do it
		
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			
			player.acceptTpRequest(toPlayer);
			
			m_threadLock = false;
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("tpd")) {
			
			player.declineTpRequest(toPlayer);
			
			m_threadLock = false;
			return true;
		}
		
		m_threadLock = false;
		return false;
	}
	
}
